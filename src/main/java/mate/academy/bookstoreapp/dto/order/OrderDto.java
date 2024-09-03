package mate.academy.bookstoreapp.dto.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import mate.academy.bookstoreapp.dto.orderitem.OrderItemDto;
import mate.academy.bookstoreapp.model.Order;

public record OrderDto(
        Long id,
        Long userId,
        Set<OrderItemDto> orderItems,
        LocalDate orderDate,
        String shippingAddress,
        BigDecimal total,
        Order.Status status
) {
    public OrderDto(Long id,
                    Long userId,
                    Set<OrderItemDto> orderItems,
                    LocalDate orderDate,
                    String shippingAddress,
                    Order.Status status) {
        this(id, userId, orderItems, orderDate, shippingAddress, calcTotal(orderItems), status);
    }

    private static BigDecimal calcTotal(Set<OrderItemDto> orderItems) {
        return orderItems.stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
