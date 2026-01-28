package yermakov.oleksii.swapiintegrationservice.config;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ApiFormatPropertiesTest {

  private static final String DEFAULT_DATE_FORMAT = "dd-MM-yyyy";
  private static final String DEFAULT_MASS_FORMAT = "%s kg";
  private static final String DEFAULT_HEIGHT_FORMAT = "%s meters";

  private Validator validator;

  @BeforeEach
  void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @ParameterizedTest
  @MethodSource("validConfigurationProvider")
  void shouldPassValidation(String dateFormat, String massFormat, String heightFormat) {
    var properties = new ApiFormatProperties(dateFormat, massFormat, heightFormat);

    var violations = validator.validate(properties);

    assertThat(violations).isEmpty();
  }

  @ParameterizedTest
  @MethodSource("invalidDateConfigurationProvider")
  void shouldFailOnInvalidDateFormat(String dateFormat) {
    var properties =
        new ApiFormatProperties(dateFormat, DEFAULT_MASS_FORMAT, DEFAULT_HEIGHT_FORMAT);

    var violations = validator.validate(properties);

    assertThat(violations).hasSize(1);
    assertThat(violations.iterator().next().getPropertyPath()).hasToString("dateFormat");
  }

  @ParameterizedTest
  @MethodSource("invalidStringConfigurationProvider")
  void shouldFailOnInvalidMassFormat(String massFormat) {
    var properties =
        new ApiFormatProperties(DEFAULT_DATE_FORMAT, massFormat, DEFAULT_HEIGHT_FORMAT);

    var violations = validator.validate(properties);

    assertThat(violations).hasSize(1);
    var violation = violations.iterator().next();
    assertThat(violation.getPropertyPath()).hasToString("massFormat");
    assertThat(violation.getMessage()).contains("Invalid format string");
  }

  @ParameterizedTest
  @MethodSource("invalidStringConfigurationProvider")
  void shouldFailOnInvalidHeightFormat(String heightFormat) {
    var properties =
        new ApiFormatProperties(DEFAULT_DATE_FORMAT, DEFAULT_MASS_FORMAT, heightFormat);

    var violations = validator.validate(properties);

    assertThat(violations).hasSize(1);
    var violation = violations.iterator().next();
    assertThat(violation.getPropertyPath()).hasToString("heightFormat");
    assertThat(violation.getMessage()).contains("Invalid format string");
  }

  static Stream<Arguments> validConfigurationProvider() {
    return Stream.of(
        Arguments.of("dd-MM-yyyy", "%s kg", "%s meters"),
        Arguments.of("yyyy-MM-dd'T'HH:mm:ss", "Mass: %s", "Height: %s"),
        Arguments.of("EEE, d MMM yyyy", "%s", "%s"));
  }

  static Stream<Arguments> invalidDateConfigurationProvider() {
    return Stream.of(
        Arguments.of("invalid-pattern-p"),
        Arguments.of("yyyy-MM-dd'T'UnclosedQuote"),
        Arguments.of((String) null),
        Arguments.of(""));
  }

  static Stream<Arguments> invalidStringConfigurationProvider() {
    return Stream.of(Arguments.of("%d kg"), Arguments.of("%.2f meters"), Arguments.of("%d"));
  }
}
