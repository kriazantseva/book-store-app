package mate.academy.bookstoreapp.dto.orderitem;

import java.math.BigDecimal;

public record OrderItemDto(
        Long id,
        Long bookId,
        String bookTitle,
        BigDecimal bookPrice,
        int quantity,
        BigDecimal price
) {
}
