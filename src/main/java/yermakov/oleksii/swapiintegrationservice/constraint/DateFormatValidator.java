package yermakov.oleksii.swapiintegrationservice.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.format.DateTimeFormatter;

public class DateFormatValidator implements ConstraintValidator<ValidDateTimeFormat, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }
    try {
      DateTimeFormatter.ofPattern(value);
    } catch (IllegalArgumentException e) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate(
              "Invalid date pattern '" + value + "': " + e.getMessage())
          .addConstraintViolation();
      return false;
    }

    return true;
  }
}
