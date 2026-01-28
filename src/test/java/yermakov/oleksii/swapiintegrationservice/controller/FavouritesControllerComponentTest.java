package yermakov.oleksii.swapiintegrationservice.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import yermakov.oleksii.swapiintegrationservice.dto.api.AuthResponse;
import yermakov.oleksii.swapiintegrationservice.dto.api.LoginRequest;

class FavouritesControllerComponentTest extends ComponentTest {

  @Test
  void shouldReturnFavourites_WhenRealFlowIsExecuted() throws Exception {
    LoginRequest loginRequest = new LoginRequest("luke_skywalker", "password123");

    MvcResult loginResult =
        mockMvc
            .perform(
                post(AuthController.URL + "/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn();

    String jsonResponse = loginResult.getResponse().getContentAsString();
    AuthResponse authResponse = objectMapper.readValue(jsonResponse, AuthResponse.class);
    String accessToken = authResponse.accessToken();

    mockMvc
        .perform(get(FavouritesController.URL).header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.size()", is(3)))
        .andExpect(jsonPath("$", hasItems("Luke Skywalker", "R2-D2", "Leia Organa")));
  }

  @Test
  void shouldReturn401_WhenTokenIsFake() throws Exception {
    String fakeToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIn0.SimulatedSignature";

    mockMvc
        .perform(get(FavouritesController.URL).header("Authorization", "Bearer " + fakeToken))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.errorMessage", containsString("Invalid access token")));
  }

  @Test
  void shouldReturn401_WhenNoHeader() throws Exception {
    mockMvc.perform(get(FavouritesController.URL)).andExpect(status().isUnauthorized());
  }
}
