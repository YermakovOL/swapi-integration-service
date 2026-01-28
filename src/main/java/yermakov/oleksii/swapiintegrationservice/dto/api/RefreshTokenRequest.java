package yermakov.oleksii.swapiintegrationservice.dto.api;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank String refreshToken) {}
