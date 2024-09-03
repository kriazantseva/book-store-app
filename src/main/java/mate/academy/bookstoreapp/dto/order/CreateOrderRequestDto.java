package mate.academy.bookstoreapp.dto.order;

import jakarta.validation.constraints.NotEmpty;

public record CreateOrderRequestDto(
        @NotEmpty String shippingAddress
) {
}
