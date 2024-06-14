package mate.academy.bookstoreapp.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SecurityContext {
    private Authentication authentication;
}
