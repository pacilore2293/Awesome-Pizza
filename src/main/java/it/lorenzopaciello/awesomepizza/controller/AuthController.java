package it.lorenzopaciello.awesomepizza.controller;

import it.lorenzopaciello.awesomepizza.controller.dto.request.LoginRequestDto;
import it.lorenzopaciello.awesomepizza.controller.dto.request.RegistrationRequestDto;
import it.lorenzopaciello.awesomepizza.model.User;
import it.lorenzopaciello.awesomepizza.service.interfaces.AuthServiceInterface;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController("authController")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceInterface authService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginRequestDto request, HttpServletResponse response) {
        return ResponseEntity.ok(this.authService.login(request, response));
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid RegistrationRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.authService.registerUser(request));
    }

    @PutMapping("/logout")
    public ResponseEntity<Boolean> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK).body(this.authService.logout(refreshToken, response));
    }

}
