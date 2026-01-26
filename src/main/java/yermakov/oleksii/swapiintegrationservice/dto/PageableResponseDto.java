package yermakov.oleksii.swapiintegrationservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PageableResponseDto<Content>(
    Integer page, Integer count, String next, String previous, List<Content> results) {}
