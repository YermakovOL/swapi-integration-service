package yermakov.oleksii.swapiintegrationservice.service.mapper;

import static java.util.Objects.nonNull;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import yermakov.oleksii.swapiintegrationservice.config.ApiFormatProperties;
import yermakov.oleksii.swapiintegrationservice.dto.api.PersonDetailsDto;
import yermakov.oleksii.swapiintegrationservice.dto.swapi.PeopleInstance;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
@Slf4j
public abstract class PeopleMapper {

  @Autowired protected ApiFormatProperties formatProperties;

  public abstract List<PersonDetailsDto> mapToResponse(List<PeopleInstance> peopleList);

  @Mapping(target = "height", source = "height", qualifiedByName = "formatHeight")
  @Mapping(target = "mass", source = "mass", qualifiedByName = "formatMass")
  @Mapping(target = "numberOfFilms", source = "films", qualifiedByName = "countFilms")
  @Mapping(target = "dateAdded", source = "created", qualifiedByName = "formatDate")
  public abstract PersonDetailsDto mapToResponse(PeopleInstance peopleInstance);

  @Named("formatHeight")
  protected String formatHeight(String input) {
    return nonNull(input) ? formatProperties.heightFormat().formatted(input) : null;
  }

  @Named("formatMass")
  protected String formatMass(String input) {
    return nonNull(input) ? formatProperties.massFormat().formatted(input) : null;
  }

  @Named("countFilms")
  protected int countFilms(List<String> input) {
    return nonNull(input) ? input.size() : 0;
  }

  @Named("formatDate")
  protected String formatDate(String dateTime) {
    if (dateTime == null) {
      return null;
    }
    try {
      ZonedDateTime zdt = ZonedDateTime.parse(dateTime);
      return zdt.format(DateTimeFormatter.ofPattern(formatProperties.dateFormat()));
    } catch (Exception e) {
      log.error("Incorrect data format", e);
      return null;
    }
  }
}
