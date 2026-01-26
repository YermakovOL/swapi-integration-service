package yermakov.oleksii.swapiintegrationservice;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistrar;

@TestConfiguration
public class WireMockConfig {

  public static RequestPatternBuilder peoplePage1() {
    return getRequestedFor(urlMatching("/api/people")).withQueryParam("page", equalTo("1"));
  }

  public static RequestPatternBuilder peoplePage2() {
    return getRequestedFor(urlMatching("/api/people")).withQueryParam("page", equalTo("2"));
  }

  public static RequestPatternBuilder peoplePage3() {
    return getRequestedFor(urlMatching("/api/people")).withQueryParam("page", equalTo("3"));
  }

  public static RequestPatternBuilder anyRequest() {
    return anyRequestedFor(anyUrl());
  }

  @Bean(initMethod = "start", destroyMethod = "stop")
  public WireMockServer wireMockServer() {
    return new WireMockServer(WireMockConfiguration.options().dynamicPort());
  }

  @Bean
  public DynamicPropertyRegistrar wireMockPropertyRegistrar(WireMockServer server) {
    return registry -> {
      registry.add("spring.cloud.openfeign.client.config.swapiClient.url", server::baseUrl);
    };
  }
}
