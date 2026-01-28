package yermakov.oleksii.swapiintegrationservice.service;

import java.util.List;
import java.util.Map;
import yermakov.oleksii.swapiintegrationservice.dto.api.AuthResponse;
import yermakov.oleksii.swapiintegrationservice.dto.api.LoginRequest;

public interface AuthService {

  AuthResponse login(LoginRequest request);

  Map<String, String> refreshToken(String refreshToken);

  void logout(String refreshToken);

  boolean isAccessTokenValid(String header);

  List<String> getFavouriteCharacters(String authorization);
}
