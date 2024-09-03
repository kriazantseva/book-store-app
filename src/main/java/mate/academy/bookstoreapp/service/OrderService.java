package mate.academy.bookstoreapp.service;

import java.util.Set;
import mate.academy.bookstoreapp.dto.order.OrderDto;
import mate.academy.bookstoreapp.dto.order.UpdateOrderStatusDto;
import mate.academy.bookstoreapp.dto.orderitem.OrderItemDto;

public interface OrderService {
    OrderDto getOrdersByUserId(Long userId);

    Set<OrderItemDto> getOrderItemsByOrderId(Long userId, Long orderId);

    OrderItemDto getOrderItemById(Long userId, Long orderId, Long orderItemId);

    void changeStatus(Long orderId, UpdateOrderStatusDto updateOrderStatusDto);
}
