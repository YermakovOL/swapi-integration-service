package yermakov.oleksii.swapiintegrationservice.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yermakov.oleksii.swapiintegrationservice.service.AuthService;

@RestController
@RequestMapping(FavoritesController.URL)
@RequiredArgsConstructor
class FavoritesController {

  public static final String URL = "/favourites";
  private final AuthService authService;

  @GetMapping
  public List<String> getFavourites(
      @RequestHeader(name = "Authorization", required = false) String authorization) {
    return authService.getFavouriteCharacters(authorization);
  }
}
