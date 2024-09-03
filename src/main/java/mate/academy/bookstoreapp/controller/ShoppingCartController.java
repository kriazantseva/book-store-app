package mate.academy.bookstoreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreapp.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.bookstoreapp.dto.order.CreateOrderRequestDto;
import mate.academy.bookstoreapp.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstoreapp.model.User;
import mate.academy.bookstoreapp.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ShoppingCart management", description = "Endpoints for managing shopping carts")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get ShoppingCart", description = "Get all items in shopping cart")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.getShoppingCartByUserId(user.getId());
    }

    @Operation(summary = "Add to ShoppingCart", description = "Add an item to shopping cart")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ShoppingCartDto addToShoppingCart(
            Authentication authentication,
            @RequestBody @Valid CreateCartItemRequestDto requestDto
    ) {
        User user = (User) authentication.getPrincipal();
        return shoppingCartService.addItemToShoppingCart(requestDto, user.getId());
    }

    @Operation(summary = "Update item in ShoppingCart",
            description = "Update item in shopping cart by id")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartDto updateItemInShoppingCart(
            @PathVariable Long cartItemId,
            @RequestBody @Valid CreateCartItemRequestDto requestDto
    ) {
        return shoppingCartService.updateCartItem(cartItemId, requestDto);
    }

    @Operation(summary = "Remove item from ShoppingCart",
            description = "Remove item from shopping cart by id")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/items/{cartItemId}")
    public ShoppingCartDto removeItemFromShoppingCart(@PathVariable Long cartItemId) {
        return shoppingCartService.deleteCartItem(cartItemId);
    }

    @Operation(summary = "Clean the ShoppingCart", description = "Clean the shopping cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/clean")
    public void cleanCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.clearCart(user.getId());
    }

    @Operation(summary = "Make an order", description = "Transform cart to order")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/order")
    public void makeOrder(
            Authentication authentication,
            @RequestBody @Valid CreateOrderRequestDto requestDto
    ) {
        User user = (User) authentication.getPrincipal();
        shoppingCartService.transformCartToOrder(user.getId(), requestDto);
    }
}
