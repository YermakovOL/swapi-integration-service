package yermakov.oleksii.swapiintegrationservice.dto.swapi;

import java.util.List;

public record PeopleList(int count, String next, String previous, List<PeopleInstance> results) {}
