package yermakov.oleksii.swapiintegrationservice.dto.swapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record PeopleInstance(
    String name,
    String height,
    String mass,
    @JsonProperty("birth_year") String birthYear,
    List<String> films,
    String created) {}
