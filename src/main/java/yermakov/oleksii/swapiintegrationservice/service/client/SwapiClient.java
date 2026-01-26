package yermakov.oleksii.swapiintegrationservice.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import yermakov.oleksii.swapiintegrationservice.dto.api.PersonDetailsDto;
import yermakov.oleksii.swapiintegrationservice.dto.swapi.People;
import yermakov.oleksii.swapiintegrationservice.dto.swapi.PeopleList;
import yermakov.oleksii.swapiintegrationservice.dto.swapi.PersonDto;

import java.util.List;

@FeignClient(name = "swapiClient", configuration = SwapiClientConfig.class)
public interface SwapiClient {

    @GetMapping("api/people")
//    @Cacheable
    PeopleList getPeople(@RequestParam("page") int page);

    @GetMapping("api/people/{id}")
//    @Cacheable
    People getPersonById(@PathVariable("id") String id);
}
