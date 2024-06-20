package mate.academy.bookstoreapp.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import mate.academy.bookstoreapp.validation.FieldMatch;
import mate.academy.bookstoreapp.validation.Password;
import org.hibernate.validator.constraints.Length;

@FieldMatch.List({
        @FieldMatch(
                first = "password",
                second = "repeatPassword",
                message = "Passwords must match"
        )
})
public record UserRegistrationRequestDto(
        @NotBlank(message = "Email can't be empty") @Email String email,
        @NotBlank(message = "Password can't be empty")
        @Password @Length(min = 8, max = 20) String password,
        @NotBlank @Length(min = 8, max = 20) String repeatPassword,
        @NotBlank(message = "First name can't be empty") String firstName,
        @NotBlank(message = "Last name can't be empty") String lastName,
        String shippingAddress
) {
}
