package it.lorenzopaciello.awesomepizza.security.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.lorenzopaciello.awesomepizza.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private MessageSource messageSource;

    @Autowired
    public CustomAccessDeniedHandler(MessageSource messageSource){
        this.messageSource = messageSource;
    }


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ObjectMapper mapper = new ObjectMapper();

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        String jsonResponse = mapper.writeValueAsString(Map.of(
                "code", ErrorCode.ACCESS_DENIED.getCode(),
                "message", messageSource.getMessage(
                        ErrorCode.ACCESS_DENIED.getMessageKey(),
                        null,
                        LocaleContextHolder.getLocale()
                )
        ));

        response.getWriter().write(jsonResponse);
    }
}
