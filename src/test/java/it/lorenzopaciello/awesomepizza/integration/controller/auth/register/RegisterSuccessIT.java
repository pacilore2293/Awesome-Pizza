package it.lorenzopaciello.awesomepizza.integration.controller.auth.register;

import it.lorenzopaciello.awesomepizza.integration.AbstractIntegrationTest;
import it.lorenzopaciello.awesomepizza.integration.controller.auth.shared.LoginUseCase;
import it.lorenzopaciello.awesomepizza.integration.controller.auth.shared.RegisterUseCase;
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
    private LoginUseCase loginUseCase;
    @Autowired
    private RegisterUseCase registerUseCase;

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
        this.loginUseCase.loginSuccessAssertions(result, 1);
        String accessToken = this.loginUseCase.loginSuccessGetToken(result.andReturn());
        ResultActions resultActions = this.registerUseCase.RegisterRequest("pizzaiolo1", "test_prova", "ROLE_PIZZAIOLO", accessToken, null, this.mockMvc);
        this.registerUseCase.registerSuccessAssertions(resultActions, "pizzaiolo1", "ROLE_PIZZAIOLO");


    }

}
