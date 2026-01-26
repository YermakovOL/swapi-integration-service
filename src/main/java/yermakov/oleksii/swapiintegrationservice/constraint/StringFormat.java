package yermakov.oleksii.swapiintegrationservice.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StringFormatValidator.class)
public @interface StringFormat {

    String message() default "Invalid format string: must be compatible with a single String argument (e.g., '%s')";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}