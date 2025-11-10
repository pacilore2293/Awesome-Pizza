package it.lorenzopaciello.awesomepizza.integration.controller.auth.shared;

import it.lorenzopaciello.awesomepizza.model.RefreshToken;
import it.lorenzopaciello.awesomepizza.model.User;
import it.lorenzopaciello.awesomepizza.repository.RefreshTokenRepository;
import it.lorenzopaciello.awesomepizza.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Component
public class RegisterUseCase {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public ResultActions RegisterRequest(String username, String password, String role, String accessToken, String language, MockMvc mockMvc) throws Exception {

        return mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .header("accept-language", language == null ? "it" : language)
                .header("Authorization", "Bearer " + accessToken)
                .content("""
                        {
                            "username": "%s",
                            "password": "%s",
                            "role": "%s"
                        }
                    """.formatted(username, password, role)));
    }

    public void registerSuccessAssertions(ResultActions resultActions, String username, String role) throws Exception {

        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.enabled").value(false))
                .andExpect(jsonPath("$.roles[0].name").value(role));

        User newUser = userRepository.findByUsername(username).orElseThrow(() -> new AssertionError("L’utente non è stato creato nel database"));
        assertFalse(newUser.isEnabled(), "L’utente deve essere abilitato");
        assertTrue(newUser.getRoles().stream().anyMatch(r -> r.getName().equals(role)), "L’utente deve avere il ruolo ");

        List<RefreshToken> refreshTokenList = this.refreshTokenRepository.findAll();
        assertEquals(1, refreshTokenList.size());
    }

}
