package mate.academy.bookstoreapp.service;

import mate.academy.bookstoreapp.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.bookstoreapp.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto addItemToShoppingCart(CreateCartItemRequestDto requestDto, Long userId);

    ShoppingCartDto getShoppingCartByUserId(Long userId);

    ShoppingCartDto updateCartItem(Long id, CreateCartItemRequestDto requestDto);

    ShoppingCartDto deleteCartItem(Long id);

    void clearCart(Long userId);
}
