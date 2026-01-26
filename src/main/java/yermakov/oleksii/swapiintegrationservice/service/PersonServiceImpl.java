package yermakov.oleksii.swapiintegrationservice.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yermakov.oleksii.swapiintegrationservice.dto.PageableResponseDto;
import yermakov.oleksii.swapiintegrationservice.dto.api.PersonDetailsDto;
import yermakov.oleksii.swapiintegrationservice.dto.swapi.People;
import yermakov.oleksii.swapiintegrationservice.dto.swapi.PeopleList;
import yermakov.oleksii.swapiintegrationservice.service.client.SwapiClient;
import yermakov.oleksii.swapiintegrationservice.service.mapper.PeopleMapper;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PeopleService {

  private final SwapiClient swapiClient;
  private final PeopleMapper peopleMapper;

  @Override
  public PageableResponseDto<PersonDetailsDto> getPeople(int page) {
    PeopleList peopleList = swapiClient.getPeople(page);

    List<PersonDetailsDto> content = peopleMapper.mapToResponse(peopleList.results());

    return new PageableResponseDto<>(
        page, peopleList.count(), peopleList.next(), peopleList.previous(), content);
  }

  @Override
  public PersonDetailsDto getPersonById(String personId) {
    People personById = swapiClient.getPersonById(personId);

    return peopleMapper.mapToResponse(personById);
  }
}
