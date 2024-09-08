package mate.academy.bookstoreapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import mate.academy.bookstoreapp.dto.order.CreateOrderRequestDto;
import mate.academy.bookstoreapp.dto.order.OrderDto;
import mate.academy.bookstoreapp.dto.order.UpdateOrderStatusDto;
import mate.academy.bookstoreapp.dto.orderitem.OrderItemDto;
import mate.academy.bookstoreapp.model.User;
import mate.academy.bookstoreapp.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RestController
@RequestMapping("/order")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Make an order", description = "Transform cart to order")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void makeOrder(
            @AuthenticationPrincipal User user,
            @RequestBody @Valid CreateOrderRequestDto requestDto
    ) {
        orderService.transformCartToOrder(user.getId(), requestDto);
    }

    @Operation(summary = "Get orders", description = "Get orders by user id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<OrderDto> getAllOrders(Pageable pageable, @AuthenticationPrincipal User user) {
        return orderService.getAllOrdersByUserId(pageable, user.getId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change order status", description = "Change order status")
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{orderId}")
    public OrderDto updateOrderStatus(@PathVariable Long orderId,
                                   UpdateOrderStatusDto updateOrderStatusDto) {
        return orderService.changeStatus(orderId, updateOrderStatusDto);
    }

    @Operation(summary = "Get order items set",
            description = "Get order items set by order and user ids")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{orderId}/items")
    public Set<OrderItemDto> getOrderItemsSet(@PathVariable Long orderId,
                                              @AuthenticationPrincipal User user) {
        return orderService.getOrderItemsByOrderId(user.getId(), orderId);
    }

    @Operation(summary = "Get order item",
            description = "Get order item from order by order, item and user ids")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItemById(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @AuthenticationPrincipal User user
    ) {
        return orderService.getOrderItemById(user.getId(), orderId, itemId);
    }
}
