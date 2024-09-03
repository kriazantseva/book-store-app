package mate.academy.bookstoreapp.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreapp.dto.order.OrderDto;
import mate.academy.bookstoreapp.dto.order.UpdateOrderStatusDto;
import mate.academy.bookstoreapp.dto.orderitem.OrderItemDto;
import mate.academy.bookstoreapp.mapper.OrderItemMapper;
import mate.academy.bookstoreapp.mapper.OrderMapper;
import mate.academy.bookstoreapp.model.Order;
import mate.academy.bookstoreapp.model.OrderItem;
import mate.academy.bookstoreapp.repository.order.OrderRepository;
import mate.academy.bookstoreapp.service.OrderService;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderDto getOrdersByUserId(Long userId) {
        Order order = orderRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id: " + userId + " had no orders")
        );
        return orderMapper.toDto(order);
    }

    @Override
    public Set<OrderItemDto> getOrderItemsByOrderId(Long userId, Long orderId) {
        Order order = findOrderByUserIdAndOrderId(userId, orderId);
        Set<OrderItem> orderItems = order.getOrderItems();
        return orderItemMapper.toDtoSet(orderItems);
    }

    @Override
    public OrderItemDto getOrderItemById(Long userId, Long orderId, Long orderItemId) {
        Order order = findOrderByUserIdAndOrderId(userId, orderId);
        Set<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getId().equals(orderItemId)) {
                return orderItemMapper.toDto(orderItem);
            }
        }
        throw new EntityNotFoundException("Item with id: " + orderItemId + " had no order items");
    }

    @Override
    public void changeStatus(Long orderId, UpdateOrderStatusDto updateOrderStatusDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Order with id: " + orderId + " not found"));
        orderMapper.updateOrderFromDto(updateOrderStatusDto, order);
        orderRepository.save(order);
    }

    private Order findOrderByUserIdAndOrderId(Long userId, Long orderId) {
        return orderRepository.findByUserIdAndOrderId(userId, orderId).orElseThrow(
                () -> new EntityNotFoundException("Order not found"));
    }
}
