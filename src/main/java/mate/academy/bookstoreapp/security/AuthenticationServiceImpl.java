package mate.academy.bookstoreapp.security;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstoreapp.dto.user.UserLoginRequestDto;
import mate.academy.bookstoreapp.model.User;
import mate.academy.bookstoreapp.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;

    @Override
    public boolean authenticate(UserLoginRequestDto requestDto) {
        Optional<User> user = userRepository.findByEmail(requestDto.email());
        return user.isPresent() && user.get().getPassword().equals(requestDto.password());
    }
}
