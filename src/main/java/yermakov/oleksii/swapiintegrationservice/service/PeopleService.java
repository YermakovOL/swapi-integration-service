package yermakov.oleksii.swapiintegrationservice.service;

import yermakov.oleksii.swapiintegrationservice.dto.PageableResponseDto;
import yermakov.oleksii.swapiintegrationservice.dto.api.PersonDetailsDto;

public interface PeopleService {
  PageableResponseDto<PersonDetailsDto> getPeople(int page);

  PersonDetailsDto getPersonById(String personId);
}
