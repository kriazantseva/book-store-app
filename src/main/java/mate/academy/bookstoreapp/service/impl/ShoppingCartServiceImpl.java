package mate.academy.bookstoreapp.service.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreapp.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.bookstoreapp.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstoreapp.exceptions.EntityNotFoundException;
import mate.academy.bookstoreapp.mapper.CartItemMapper;
import mate.academy.bookstoreapp.mapper.ShoppingCartMapper;
import mate.academy.bookstoreapp.model.CartItem;
import mate.academy.bookstoreapp.model.ShoppingCart;
import mate.academy.bookstoreapp.model.User;
import mate.academy.bookstoreapp.repository.book.BookRepository;
import mate.academy.bookstoreapp.repository.cartitem.CartItemRepository;
import mate.academy.bookstoreapp.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookstoreapp.repository.user.UserRepository;
import mate.academy.bookstoreapp.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto addItemToShoppingCart(CreateCartItemRequestDto requestDto, Long userId) {
        ShoppingCart shoppingCart = getCartByUserId(userId);
        checkCartItem(requestDto, shoppingCart);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartDto getShoppingCartByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id " + userId + " not found")
        );
        return shoppingCartRepository.findShoppingCartByUser(user)
                .map(shoppingCartMapper::toDto)
                .orElseThrow(
                        () -> new EntityNotFoundException("Shopping cart with user "
                                + user + " not found")
                );
    }

    @Override
    public ShoppingCartDto updateCartItem(Long id, CreateCartItemRequestDto requestDto) {
        CartItem cartItem = getCartItemById(id);
        cartItemMapper.updateCartItemFromDto(requestDto, cartItem);
        cartItemRepository.save(cartItem);
        ShoppingCart shoppingCart = cartItem.getShoppingCart();
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto deleteCartItem(Long id) {
        CartItem cartItem = getCartItemById(id);
        ShoppingCart shoppingCart = cartItem.getShoppingCart();
        cartItemRepository.delete(cartItem);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void clearCart(Long userId) {
        ShoppingCart shoppingCart = getCartByUserId(userId);
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
    }

    private void checkCartItem(CreateCartItemRequestDto requestDto, ShoppingCart shoppingCart) {
        Optional<CartItem> existingCartItem = shoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(requestDto.bookId()))
                .findFirst();
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + requestDto.quantity());
            cartItemRepository.save(cartItem);
        } else {
            CartItem cartItem = cartItemMapper.toEntity(requestDto, bookRepository);
            cartItem.setShoppingCart(shoppingCart);
            cartItemRepository.save(cartItem);
            shoppingCart.getCartItems().add(cartItem);
        }
    }

    private CartItem getCartItemById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cart item with id " + id + " not found")
        );
    }

    private ShoppingCart getCartByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User with id " + userId + " not found")
        );
        return shoppingCartRepository.findShoppingCartByUser(user).orElseThrow(
                () -> new EntityNotFoundException("Shopping cart with id " + userId + " not found")
        );
    }
}
