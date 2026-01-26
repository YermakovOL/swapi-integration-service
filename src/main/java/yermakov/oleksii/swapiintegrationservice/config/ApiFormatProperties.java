package yermakov.oleksii.swapiintegrationservice.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import yermakov.oleksii.swapiintegrationservice.constraint.ValidDateTimeFormat;
import yermakov.oleksii.swapiintegrationservice.constraint.StringFormat;

@ConfigurationProperties("api.format.people-api")
@Validated
public record ApiFormatProperties(
    @NotBlank @ValidDateTimeFormat String dateFormat,
    @NotBlank @StringFormat String massFormat,
    @NotBlank @StringFormat String heightFormat) {}
