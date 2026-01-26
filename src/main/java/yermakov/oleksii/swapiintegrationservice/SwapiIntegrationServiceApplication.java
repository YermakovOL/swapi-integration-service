package yermakov.oleksii.swapiintegrationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableCaching
@EnableFeignClients
@ConfigurationPropertiesScan
public class SwapiIntegrationServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SwapiIntegrationServiceApplication.class, args);
  }
}
