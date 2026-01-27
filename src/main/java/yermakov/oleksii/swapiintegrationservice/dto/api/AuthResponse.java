package yermakov.oleksii.swapiintegrationservice.dto.api;


public record AuthResponse(String accessToken, String refreshToken, String user) {}
