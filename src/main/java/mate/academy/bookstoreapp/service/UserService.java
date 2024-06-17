package mate.academy.bookstoreapp.service;

import mate.academy.bookstoreapp.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreapp.dto.user.UserResponseDto;
import mate.academy.bookstoreapp.exceptions.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
