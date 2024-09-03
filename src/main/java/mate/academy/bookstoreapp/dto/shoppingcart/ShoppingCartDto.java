package mate.academy.bookstoreapp.dto.shoppingcart;

import java.util.Set;
import mate.academy.bookstoreapp.dto.cartitem.CartItemDto;

public record ShoppingCartDto(
        Long id,
        Long userId,
        Set<CartItemDto> cartItems
) {
}
