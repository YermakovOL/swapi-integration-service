package yermakov.oleksii.swapiintegrationservice.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static yermakov.oleksii.swapiintegrationservice.WireMockConfig.anyRequest;
import static yermakov.oleksii.swapiintegrationservice.WireMockConfig.peoplePage1;
import static yermakov.oleksii.swapiintegrationservice.WireMockConfig.peoplePage2;
import static yermakov.oleksii.swapiintegrationservice.WireMockConfig.peoplePage3;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class PersonControllerComponentTest extends ComponentTest {

  @Autowired private WireMockServer wireMockServer;

  @Test
  @SneakyThrows
  public void getPeopleList200() {
    mockMvc
        .perform(get(PeopleController.URL).param("page", "1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.count", is(82)))
        .andExpect(jsonPath("$.results", hasSize(2)))
        .andExpect(jsonPath("$.page", is(1)))
        .andExpect(jsonPath("$.next", is("next")))
        .andExpect(jsonPath("$.previous", nullValue()))
        .andExpect(jsonPath("$.results[0].name", is("Luke Skywalker")))
        .andExpect(jsonPath("$.results[0].height", is("172 meters")))
        .andExpect(jsonPath("$.results[0].mass", is("4 kg")))
        .andExpect(jsonPath("$.results[0].birth_year", is(4)))
        .andExpect(jsonPath("$.results[0].number_of_films", is(4)))
        .andExpect(jsonPath("$.results[0].date_added", is("12-09-2014")));

    wireMockServer.verify(1, peoplePage1());
  }

  @Test
  @SneakyThrows
  public void getPeopleListWithSeveralPages200() {
    mockMvc
        .perform(get(PeopleController.URL).param("page", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.results[0].name", is("Luke Skywalker")));

    mockMvc
        .perform(get(PeopleController.URL).param("page", "2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.results[0].name", is("R2-D2")));

    mockMvc
        .perform(get(PeopleController.URL).param("page", "3"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.results[0].name", is("Leia Organa")));

    wireMockServer.verify(1, peoplePage1());
    wireMockServer.verify(1, peoplePage2());
    wireMockServer.verify(1, peoplePage3());
  }

  @Test
  @SneakyThrows
  public void getPeopleListInvalidPaginationInput() {
    mockMvc
            .perform(get(PeopleController.URL).param("page", "0"))
            .andDo(print())
            .andExpect(status().isBadRequest());

    wireMockServer.verify(0, anyRequest());
  }
}
