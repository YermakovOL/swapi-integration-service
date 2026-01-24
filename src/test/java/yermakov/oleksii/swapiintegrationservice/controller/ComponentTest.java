package yermakov.oleksii.swapiintegrationservice.controller;


import org.springframework.boot.test.context.SpringBootTest;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

@SpringBootTest
@EnableWireMock(@ConfigureWireMock(filesUnderClasspath = "wiremock"))
public class ComponentTest {
    
}
