package mate.academy.bookstoreapp.mapper;

import mate.academy.bookstoreapp.config.MapperConfig;
import mate.academy.bookstoreapp.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookstoreapp.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    @Mapping(target = "cartItems", source = "cartItems")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
