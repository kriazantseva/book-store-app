package mate.academy.bookstoreapp.service.impl;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreapp.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreapp.dto.user.UserResponseDto;
import mate.academy.bookstoreapp.exceptions.RegistrationException;
import mate.academy.bookstoreapp.mapper.UserMapper;
import mate.academy.bookstoreapp.model.User;
import mate.academy.bookstoreapp.repository.user.UserRepository;
import mate.academy.bookstoreapp.service.UserService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("Can't register the user with this email. "
                    + "The email is already existed.");
        }

        User user = userMapper.toUser(requestDto);
        return userMapper.toUserResponseDto(userRepository.save(user));
    }
}
