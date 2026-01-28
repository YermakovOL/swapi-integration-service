package yermakov.oleksii.swapiintegrationservice.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PersonDetailsDto {

  private String name;

  private String height;

  private String mass;

  @JsonProperty("birth_year")
  private String birthYear;

  @JsonProperty("number_of_films")
  private Integer numberOfFilms;

  @JsonProperty("date_added")
  private String dateAdded;
}
