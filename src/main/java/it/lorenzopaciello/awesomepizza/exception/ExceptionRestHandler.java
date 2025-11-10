package it.lorenzopaciello.awesomepizza.exception;

import it.lorenzopaciello.awesomepizza.exception.custom.ConflictException;
import it.lorenzopaciello.awesomepizza.exception.custom.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionRestHandler {

    private MessageSource messageSource;

    @Autowired
    public ExceptionRestHandler(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    //----- BEAN VALIDATION -----//
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        Map<String, List<String>> responseMap = Map.of("B001", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(responseMap);
    }

    //----- SPRING SECURITY -----//
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ResponseErrorDto> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseErrorDto.builder()
                        .code(ErrorCode.AUTH_INVALID_CREDENTIALS.getCode())
                        .message(this.messageSource.getMessage(ErrorCode.AUTH_INVALID_CREDENTIALS.getMessageKey(), null, LocaleContextHolder.getLocale()))
                        .build());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ResponseErrorDto> handleUserNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ResponseErrorDto.builder()
                        .code(ErrorCode.USER_NOT_FOUND_USERNAME.getCode())
                        .message(this.messageSource.getMessage(ErrorCode.USER_NOT_FOUND_USERNAME.getMessageKey(), null, LocaleContextHolder.getLocale()))
                        .build());
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<Map<String, String>> handleLocked(LockedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of(ErrorCode.AUTH_BLOCKED.getCode(), this.messageSource.getMessage(ErrorCode.AUTH_BLOCKED.getMessageKey(), null, LocaleContextHolder.getLocale())));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Map<String, String>> handleDisabled(DisabledException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of(ErrorCode.AUTH_DISABLED.getCode(), this.messageSource.getMessage(ErrorCode.AUTH_DISABLED.getMessageKey(), null, LocaleContextHolder.getLocale())));
    }

    //----- CUSTOM -----//
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(ex.getErrorCode().getCode(), this.messageSource.getMessage(ex.getErrorCode().getMessageKey(), null, LocaleContextHolder.getLocale())));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, String>> handleConflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(ex.getErrorCode().getCode(), this.messageSource.getMessage(ex.getErrorCode().getMessageKey(), null, LocaleContextHolder.getLocale())));
    }

    //----- GENRIC -----//
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(ErrorCode.INTERNAL_ERROR.getCode(), ErrorCode.INTERNAL_ERROR.getMessageKey()));
    }
}
