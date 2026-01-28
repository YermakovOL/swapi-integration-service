package yermakov.oleksii.swapiintegrationservice.controller;

import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import yermakov.oleksii.swapiintegrationservice.dto.api.AuthResponse;
import yermakov.oleksii.swapiintegrationservice.dto.api.LoginRequest;
import yermakov.oleksii.swapiintegrationservice.dto.api.RefreshTokenRequest;
import yermakov.oleksii.swapiintegrationservice.service.AuthService;

@RestController
@RequestMapping(AuthController.URL)
@RequiredArgsConstructor
public class AuthController {

  public static final String URL = "/auth";

  private final AuthService authService;

  @PostMapping("/login")
  public AuthResponse login(@RequestBody @Valid LoginRequest request) {
    return authService.login(request);
  }

  @PostMapping("/refresh")
  public Map<String, String> refresh(@RequestBody @Valid RefreshTokenRequest request) {
    return authService.refreshToken(request.refreshToken());
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void logout(@RequestBody @Valid RefreshTokenRequest request) {
    authService.logout(request.refreshToken());
  }
}
