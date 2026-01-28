package yermakov.oleksii.swapiintegrationservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageableResponseDto<C>(Integer page, Integer count, List<C> results) {}
