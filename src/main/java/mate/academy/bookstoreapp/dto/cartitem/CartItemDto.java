package mate.academy.bookstoreapp.dto.cartitem;

import java.math.BigDecimal;

public record CartItemDto(
        Long id,
        Long bookId,
        String bookTitle,
        BigDecimal bookPrice,
        int quantity
) {
}
