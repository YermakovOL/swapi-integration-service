package yermakov.oleksii.swapiintegrationservice.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import yermakov.oleksii.swapiintegrationservice.WireMockConfig;

@SpringBootTest
@AutoConfigureMockMvc
@Import(WireMockConfig.class)
public class ComponentTest {
  @Autowired MockMvc mockMvc;
  @Autowired WireMockServer wireMock;

  @BeforeEach
  public void setUp(){
    wireMock.resetRequests();
  }
}
