package mate.academy.bookstoreapp.dto.orderitem;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/*Use CreateOrderItemRequestDto in ShoppingCartServiceImpl in transformCartToOrder method
* for mapping CartItem into OrderItem using @param bookId and @param quantity */
public record CreateOrderItemRequestDto(
        @NotNull @Positive(message = "The book id can't be negative") Long bookId,
        @Min(1) @Max(100) @NotNull int quantity
) {
}
