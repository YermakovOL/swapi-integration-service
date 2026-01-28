package yermakov.oleksii.swapiintegrationservice.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import yermakov.oleksii.swapiintegrationservice.dto.api.AuthResponse;
import yermakov.oleksii.swapiintegrationservice.dto.api.LoginRequest;
import yermakov.oleksii.swapiintegrationservice.dto.api.RefreshTokenRequest;

class AuthControllerComponentTest extends ComponentTest {
  @Test
  void login_ShouldReturnTokens_WhenRequestIsValid() throws Exception {

    LoginRequest loginRequest = new LoginRequest("han_solo", "password123");

    mockMvc
        .perform(
            post(AuthController.URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken", not(emptyString())))
        .andExpect(jsonPath("$.refreshToken", not(emptyString())))
        .andExpect(jsonPath("$.user", is("han_solo")));
  }

  @Test
  void refresh_ShouldReturnNewAccessToken_WhenRefreshTokenIsValid() throws Exception {
    String validRefreshToken = loginAndGetRefreshToken("leia_organa");

    RefreshTokenRequest refreshRequest = new RefreshTokenRequest(validRefreshToken);

    mockMvc
        .perform(
            post(AuthController.URL + "/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken", not(emptyString())));
  }

  @Test
  void refresh_ShouldFail_WhenRefreshTokenIsInvalid() throws Exception {
    RefreshTokenRequest invalidRequest = new RefreshTokenRequest("invalid-uuid-token");

    mockMvc
        .perform(
            post(AuthController.URL + "/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.errorMessage", containsString("Invalid refresh token")));
  }

  @Test
  void logout_ShouldRemoveToken_AndPreventFurtherRefreshes() throws Exception {
    String refreshToken = loginAndGetRefreshToken("chewbacca");

    RefreshTokenRequest logoutRequest = new RefreshTokenRequest(refreshToken);

    mockMvc
        .perform(
            post(AuthController.URL + "/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(logoutRequest)))
        .andExpect(status().isNoContent());

    mockMvc
        .perform(
            post(AuthController.URL + "/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(logoutRequest)))
        .andExpect(status().isUnauthorized());
  }

  private String loginAndGetRefreshToken(String username) throws Exception {
    LoginRequest loginRequest = new LoginRequest(username, "pass");

    MvcResult result =
        mockMvc
            .perform(
                post(AuthController.URL + "/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn();

    String json = result.getResponse().getContentAsString();
    return objectMapper.readValue(json, AuthResponse.class).refreshToken();
  }

  @ParameterizedTest
  @EmptySource
  @NullSource
  void login_ShouldReturn400_WhenUsernameIsBlank(String input) throws Exception {

    LoginRequest invalidRequest = new LoginRequest(input, input);

    mockMvc
        .perform(
            post(AuthController.URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.size()", is(2)))
        .andExpect(jsonPath("$[*].invalidParameter", hasItem("username")))
        .andExpect(jsonPath("$[*].errorMessage", hasItem("must not be blank")));
  }

  @ParameterizedTest
  @EmptySource
  @NullSource
  void refresh_ShouldReturn400_WhenTokenIsBlank(String input) throws Exception {

    RefreshTokenRequest invalidRequest = new RefreshTokenRequest(input);

    mockMvc
        .perform(
            post(AuthController.URL + "/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].invalidParameter", is("refreshToken")))
        .andExpect(jsonPath("$[0].errorMessage", is("must not be blank")));
  }

  @ParameterizedTest
  @EmptySource
  @NullSource
  void logout_ShouldReturn400_WhenTokenIsBlank(String input) throws Exception {

    RefreshTokenRequest invalidRequest = new RefreshTokenRequest(input);

    mockMvc
        .perform(
            post(AuthController.URL + "/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$[0].invalidParameter", is("refreshToken")))
        .andExpect(jsonPath("$[0].errorMessage", is("must not be blank")));
  }
}
