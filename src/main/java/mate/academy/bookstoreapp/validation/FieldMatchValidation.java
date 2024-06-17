package mate.academy.bookstoreapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.springframework.beans.BeanWrapperImpl;

public class FieldMatchValidation implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object firstObj = new BeanWrapperImpl(value).getPropertyValue(firstFieldName);
        Object secondObj = new BeanWrapperImpl(value).getPropertyValue(secondFieldName);
        return Objects.equals(firstObj, secondObj);
    }
}
