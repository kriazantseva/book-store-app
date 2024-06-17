package mate.academy.bookstoreapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import mate.academy.bookstoreapp.validation.FieldMatch;
import org.hibernate.validator.constraints.Length;

@FieldMatch.List({
        @FieldMatch(
                first = "password",
                second = "repeatPassword",
                message = "Passwords must match"
        )
})
public record UserRegistrationRequestDto(
        @NotBlank @Email String email,
        @NotBlank @Length(min = 8, max = 20) String password,
        @NotBlank @Length(min = 8, max = 20) String repeatPassword,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String shippingAddress
) {
}
