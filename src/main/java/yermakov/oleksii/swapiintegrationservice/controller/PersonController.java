package yermakov.oleksii.swapiintegrationservice.controller;


import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yermakov.oleksii.swapiintegrationservice.dto.api.PersonDetailsDto;
import yermakov.oleksii.swapiintegrationservice.dto.api.ViewPersonsResponseDto;
import yermakov.oleksii.swapiintegrationservice.service.PersonService;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public ResponseEntity<ViewPersonsResponseDto> viewPersons(
            @RequestParam(defaultValue = "1") @Min(1) int page) {
        return ResponseEntity.ok((ViewPersonsResponseDto) personService.getPeople(page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDetailsDto> getPersonDetails(@PathVariable String id) {
        return ResponseEntity.ok(personService.getPersonById(id));
    }
}
