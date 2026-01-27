package yermakov.oleksii.swapiintegrationservice.controller;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static yermakov.oleksii.swapiintegrationservice.WireMockConfig.anyRequest;
import static yermakov.oleksii.swapiintegrationservice.WireMockConfig.peopleListRequest;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

class PersonControllerComponentTest extends ComponentTest {

  @Autowired private WireMockServer wireMockServer;

  @Test
  @SneakyThrows
  void getPeopleList200() {
    mockMvc
        .perform(get(PeopleController.URL).param("page", "1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.count", is(82)))
        .andExpect(jsonPath("$.results", hasSize(2)))
        .andExpect(jsonPath("$.page", is(1)))
        .andExpect(jsonPath("$.results[0].name", is("Luke Skywalker")))
        .andExpect(jsonPath("$.results[0].height", is("172 meters")))
        .andExpect(jsonPath("$.results[0].mass", is("77 kg")))
        .andExpect(jsonPath("$.results[0].birth_year", is("19BBY")))
        .andExpect(jsonPath("$.results[0].date_added", is("09-12-2014")));

    wireMockServer.verify(1, peopleListRequest());
  }

  @Test
  @SneakyThrows
  void getPeopleListWithSeveralPages200() {
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

    wireMockServer.verify(3, peopleListRequest());
  }

  @Test
  @SneakyThrows
  void getPeopleListWithSameCachedPage200() {
    mockMvc
        .perform(get(PeopleController.URL).param("page", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.results[0].name", is("Luke Skywalker")));
    mockMvc
        .perform(get(PeopleController.URL).param("page", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.results[0].name", is("Luke Skywalker")));
    ;
    mockMvc
        .perform(get(PeopleController.URL).param("page", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.results[0].name", is("Luke Skywalker")));

    wireMockServer.verify(1, peopleListRequest());
  }

  @Test
  @SneakyThrows
  void getPeopleListInvalidPaginationInput() {
    mockMvc
        .perform(get(PeopleController.URL).param("page", "0"))
        .andDo(print())
        .andExpect(status().isBadRequest());

    wireMockServer.verify(0, anyRequest());
  }

  @Test
  @SneakyThrows
  void getPeopleList400BadRequest() {
    mockMvc
        .perform(get(PeopleController.URL).param("page", "-1"))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.[0].invalidParameter", is("page")))
        .andExpect(jsonPath("$.[0].errorMessage", not(emptyString())));

    wireMockServer.verify(0, anyRequest());
  }

  @Test
  @SneakyThrows
  void getPeopleListWhenIntegrationError() {
    wireMockServer.setScenarioState("PeopleListApi", "ERROR");

    mockMvc
        .perform(get(PeopleController.URL))
        .andDo(print())
        .andExpect(status().is5xxServerError())
        .andExpect(jsonPath("$.errorMessage", containsString("Service temporary unavailable")));

    wireMockServer.verify(1, getRequestedFor(urlPathEqualTo("/api/people")));
  }

  @Test
  @SneakyThrows
  void getPeopleList404NotFound() {
    wireMockServer.setScenarioState("PeopleListApi", "NOT_FOUND");

    mockMvc
        .perform(get(PeopleController.URL))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.errorMessage", containsString("not found")));

    wireMockServer.verify(1, getRequestedFor(urlPathEqualTo("/api/people")));
  }

  @Test
  @SneakyThrows
   void getPersonById200() {
    mockMvc
            .perform(get(PeopleController.URL + "/1"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.name", is("Luke Skywalker")))
            .andExpect(jsonPath("$.height", is("172 meters"))) // Assuming your mapper adds units
            .andExpect(jsonPath("$.mass", is("77 kg")))
            .andExpect(jsonPath("$.birth_year", is("19BBY")))
            .andExpect(jsonPath("$.number_of_films", is(4)))
            .andExpect(jsonPath("$.date_added", is("09-12-2014")));

    wireMockServer.verify(1, getRequestedFor(urlPathEqualTo("/api/people/1")));
  }

  @Test
  @SneakyThrows
   void getPersonById404NotFound() {
    wireMockServer.setScenarioState("PeopleInstanceApi", "NOT_FOUND");

    mockMvc
            .perform(get(PeopleController.URL + "/999"))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.errorMessage", containsString("not found")));

    wireMockServer.verify(1, getRequestedFor(urlPathEqualTo("/api/people/999")));
  }

  @Test
  @SneakyThrows
   void getPersonByIdWhenIntegrationError() {
    wireMockServer.setScenarioState("PeopleInstanceApi", "ERROR");

    mockMvc
            .perform(get(PeopleController.URL + "/50"))
            .andDo(print())
            .andExpect(status().is5xxServerError())
            .andExpect(jsonPath("$.errorMessage", is("Service temporary unavailable")));

    wireMockServer.verify(1, getRequestedFor(urlPathEqualTo("/api/people/50")));
  }
}
