package yermakov.oleksii.swapiintegrationservice.client;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwapiClientConfig {

  @Bean
  ErrorDecoder errorDecoder() {
    return new SwapiErrorDecoder();
  }
}
