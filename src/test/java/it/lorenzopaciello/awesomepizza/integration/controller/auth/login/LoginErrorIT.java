package it.lorenzopaciello.awesomepizza.integration.controller.auth.login;

import it.lorenzopaciello.awesomepizza.integration.AbstractIntegrationTest;

import it.lorenzopaciello.awesomepizza.integration.controller.auth.shared.LoginUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Testcontainers
public class LoginErrorIT extends AbstractIntegrationTest {

    @Autowired
    private LoginUseCase loginUseCase;

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
    @DisplayName("Login admin con credenziali errate - username - ITA")
    void loginAdminBadCredentialUsername() throws Exception {
        this.loginUseCase.loginRequest("admin______", "admin_pass_______", null, this.mockMvc)
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("USR_001"))
                .andExpect(jsonPath("$.message").value("Utente non trovato per username"))
                .andExpect(cookie().doesNotExist("refreshToken"));

        this.loginUseCase.loginUnauthorizedAssertions("admin", 0);
    }

    @Test
    @DisplayName("Login admin con credenziali errate - password - ITA")
    void loginAdminBadCredentialPassword() throws Exception {
        this.loginUseCase.loginRequest("admin", "admin_pass_______", null, this.mockMvc)
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("AUTH_001"))
                .andExpect(jsonPath("$.message").value("Credenziali non valide"))
                .andExpect(cookie().doesNotExist("refreshToken"));

        this.loginUseCase.loginUnauthorizedAssertions("admin", 0);
    }

}
