package mate.academy.bookstoreapp.dto.order;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequestDto(
        @NotBlank String shippingAddress
) {
}
