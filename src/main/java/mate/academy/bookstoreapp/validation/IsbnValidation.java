package mate.academy.bookstoreapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IsbnValidation implements ConstraintValidator<Isbn, String> {
    private static final String PATTERN_OF_ISBN = "\\d{3}-\\d-\\d{2}-\\d{6}-\\d";

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext constraintValidatorContext) {
        return isbn != null && Pattern.compile(PATTERN_OF_ISBN).matcher(isbn).matches();
    }
}
