package yermakov.oleksii.swapiintegrationservice.config;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;

@TestConfiguration
public class WireMockConfig {

  public static RequestPatternBuilder peopleListRequest() {
    return getRequestedFor(urlPathEqualTo("/api/people"));
  }

  public static RequestPatternBuilder anyRequest() {
    return anyRequestedFor(anyUrl());
  }

  @Bean(initMethod = "start", destroyMethod = "stop")
  public WireMockServer wireMockServer() {
    return new WireMockServer(
        WireMockConfiguration.options().dynamicPort().notifier(new ConsoleNotifier(true)));
  }

  @Bean
  public DynamicPropertyRegistrar wireMockPropertyRegistrar(WireMockServer server) {
    return registry ->
        registry.add("spring.cloud.openfeign.client.config.swapiClient.url", server::baseUrl);
  }
}
