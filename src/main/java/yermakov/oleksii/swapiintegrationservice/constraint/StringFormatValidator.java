package yermakov.oleksii.swapiintegrationservice.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.IllegalFormatException;

public class StringFormatValidator implements ConstraintValidator<StringFormat, String> {

  @Override
  public boolean isValid(String format, ConstraintValidatorContext context) {
    if (format == null) {
      return true;
    }

    try {
      String.format(format, "test-input");
    } catch (IllegalFormatException e) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(
              "Invalid format string '" + format + "': " + e.getMessage())
          .addConstraintViolation();

      return false;
    }

    return true;
  }
}
