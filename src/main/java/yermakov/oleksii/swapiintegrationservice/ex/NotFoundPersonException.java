package yermakov.oleksii.swapiintegrationservice.ex;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundPersonException extends ResponseStatusException {
    public NotFoundPersonException() {
        super(HttpStatus.NOT_FOUND, "Person not found");
    }
}
