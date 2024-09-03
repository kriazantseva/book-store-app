package mate.academy.bookstoreapp.dto.orderitem;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateOrderItemRequestDto(
        @NotNull @Positive(message = "The book id can't be negative") Long bookId,
        @Min(1) @Max(100) @NotNull int quantity
) {
}
