package yermakov.oleksii.swapiintegrationservice.client;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.util.regex.Pattern;
import yermakov.oleksii.swapiintegrationservice.ex.NotFoundPeopleListException;
import yermakov.oleksii.swapiintegrationservice.ex.NotFoundPersonException;

public class SwapiErrorDecoder implements ErrorDecoder {

  private final ErrorDecoder defaultErrorDecoder;

  private static final Pattern PEOPLE_LIST_PATTERN = Pattern.compile("(?s).*/people(\\?.*)?");
  private static final Pattern PEOPLE_ITEM_PATTERN =
      Pattern.compile("(?s).*/people/[^/?]+(\\?.*)?");

  public SwapiErrorDecoder() {
    defaultErrorDecoder = new Default();
  }

  @Override
  public Exception decode(String methodKey, Response response) {

    Exception decoded = defaultErrorDecoder.decode(methodKey, response);

    return handleNotFound(decoded);
  }

  private Exception handleNotFound(Exception decoded) {
    if (decoded instanceof FeignException.NotFound notFound) {
      if (PEOPLE_LIST_PATTERN.matcher(notFound.request().url()).matches()) {
        return new NotFoundPeopleListException();
      }

      if (PEOPLE_ITEM_PATTERN.matcher(notFound.request().url()).matches()) {
        return new NotFoundPersonException();
      }
    }

    return decoded;
  }
}
