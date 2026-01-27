package yermakov.oleksii.swapiintegrationservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import yermakov.oleksii.swapiintegrationservice.dto.api.AuthResponse;
import yermakov.oleksii.swapiintegrationservice.dto.api.LoginRequest;
import yermakov.oleksii.swapiintegrationservice.ex.UnauthorizedException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Requires dependencies in pom.xml:
 * - io.jsonwebtoken:jjwt-api:0.11.5
 * - io.jsonwebtoken:jjwt-impl:0.11.5
 * - io.jsonwebtoken:jjwt-jackson:0.11.5
 */
@Slf4j
@Service
class AuthServiceImpl implements AuthService {

  // In a real app, load this from application.properties
  private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private static final long ACCESS_EXPIRATION_MS = 15 * 60 * 1000; // 15 minutes
  private static final long REFRESH_EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000; // 7 days

  // In-Memory storage for Refresh Tokens (Map<RefreshToken, Username>)
  // This makes the service thread-safe and allows multiple users
  private final Map<String, String> refreshStorage = new ConcurrentHashMap<>();

  @Override
  public AuthResponse login(LoginRequest request) {
    log.info("Attempting login for user: {}", request.username());

    String accessToken = createToken(request.username(), ACCESS_EXPIRATION_MS);
    String refreshToken = createToken(request.username(), REFRESH_EXPIRATION_MS);

    // Store refresh token to allow validation and logout later
    refreshStorage.put(refreshToken, request.username());

    log.info("Login successful. Tokens generated for user: {}", request.username());
    return new AuthResponse(accessToken, refreshToken, request.username());
  }

  @Override
  public Map<String, String> refreshToken(String refreshToken) {
    // Note: I updated the signature to accept the token as input.
    // A stateless service cannot know "this.refreshToken".

    log.debug("Attempting to refresh access token");

    if (refreshToken == null || !refreshStorage.containsKey(refreshToken)) {
      log.warn("Refresh token invalid or not found in storage");
      throw new UnauthorizedException("Invalid refresh token");
    }

    try {
      // Validate expiration and signature
      String username = getUsernameFromToken(refreshToken);

      // Generate new Access Token
      String newAccessToken = createToken(username, ACCESS_EXPIRATION_MS);

      log.info("Access token refreshed successfully for user: {}", username);
      return Map.of("accessToken", newAccessToken);

    } catch (JwtException e) {
      log.error("Refresh token signature validation failed", e);
      throw new UnauthorizedException("Invalid refresh token signature");
    }
  }

  @Override
  public void logout(String refreshToken) {
    // Remove from storage effectively invalidates the session
    if (refreshToken != null) {
      String removedUser = refreshStorage.remove(refreshToken);
      if (removedUser != null) {
        log.info("User {} logged out successfully", removedUser);
      } else {
        log.warn("Logout attempted with unknown refresh token");
      }
    }
  }

  @Override
  public boolean isAccessTokenValid(String header) {
    if (header == null || !header.startsWith("Bearer ")) {
      log.trace("Authorization header missing or invalid format");
      return false;
    }

    String token = header.substring(7);
    try {
      Jwts.parserBuilder()
              .setSigningKey(SECRET_KEY)
              .build()
              .parseClaimsJws(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      log.warn("Token validation failed: {}", e.getMessage());
      return false;
    }
  }

  // This method simulates a protected resource
  @Override
  public List<String> getFavouriteCharacters(String authorization) {
    if (!isAccessTokenValid(authorization)) {
      log.warn("Unauthorized access attempt to getFavouriteCharacters");
      throw new UnauthorizedException("Invalid or expired Access Token");
    }

    log.debug("Returning favourite characters for authorized request");
    return List.of("Luke Skywalker", "R2-D2", "Leia Organa");
  }

  private String createToken(String username, long expirationMs) {
    return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
            .signWith(SECRET_KEY)
            .compact();
  }

  private String getUsernameFromToken(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(SECRET_KEY)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
  }

  // Overloaded for backward compatibility with your interface if you can't change it,
  // but this method is technically broken in a Singleton service.
  @Override
  public Map<String, String> refreshToken() {
    throw new UnsupportedOperationException("Use refreshToken(String token) instead");
  }

  @Override
  public void logout() {
    throw new UnsupportedOperationException("Use logout(String token) instead");
  }
}