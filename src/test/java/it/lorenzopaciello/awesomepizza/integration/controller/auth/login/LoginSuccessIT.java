package it.lorenzopaciello.awesomepizza.integration.controller.auth.login;

import it.lorenzopaciello.awesomepizza.integration.AbstractIntegrationTest;
import it.lorenzopaciello.awesomepizza.integration.controller.auth.shared.LoginUseCase;
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
        this.loginUseCase.loginSuccessAssertions(resultActions);
    }

}
