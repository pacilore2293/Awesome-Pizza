package it.lorenzopaciello.awesomepizza.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

//TODO DA SISTEMARE
@Getter
@Setter
public class LoginRequestDto {

    @NotBlank(message = "Username obbligatorio")
    private String username;

    @NotBlank(message = "Password obbligatoria")
    private String password;

}