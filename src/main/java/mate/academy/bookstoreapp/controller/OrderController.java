package mate.academy.bookstoreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Set;
import lombok.AllArgsConstructor;
import mate.academy.bookstoreapp.dto.order.OrderDto;
import mate.academy.bookstoreapp.dto.order.UpdateOrderStatusDto;
import mate.academy.bookstoreapp.dto.orderitem.OrderItemDto;
import mate.academy.bookstoreapp.model.User;
import mate.academy.bookstoreapp.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Get orders", description = "Get orders by user id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public OrderDto getOrder(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrdersByUserId(user.getId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change order status", description = "Change order status")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{orderId}")
    public void updateOrderStatus(@PathVariable Long orderId,
                                   UpdateOrderStatusDto updateOrderStatusDto) {
        orderService.changeStatus(orderId, updateOrderStatusDto);
    }

    @Operation(summary = "Get order items set",
            description = "Get order items set by order and user ids")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{orderId}/items")
    public Set<OrderItemDto> getOrderItemsSet(@PathVariable Long orderId,
                                            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItemsByOrderId(user.getId(), orderId);
    }

    @Operation(summary = "Get order item",
            description = "Get order item from order by order, item and user ids")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItemById(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItemById(user.getId(), orderId, itemId);
    }
}
