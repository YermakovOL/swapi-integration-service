package yermakov.oleksii.swapiintegrationservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yermakov.oleksii.swapiintegrationservice.ex.UnauthorizedException;
import yermakov.oleksii.swapiintegrationservice.service.AuthService;

import java.util.List;

@RestController
@RequestMapping("/favourites")
@RequiredArgsConstructor
class FavouritesController {

    private final AuthService authService;

    @GetMapping
    public List<String> getFavourites(@RequestHeader("Authorization") String authorization) {
        if (!authService.isAccessTokenValid(authorization)) {
            boolean refreshed = authService.tryRefreshToken();
            if (!refreshed) {
                throw new UnauthorizedException();
            }
        }
        return authService.getFavouriteCharacters();
    }
}
