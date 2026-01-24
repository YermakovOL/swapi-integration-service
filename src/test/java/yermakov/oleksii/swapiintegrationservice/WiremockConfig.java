package yermakov.oleksii.swapiintegrationservice;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

@TestConfiguration
@EnableWireMock(@ConfigureWireMock(registerSpringBean = true))
public class WiremockConfig {

  @Autowired private WireMockServer wireMockServer;

  @Bean
  public DynamicPropertyRegistrar registerProperties() {
    return registry -> {
      registry.add("feign.client.config.swapiClient.url", () -> "http://localhost:" + wireMockServer.port());
    };
  }
}
