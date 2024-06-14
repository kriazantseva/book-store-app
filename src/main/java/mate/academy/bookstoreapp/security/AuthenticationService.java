package mate.academy.bookstoreapp.security;

import mate.academy.bookstoreapp.dto.user.UserLoginRequestDto;

public interface AuthenticationService {
    boolean authenticate(UserLoginRequestDto requestDto);
}
