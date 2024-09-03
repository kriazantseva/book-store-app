package mate.academy.bookstoreapp.dto.order;

import mate.academy.bookstoreapp.model.Order;

public record UpdateOrderStatusDto(
        Order.Status status
) {
}
