package mate.academy.bookstoreapp.dto.cartitem;

public record CartItemDto(
        Long id,
        Long bookId,
        Long bookTitle,
        Long bookPrice,
        int quantity
) {
}
