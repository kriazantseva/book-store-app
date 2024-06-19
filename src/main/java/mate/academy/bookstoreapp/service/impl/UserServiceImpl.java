package mate.academy.bookstoreapp.service.impl;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreapp.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstoreapp.dto.user.UserResponseDto;
import mate.academy.bookstoreapp.exceptions.RegistrationException;
import mate.academy.bookstoreapp.mapper.UserMapper;
import mate.academy.bookstoreapp.model.Role;
import mate.academy.bookstoreapp.model.User;
import mate.academy.bookstoreapp.repository.role.RoleRepository;
import mate.academy.bookstoreapp.repository.user.UserRepository;
import mate.academy.bookstoreapp.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("Can't register the user with this email. "
                    + "The email is already existed.");
        }

        User user = userMapper.toUser(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.password()));
        if (requestDto.email().contains("@admin")) {
            setRolesToUser(user, Role.RoleName.ADMIN);
        } else {
            setRolesToUser(user, Role.RoleName.USER);
        }
        return userMapper.toUserResponseDto(userRepository.save(user));
    }

    private void setRolesToUser(User user, Role.RoleName roleName) {
        Role role = roleRepository.findByRole(roleName).orElseThrow(() ->
                new NoSuchElementException("Role " + roleName + " not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
    }
}
