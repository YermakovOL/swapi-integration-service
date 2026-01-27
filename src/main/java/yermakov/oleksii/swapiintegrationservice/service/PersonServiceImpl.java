package yermakov.oleksii.swapiintegrationservice.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import yermakov.oleksii.swapiintegrationservice.dto.PageableResponseDto;
import yermakov.oleksii.swapiintegrationservice.dto.api.PersonDetailsDto;
import yermakov.oleksii.swapiintegrationservice.dto.swapi.PeopleInstance;
import yermakov.oleksii.swapiintegrationservice.dto.swapi.PeopleList;
import yermakov.oleksii.swapiintegrationservice.client.SwapiClient;
import yermakov.oleksii.swapiintegrationservice.service.mapper.PeopleMapper;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PeopleService {

  private final SwapiClient swapiClient;
  private final PeopleMapper peopleMapper;

  @Override
  @Cacheable(cacheNames = "people-api", key = "#page")
  public PageableResponseDto<PersonDetailsDto> getPeople(int page) {
    PeopleList peopleList = swapiClient.getPeople(page);

    List<PersonDetailsDto> content = peopleMapper.mapToResponse(peopleList.results());

    return new PageableResponseDto<>(page, peopleList.count(), content);
  }

  @Override
  @Cacheable(cacheNames = "people-api", key = "'person:' + #personId")
  public PersonDetailsDto getPersonById(String personId) {
    PeopleInstance personById = swapiClient.getPeopleById(personId);

    return peopleMapper.mapToResponse(personById);
  }
}
