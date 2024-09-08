package mate.academy.bookstoreapp.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreapp.dto.order.CreateOrderRequestDto;
import mate.academy.bookstoreapp.dto.order.OrderDto;
import mate.academy.bookstoreapp.dto.order.UpdateOrderStatusDto;
import mate.academy.bookstoreapp.dto.orderitem.CreateOrderItemRequestDto;
import mate.academy.bookstoreapp.dto.orderitem.OrderItemDto;
import mate.academy.bookstoreapp.mapper.OrderItemMapper;
import mate.academy.bookstoreapp.mapper.OrderMapper;
import mate.academy.bookstoreapp.model.CartItem;
import mate.academy.bookstoreapp.model.Order;
import mate.academy.bookstoreapp.model.OrderItem;
import mate.academy.bookstoreapp.model.ShoppingCart;
import mate.academy.bookstoreapp.model.User;
import mate.academy.bookstoreapp.repository.book.BookRepository;
import mate.academy.bookstoreapp.repository.order.OrderRepository;
import mate.academy.bookstoreapp.repository.orderitem.OrderItemRepository;
import mate.academy.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstoreapp.repository.user.UserRepository;
import mate.academy.bookstoreapp.service.OrderService;
import mate.academy.bookstoreapp.service.ShoppingCartService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ShoppingCartRepository cartRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartService shoppingCartService;

    @Transactional
    @Override
    public void transformCartToOrder(Long id, CreateOrderRequestDto requestDto) {
        Order order = orderMapper.toEntity(requestDto);
        ShoppingCart shoppingCart = getCartByUserId(id);
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        for (CartItem item : cartItems) {
            CreateOrderItemRequestDto orderItemRequestDto = new CreateOrderItemRequestDto(
                    item.getBook().getId(),
                    item.getQuantity()
            );
            OrderItem orderItem = createOrderItem(orderItemRequestDto, bookRepository);
            orderItem.setOrder(order);
            orderItemRepository.save(orderItem);
            order.getOrderItems().add(orderItem);
        }
        orderRepository.save(order);
        shoppingCartService.clearCart(id);
    }

    @Override
    public List<OrderDto> getAllOrdersByUserId(Pageable pageable, Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId, pageable).getContent();
        if (orders.isEmpty()) {
            throw new EntityNotFoundException("No orders found for user " + userId);
        }
        return orderMapper.toDtoList(orders);
    }

    @Override
    public Set<OrderItemDto> getOrderItemsByOrderId(Long userId, Long orderId) {
        Order order = findOrderByUserIdAndOrderId(userId, orderId);
        Set<OrderItem> orderItems = order.getOrderItems();
        return orderItemMapper.toDtoSet(orderItems);
    }

    @Override
    public OrderItemDto getOrderItemById(Long userId, Long orderId, Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderIdAndUserId(
                orderItemId, orderId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item with id: " + orderItemId + " had no order items"));
        return orderItemMapper.toDto(orderItem);
    }

    @Transactional
    @Override
    public OrderDto changeStatus(Long orderId, UpdateOrderStatusDto updateOrderStatusDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order with id: " + orderId + " not found"));
        orderMapper.updateOrderFromDto(updateOrderStatusDto, order);
        return orderMapper.toDto(orderRepository.save(order));
    }

    private Order findOrderByUserIdAndOrderId(Long userId, Long orderId) {
        return orderRepository.findByUserIdAndOrderId(userId, orderId).orElseThrow(
                () -> new EntityNotFoundException("Order not found"));
    }

    private OrderItem createOrderItem(
            CreateOrderItemRequestDto requestDto,
            BookRepository bookRepository
    ) {
        return orderItemMapper.toEntity(requestDto, bookRepository);
    }

    private ShoppingCart getCartByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new mate.academy.bookstoreapp.exceptions.EntityNotFoundException(
                        "User with id " + userId + " not found")
        );
        return cartRepository.findShoppingCartByUser(user).orElseThrow(
                () -> new mate.academy.bookstoreapp.exceptions.EntityNotFoundException(
                        "Shopping cart with id " + userId + " not found")
        );
    }
}
