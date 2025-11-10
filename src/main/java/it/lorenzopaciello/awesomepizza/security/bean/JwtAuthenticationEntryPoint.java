package it.lorenzopaciello.awesomepizza.security.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.lorenzopaciello.awesomepizza.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private MessageSource messageSource;

    @Autowired
    public JwtAuthenticationEntryPoint(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String jsonResponse = mapper.writeValueAsString(Map.of(
                "code", ErrorCode.AUTH_TOKEN_INVALID.getCode(),
                "message", messageSource.getMessage(
                        ErrorCode.AUTH_TOKEN_INVALID.getMessageKey(),
                        null,
                        LocaleContextHolder.getLocale()
                )
        ));

        response.getWriter().write(jsonResponse);
    }
}
