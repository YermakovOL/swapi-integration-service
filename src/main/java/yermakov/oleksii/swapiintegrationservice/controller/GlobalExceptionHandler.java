package yermakov.oleksii.swapiintegrationservice.controller;

import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

import feign.FeignException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import yermakov.oleksii.swapiintegrationservice.dto.api.ValidationErrorResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(HandlerMethodValidationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ValidationErrorResponse> handleValidationExceptions(
      HandlerMethodValidationException ex) {

    return ex.getParameterValidationResults().stream()
        .flatMap(
            result ->
                result.getResolvableErrors().stream()
                    .map(
                        error ->
                            new ValidationErrorResponse(
                                result.getMethodParameter().getParameterName(),
                                error.getDefaultMessage())))
        .toList();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ValidationErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex) {

    return ex.getBindingResult().getFieldErrors().stream()
        .map(error -> new ValidationErrorResponse(error.getField(), error.getDefaultMessage()))
        .toList();
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ValidationErrorResponse handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String parameter = ex.getName();
    String type =
        Optional.ofNullable(ex.getRequiredType()).map(Class::getSimpleName).orElse("unknown");
    String message = String.format("Keep the type: %s", type);

    return new ValidationErrorResponse(parameter, message);
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Map<String, String>> handleResponseStatusException(
      ResponseStatusException ex) {
    return ResponseEntity.status(ex.getStatusCode())
        .body(Map.of("errorMessage", Optional.ofNullable(ex.getReason()).orElse(ex.getMessage())));
  }

  @ExceptionHandler(FeignException.class)
  public ResponseEntity<Map<String, String>> handleFeignException(FeignException ex) {
    String message =
        Optional.ofNullable(ex.contentUTF8())
            .filter(StringUtils::isNotBlank)
            .orElse(ex.getMessage());

    log.error("Integration error has occurred: {}", message);

    return ResponseEntity.status(SERVICE_UNAVAILABLE)
        .body(Map.of("errorMessage", "Service temporary unavailable"));
  }
}
