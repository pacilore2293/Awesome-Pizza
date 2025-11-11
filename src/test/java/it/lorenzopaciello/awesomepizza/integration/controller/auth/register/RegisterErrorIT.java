package it.lorenzopaciello.awesomepizza.integration.controller.auth.register;

import it.lorenzopaciello.awesomepizza.integration.AbstractIntegrationTest;
import it.lorenzopaciello.awesomepizza.integration.controller.auth.shared.LoginUseCase;
import it.lorenzopaciello.awesomepizza.integration.controller.auth.shared.RegisterUseCase;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegisterErrorIT extends AbstractIntegrationTest {

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
    @Order(1)
    @DisplayName("Registrazione nuovo utente pizzaiolo senza permessi admin")
    void registerNewUserNoPermission() throws Exception {

        ResultActions result = this.loginUseCase.loginRequest("admin", "admin_pass", null, this.mockMvc);
        this.loginUseCase.loginSuccessAssertions("admin", result, 1);
        String accessToken = this.loginUseCase.loginSuccessGetToken(result.andReturn());

        ResultActions resultActions = this.registerUseCase.RegisterRequest("pizzaiolo1", "test_prova", "ROLE_PIZZAIOLO", accessToken, null, this.mockMvc);
        this.registerUseCase.registerSuccessAssertions(resultActions, "pizzaiolo1", "ROLE_PIZZAIOLO", 0);

        ResultActions resultLoginPizzaiolo = this.loginUseCase.loginRequest("pizzaiolo1", "test_prova", null, this.mockMvc);
        this.loginUseCase.loginSuccessAssertions("pizzaiolo1", resultLoginPizzaiolo, 1);
        String accessTokenPizzaiolo = this.loginUseCase.loginSuccessGetToken(resultLoginPizzaiolo.andReturn());

        ResultActions resultActionPizzaiolo = this.registerUseCase.RegisterRequest("pizzaiolo2", "test_prova", "ROLE_PIZZAIOLO", accessTokenPizzaiolo, null, this.mockMvc);
        this.registerUseCase.registerErrorNotPermissionAssertions(resultActionPizzaiolo, "pizzaiolo1", "ROLE_PIZZAIOLO", 0);
        Thread.sleep(1000);
    }

    @Test
    @Order(2)
    @DisplayName("Registrazione nuovo utente pizzaiolo senza token")
    void registerNewUserNoPToken() throws Exception {

        ResultActions result = this.loginUseCase.loginRequest("admin", "admin_pass", null, this.mockMvc);
        this.loginUseCase.loginSuccessAssertions("admin", result, 2);
        String accessToken = this.loginUseCase.loginSuccessGetToken(result.andReturn());

        ResultActions resultActions = this.registerUseCase.RegisterRequest("pizzaiolo1", "test_prova", "ROLE_PIZZAIOLO", null, null, this.mockMvc);
        this.registerUseCase.registerErrorNotAuthAssertions(resultActions, "pizzaiolo1", "ROLE_PIZZAIOLO", 0);
        Thread.sleep(1000);
    }

}
