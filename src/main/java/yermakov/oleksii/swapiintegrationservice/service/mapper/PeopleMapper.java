package yermakov.oleksii.swapiintegrationservice.service.mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import yermakov.oleksii.swapiintegrationservice.config.ApiFormatProperties;
import yermakov.oleksii.swapiintegrationservice.dto.api.PersonDetailsDto;
import yermakov.oleksii.swapiintegrationservice.dto.swapi.People;

@Mapper(
    componentModel = SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR)
public abstract class PeopleMapper {

  @Autowired
  protected ApiFormatProperties formatProperties;

  public abstract List<PersonDetailsDto> mapToResponse(List<People> peopleList);

  @Mapping(target = "height", source = "height", qualifiedByName = "formatHeight")
  @Mapping(target = "mass", source = "mass", qualifiedByName = "formatMass")
  @Mapping(target = "date_added", source = "created", qualifiedByName = "formatDate")
  public abstract PersonDetailsDto mapToResponse(People people);

  @Named("formatHeight")
  protected String formatHeight(String input) {
    return formatProperties.heightFormat().formatted(input);
  }

  @Named("formatMass")
  protected String formatMass(String input) {
    return formatProperties.massFormat().formatted(input);
  }

  @Named("formatDate")
  protected String formatDate(String dateTime) {
    ZonedDateTime zdt = ZonedDateTime.parse(dateTime);
    return zdt.format(DateTimeFormatter.ofPattern(formatProperties.dateFormat()));
  }
}
