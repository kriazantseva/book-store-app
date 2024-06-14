package mate.academy.bookstoreapp.security;

import java.util.List;
import lombok.Getter;

public class PublicAvailableEndpoints {
    @Getter
    private static List<String> publicEndpoints = List.of(
            "/api/auth/login",
            "/api/auth/registration"
    );

}
