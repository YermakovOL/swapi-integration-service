package yermakov.oleksii.swapiintegrationservice.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yermakov.oleksii.swapiintegrationservice.dto.PageableResponseDto;
import yermakov.oleksii.swapiintegrationservice.dto.api.PersonDetailsDto;
import yermakov.oleksii.swapiintegrationservice.service.PeopleService;

@RestController
@RequestMapping(PeopleController.URL)
@RequiredArgsConstructor
public class PeopleController {
  public static final String URL = "/people";
  private final PeopleService peopleService;

  @GetMapping
  public PageableResponseDto<PersonDetailsDto> viewPeople(
          @RequestParam(defaultValue = "1") @Min(1) int page) {
    return peopleService.getPeople(page);
  }

  @GetMapping("/{id}")
  public PersonDetailsDto getPeopleById(@PathVariable String id) {
    return peopleService.getPersonById(id);
  }
}
