package yermakov.oleksii.swapiintegrationservice.service;

import yermakov.oleksii.swapiintegrationservice.dto.api.AuthResponse;
import yermakov.oleksii.swapiintegrationservice.dto.api.LoginRequest;

import java.util.List;
import java.util.Map;

public interface AuthService {

  AuthResponse login(LoginRequest request);

  Map<String, String> refreshToken();

  void logout();

  List<String> getFavouriteCharacters(String authorization);
}
