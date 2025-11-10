package it.lorenzopaciello.awesomepizza.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

//TODO DA SISTEMARE
@Getter
@Setter
public class RegistrationRequestDto {

    @NotBlank(message = "Username obbligatorio")
    private String username;

    @NotBlank(message = "Password obbligatoria")
    @Size(min = 8, message = "La password deve avere almeno 8 caratteri")
    private String password;

    @NotBlank(message = "Ruolo obbligatorio")
    private String role;

}