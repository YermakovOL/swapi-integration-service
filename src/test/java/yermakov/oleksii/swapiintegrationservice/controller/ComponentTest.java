package yermakov.oleksii.swapiintegrationservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import yermakov.oleksii.swapiintegrationservice.config.WireMockConfig;

@SpringBootTest
@AutoConfigureMockMvc
@Import(WireMockConfig.class)
public abstract class ComponentTest {
  @Autowired MockMvc mockMvc;
  @Autowired WireMockServer wireMock;
  @Autowired CacheManager cacheManager;
  @Autowired ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
    cacheManager.getCacheNames().forEach(name -> cacheManager.getCache(name).clear());
    wireMock.resetRequests();
    wireMock.resetScenarios();
  }
}
