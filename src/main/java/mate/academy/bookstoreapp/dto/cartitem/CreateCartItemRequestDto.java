package mate.academy.bookstoreapp.dto.cartitem;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartItemRequestDto(
        @NotNull @Positive(message = "The book id can't be negative") Long bookId,
        @NotNull @Positive @Min(1) @Max(100) int quantity
) {
}
