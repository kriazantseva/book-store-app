package mate.academy.bookstoreapp.service;

import java.util.List;
import java.util.Set;
import mate.academy.bookstoreapp.dto.order.CreateOrderRequestDto;
import mate.academy.bookstoreapp.dto.order.OrderDto;
import mate.academy.bookstoreapp.dto.order.UpdateOrderStatusDto;
import mate.academy.bookstoreapp.dto.orderitem.OrderItemDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    void transformCartToOrder(Long id, CreateOrderRequestDto requestDto);

    List<OrderDto> getAllOrdersByUserId(Pageable pageable, Long userId);

    Set<OrderItemDto> getOrderItemsByOrderId(Long userId, Long orderId);

    OrderItemDto getOrderItemById(Long userId, Long orderId, Long orderItemId);

    OrderDto changeStatus(Long orderId, UpdateOrderStatusDto updateOrderStatusDto);
}
