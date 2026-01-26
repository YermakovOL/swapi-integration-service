package yermakov.oleksii.swapiintegrationservice.dto.api;

import lombok.Data;

@Data
public class PersonDetailsDto {

  private String name;

  private String height;

  private String mass;

  private String birth_year;

  private String number_of_films;

  private String date_added;
}
