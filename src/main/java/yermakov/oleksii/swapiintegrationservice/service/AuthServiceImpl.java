package yermakov.oleksii.swapiintegrationservice.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.springframework.stereotype.Service;
import yermakov.oleksii.swapiintegrationservice.config.AuthConfigurationProperties;
import yermakov.oleksii.swapiintegrationservice.dto.api.AuthResponse;
import yermakov.oleksii.swapiintegrationservice.dto.api.LoginRequest;
import yermakov.oleksii.swapiintegrationservice.ex.UnauthorizedException;

@Slf4j
@Service
class AuthServiceImpl implements AuthService {

  private static final String BEARER_PREFIX = "Bearer ";
  public static final List<String> STATIC_LIST = List.of("Luke Skywalker", "R2-D2", "Leia Organa");

  private final Cache<String, String> accessTokenCache;

  private final Cache<String, RefreshTokenData> refreshTokenCache;

  public AuthServiceImpl(AuthConfigurationProperties authProps) {
    this.accessTokenCache =
        Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMillis(authProps.accessMs()))
            .maximumSize(10_000)
            .build();

    this.refreshTokenCache =
        Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMillis(authProps.refreshMs()))
            .maximumSize(10_000)
            .build();
  }

  @Override
  public AuthResponse login(LoginRequest request) {
    log.info("Attempting login for user: {}", request.username());

    String accessToken = UUID.randomUUID().toString();
    String refreshToken = UUID.randomUUID().toString();

    saveTokens(accessToken, refreshToken, request.username());

    log.info("Login successful");
    return new AuthResponse(accessToken, refreshToken, request.username());
  }

  @Override
  public Map<String, String> refreshToken(String refreshToken) {
    RefreshTokenData tokenData = refreshTokenCache.getIfPresent(refreshToken);

    if (tokenData == null) {
      log.warn("Refresh token invalid or expired");
      throw new UnauthorizedException("Invalid refresh token");
    }

    if (tokenData.associatedAccessToken() != null) {
      accessTokenCache.invalidate(tokenData.associatedAccessToken());
    }

    String newAccessToken = UUID.randomUUID().toString();

    refreshTokenCache.put(refreshToken, new RefreshTokenData(tokenData.username(), newAccessToken));

    accessTokenCache.put(newAccessToken, tokenData.username());

    log.info("Access token refreshed for user: {}", tokenData.username());
    return Map.of("accessToken", newAccessToken);
  }

  @Override
  public void logout(String refreshToken) {
    if (StringUtils.isNotBlank(refreshToken)) {
      RefreshTokenData tokenData = refreshTokenCache.getIfPresent(refreshToken);

      if (tokenData != null) {
        refreshTokenCache.invalidate(refreshToken);

        if (tokenData.associatedAccessToken() != null) {
          accessTokenCache.invalidate(tokenData.associatedAccessToken());
        }
        log.info("User {} logged out", tokenData.username());
      }
    }
  }

  @Override
  public boolean isAccessTokenValid(String header) {
    if (!Strings.CS.startsWith(header, BEARER_PREFIX)) return false;

    String token = Strings.CS.removeStart(header, BEARER_PREFIX);

    return accessTokenCache.getIfPresent(token) != null;
  }

  @Override
  public List<String> getFavouriteCharacters(String authorization) {
    if (!isAccessTokenValid(authorization)) {
      throw new UnauthorizedException("Invalid access token");
    }
    return STATIC_LIST;
  }

  private void saveTokens(String accessToken, String refreshToken, String username) {
    accessTokenCache.put(accessToken, username);
    refreshTokenCache.put(refreshToken, new RefreshTokenData(username, accessToken));
  }

  private record RefreshTokenData(String username, String associatedAccessToken) {}
}
