package it.lorenzopaciello.awesomepizza.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.lorenzopaciello.awesomepizza.controller.dto.request.LoginRequestDto;
import it.lorenzopaciello.awesomepizza.controller.dto.request.RegistrationRequestDto;
import it.lorenzopaciello.awesomepizza.controller.dto.response.UserResponseDto;
import it.lorenzopaciello.awesomepizza.service.interfaces.AuthServiceInterface;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Autenticazione", description = "Gestione login, refresh e logout utenti")
@RestController("authController")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceInterface authService;

    @Operation(
            summary = "Esegue il login",
            description = "Restituisce un access token e un refresh token (HTTP-only cookie)",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Login Request esempio",
                                    summary = "Credenziali utente di admin",
                                    value = """
                                    {
                                        "username": "admin",
                                        "password": "admin_pass"
                                    }
                                """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login effettuato con successo",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNjg1NTQ4MDAwLCJleHAiOjE2ODU1NTAwMDB9.Yy9W-JmUPZ8wb3hDoK9xsw1oqlTnTxJHYeIDGlMdoT0"
                                }
                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenziali non valide",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                     "code": "AUTH_001",
                                     "message": "Credenziali non valide"
                                }
                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Errore interno del server",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                     "code": "GEN_001",
                                     "message": "Errore interno del server, contattare l'assistenza"
                                }
                            """
                            )
                    )
            )
    })
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginRequestDto request, HttpServletResponse response) {
        return ResponseEntity.ok(this.authService.login(request, response));
    }

    @Operation(summary = "Refresh token", description = "Rinnova e restituisce un access token e un refresh token (HTTP-only cookie)")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token rinnovato con successo",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                 {
                                    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGUiOiJST0xFX0FETUlOIiwiaWF0IjoxNjg1NTQ4MDAwLCJleHAiOjE2ODU1NTAwMDB9.Yy9W-JmUPZ8wb3hDoK9xsw1oqlTnTxJHYeIDGlMdoT0"
                                 }
                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Errore interno del server",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                     "code": "GEN_001",
                                     "message": "Errore interno del server, contattare l'assistenza"
                                }
                            """
                            )
                    )
            )
    })
    @PutMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        return ResponseEntity.ok(this.authService.refresh(refreshToken, response));
    }

    @Operation(
            summary = "Registra nuovo utente",
            description = "Restituisce le informazione dell'utente appena registrato",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Login Request esempio",
                                    summary = "Credenziali utente di admin",
                                    value = """
                                    {
                                         "username": "pizzaiolo3",
                                         "password": "admin_pass",
                                         "role": "ROLE_PIZZAIOLO"
                                    }
                                """
                            )
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Registrazione effettuata con successo",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                     "username": "pizzaiolo3",
                                     "enabled": false,
                                     "roles": [
                                         {
                                             "name": "ROLE_PIZZAIOLO"
                                         }
                                     ]
                               }
                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Credenziali non valide",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                     "code": "AUTH_001",
                                     "message": "Token mancante o non valido"
                                }
                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Non autorizzato",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                     "code": "AUTH_006",
                                     "message": "Accesso negato: privilegi insufficienti"
                                 }
                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Errore interno del server",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                     "code": "GEN_001",
                                     "message": "Errore interno del server, contattare l'assistenza"
                                }
                            """
                            )
                    )
            )
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody @Valid RegistrationRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.authService.registerUser(request));
    }

    @Operation(summary = "Effettua il logout dell'utente utente", description = "Restituisce true se l aprocedura ha avuto successo")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registrazione effettuata con successo",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                true
                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Errore interno del server",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                {
                                     "code": "GEN_001",
                                     "message": "Errore interno del server, contattare l'assistenza"
                                }
                            """
                            )
                    )
            )
    })
    @PutMapping("/logout")
    public ResponseEntity<Boolean> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.OK).body(this.authService.logout(refreshToken, response));
    }

}
