package it.lorenzopaciello.awesomepizza.exception;

import it.lorenzopaciello.awesomepizza.exception.custom.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Locale;

@RestControllerAdvice
public class ExceptionRestHandler {

    private MessageSource messageSource;

    @Autowired
    public ExceptionRestHandler(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<String>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.messageSource.getMessage(ex.getMessage(), null, locale));
    }
}
