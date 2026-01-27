package yermakov.oleksii.swapiintegrationservice.dto.swapi;

import java.util.List;

public record PeopleInstance(
        String name,
        String height,
        String mass,
        String birth_year,
        List<String> films,
        String created) {}
