package mate.academy.bookstoreapp.mapper;

import java.util.List;
import mate.academy.bookstoreapp.config.MapperConfig;
import mate.academy.bookstoreapp.dto.order.CreateOrderRequestDto;
import mate.academy.bookstoreapp.dto.order.OrderDto;
import mate.academy.bookstoreapp.dto.order.UpdateOrderStatusDto;
import mate.academy.bookstoreapp.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(source = "orderItems", target = "orderItems")
    OrderDto toDto(Order order);

    Order toEntity(CreateOrderRequestDto requestDto);

    List<OrderDto> toDtoList(List<Order> orders);

    void updateOrderFromDto(UpdateOrderStatusDto requestDto, @MappingTarget Order order);
}
