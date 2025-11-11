package it.lorenzopaciello.awesomepizza.integration.controller.auth.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.lorenzopaciello.awesomepizza.model.RefreshToken;
import it.lorenzopaciello.awesomepizza.model.User;
import it.lorenzopaciello.awesomepizza.repository.RefreshTokenRepository;
import it.lorenzopaciello.awesomepizza.repository.UserRepository;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Component
public class LogoutUseCase {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public ResultActions loginRequest(String username, String password, String language, MockMvc mockMvc) throws Exception {

        return mockMvc.perform(post("/api/auth/login")
                        .header("accept-language", language == null ? "it" : language)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "username": "%s",
                            "password": "%s"
                        }
                    """.formatted(username, password)));
    }

    public void loginSuccessAssertions(String username, ResultActions resultActions, int numRefreshToken) throws Exception {

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(cookie().exists("refreshToken"))
                .andExpect(cookie().httpOnly("refreshToken", true))
                .andExpect(cookie().maxAge("refreshToken", Matchers.greaterThan(0)));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new AssertionError("Utente non trovato nel DB"));
        assertTrue(user.isEnabled());

        List<RefreshToken> tokensByUser = refreshTokenRepository.findByUser(user);
        assertEquals(numRefreshToken, tokensByUser.size());

        List<RefreshToken> tokensByUserActive = refreshTokenRepository.findByUserAndRevokedFalse(user);
        assertEquals(1, tokensByUserActive.size());
    }


    public String loginSuccessGetToken(MvcResult mvcResult) throws JsonProcessingException, UnsupportedEncodingException {
        String responseBody = mvcResult.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(responseBody);
        String accessToken = json.get("accessToken").asText();

        assertNotNull(accessToken, "Access token non deve essere nullo");

        return accessToken;
    }

    public void loginUnauthorizedAssertions(String username, int numRefreshToken) throws Exception {

        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new AssertionError("Utente non trovato nel DB"));
        assertFalse(user.isEnabled());

        List<RefreshToken> tokens = refreshTokenRepository.findByUser(user);
        assertEquals(numRefreshToken, tokens.size());

    }
}
