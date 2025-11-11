package it.lorenzopaciello.awesomepizza.service.interfaces;

import it.lorenzopaciello.awesomepizza.controller.dto.request.LoginRequestDto;
import it.lorenzopaciello.awesomepizza.controller.dto.request.RegistrationRequestDto;
import it.lorenzopaciello.awesomepizza.controller.dto.response.UserResponseDto;
import it.lorenzopaciello.awesomepizza.model.User;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface AuthServiceInterface {
    public Map<String, String> login(LoginRequestDto authRequestDto, HttpServletResponse response);
    public Map<String, String> refresh(String refreshToken, HttpServletResponse response);
    public UserResponseDto registerUser(RegistrationRequestDto registrationRequestDto);
    public Boolean logout(String refreshToken, HttpServletResponse response);
}
