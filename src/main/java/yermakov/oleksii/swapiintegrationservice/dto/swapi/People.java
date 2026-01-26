package yermakov.oleksii.swapiintegrationservice.dto.swapi;

public record People(
    String name,
    String height,
    String mass,
    String birth_year,
    String number_of_films,
    String created) {}
