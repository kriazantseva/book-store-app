package mate.academy.bookstoreapp.mapper;

import mate.academy.bookstoreapp.config.MapperConfig;
import mate.academy.bookstoreapp.dto.user.UserResponseDto;
import mate.academy.bookstoreapp.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toUserResponseDto(User user);
}
