package mate.academy.bookstoreapp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidation.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "Weak password. You need minimum eight characters, "
            + "at least one uppercase letter, one lowercase letter and one number";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
