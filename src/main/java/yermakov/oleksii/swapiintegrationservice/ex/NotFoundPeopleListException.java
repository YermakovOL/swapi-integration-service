package yermakov.oleksii.swapiintegrationservice.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundPeopleListException extends ResponseStatusException {
  public NotFoundPeopleListException() {
    super(HttpStatus.NOT_FOUND, "Page of people list is not found");
  }
}
