package yermakov.oleksii.swapiintegrationservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("api.auth")
public record AuthConfigurationProperties(Long accessMs, Long refreshMs) {}
