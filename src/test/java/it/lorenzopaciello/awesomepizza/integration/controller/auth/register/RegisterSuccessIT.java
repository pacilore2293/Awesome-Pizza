package it.lorenzopaciello.awesomepizza.integration.controller.auth.register;

import it.lorenzopaciello.awesomepizza.integration.AbstractIntegrationTest;
import it.lorenzopaciello.awesomepizza.integration.controller.auth.shared.LoginUseCase;
import it.lorenzopaciello.awesomepizza.model.RefreshToken;
import it.lorenzopaciello.awesomepizza.model.User;
import it.lorenzopaciello.awesomepizza.repository.RefreshTokenRepository;
import it.lorenzopaciello.awesomepizza.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
public class RegisterSuccessIT extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LoginUseCase loginUseCase;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Container
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("awesomepizza_db")
                    .withUsername("awesomepizza_admin")
                    .withPassword("awesomepizza_pass");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    @DisplayName("Registrazione nuovo utente pizzaiolo")
    void registerNewUserPizzaiolo() throws Exception {
        ResultActions result = this.loginUseCase.loginRequest("admin", "admin_pass", null, this.mockMvc);
        this.loginUseCase.loginSuccessAssertions(result);
        String accessToken = this.loginUseCase.loginSuccessGetToken(result.andReturn());
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content("""
                        {
                            "username": "pizzaiolo",
                            "password": "pizza_pass",
                            "role": "ROLE_PIZZAIOLO"
                        }
                    """))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("pizzaiolo"))
                .andExpect(jsonPath("$.enabled").value(false))
                .andExpect(jsonPath("$.roles[0].name").value("ROLE_PIZZAIOLO"));

        User newUser = userRepository.findByUsername("pizzaiolo").orElseThrow(() -> new AssertionError("L’utente pizzaiolo non è stato creato nel database"));
        assertFalse(newUser.isEnabled(), "L’utente pizzaiolo deve essere abilitato");
        assertTrue(newUser.getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_PIZZAIOLO")), "L’utente pizzaiolo deve avere il ruolo ROLE_PIZZAIOLO");

        List<RefreshToken> refreshTokenList = this.refreshTokenRepository.findAll();
        assertEquals(1, refreshTokenList.size());
    }

}
