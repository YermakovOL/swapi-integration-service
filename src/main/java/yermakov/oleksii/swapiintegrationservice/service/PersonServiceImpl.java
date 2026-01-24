package yermakov.oleksii.swapiintegrationservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yermakov.oleksii.swapiintegrationservice.dto.api.PersonDetailsDto;
import yermakov.oleksii.swapiintegrationservice.dto.swapi.PersonDto;
import yermakov.oleksii.swapiintegrationservice.service.client.SwapiClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final SwapiClient swapiClient;

    @Override
    public List<PersonDto> getPeople(int page) {
        return swapiClient.getPeople(page);
    }

    @Override
    public PersonDetailsDto getPersonById(String personId) {
        return swapiClient.getPersonById(personId);
    }
}
