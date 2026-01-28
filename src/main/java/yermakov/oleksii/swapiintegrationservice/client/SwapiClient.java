package yermakov.oleksii.swapiintegrationservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import yermakov.oleksii.swapiintegrationservice.dto.swapi.PeopleInstance;
import yermakov.oleksii.swapiintegrationservice.dto.swapi.PeopleList;

@FeignClient(name = "swapiClient", configuration = SwapiClientConfig.class)
public interface SwapiClient {

  @GetMapping("api/people")
  PeopleList getPeople(@RequestParam("page") int page);

  @GetMapping("api/people/{id}")
  PeopleInstance getPeopleById(@PathVariable("id") String id);
}
