package it.lorenzopaciello.awesomepizza.integration.controller.auth.login;

import it.lorenzopaciello.awesomepizza.integration.AbstractIntegrationTest;
import it.lorenzopaciello.awesomepizza.integration.controller.auth.shared.LoginUseCase;
import it.lorenzopaciello.awesomepizza.integration.controller.auth.shared.RegisterUseCase;
import it.lorenzopaciello.awesomepizza.repository.RefreshTokenRepository;
import it.lorenzopaciello.awesomepizza.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
public class LoginSuccessIT extends AbstractIntegrationTest {

    @Autowired
    private LoginUseCase loginUseCase;
    @Autowired
    private RegisterUseCase registerUseCase;
    @Autowired
    private UserRepository userRepository;
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
    @DisplayName("Login admin con credenziali corrette restituisce 200 con access token nel response body e refresh nel cookie httpOnly")
    void loginAdminSuccess() throws Exception {
        ResultActions resultActions = this.loginUseCase.loginRequest("admin", "admin_pass", null, this.mockMvc);
        this.loginUseCase.loginSuccessAssertions(resultActions, 1);
        Thread.sleep(500);
    }

    @Test
    @DisplayName("Login pizzaiolo dopo registrazione con credenziali corrette restituisce 200 con access token nel response body e refresh nel cookie httpOnly")
    void loginPizzaioloSuccess() throws Exception {
        ResultActions resultActionsLoginAdmin = this.loginUseCase.loginRequest("admin", "admin_pass", null, this.mockMvc);
        this.loginUseCase.loginSuccessAssertions(resultActionsLoginAdmin, 1);
        String tokenAdmin = this.loginUseCase.loginSuccessGetToken(resultActionsLoginAdmin.andReturn());

        ResultActions resultActions = this.registerUseCase.RegisterRequest("pizzaiolo1", "admin_pass", "ROLE_PIZZAIOLO", tokenAdmin, null, this.mockMvc);
        this.registerUseCase.registerSuccessAssertions(resultActions, "pizzaiolo1", "ROLE_PIZZAIOLO");

        ResultActions resultActionsLoginPizzaiolo = this.loginUseCase.loginRequest("pizzaiolo1", "admin_pass", null, this.mockMvc);
        this.loginUseCase.loginSuccessAssertions(resultActionsLoginPizzaiolo, 2);
    }

}
