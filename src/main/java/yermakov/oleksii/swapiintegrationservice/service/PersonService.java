package yermakov.oleksii.swapiintegrationservice.service;

import yermakov.oleksii.swapiintegrationservice.dto.api.PersonDetailsDto;
import yermakov.oleksii.swapiintegrationservice.dto.swapi.PersonDto;

import java.util.List;

public interface PersonService {
    List<PersonDto> getPeople(int page);

    PersonDetailsDto getPersonById(String personId);
}
