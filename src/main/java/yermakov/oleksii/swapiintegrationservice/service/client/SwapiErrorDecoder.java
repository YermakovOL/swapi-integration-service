package yermakov.oleksii.swapiintegrationservice.service.client;

import feign.Response;
import feign.codec.ErrorDecoder;

public class SwapiErrorDecoder implements ErrorDecoder {

  private final ErrorDecoder defaultErrorDecoder;

  public SwapiErrorDecoder() {
    defaultErrorDecoder = new Default();
  }

  @Override
  public Exception decode(String s, Response response) {
    return defaultErrorDecoder.decode(s, response);
  }
}
