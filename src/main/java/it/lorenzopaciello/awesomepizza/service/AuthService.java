package it.lorenzopaciello.awesomepizza.service;

import it.lorenzopaciello.awesomepizza.controller.dto.request.LoginRequestDto;
import it.lorenzopaciello.awesomepizza.controller.dto.request.RegistrationRequestDto;
import it.lorenzopaciello.awesomepizza.exception.ErrorCode;
import it.lorenzopaciello.awesomepizza.exception.custom.ConflictException;
import it.lorenzopaciello.awesomepizza.model.RefreshToken;
import it.lorenzopaciello.awesomepizza.model.Role;
import it.lorenzopaciello.awesomepizza.model.UserAuth;
import it.lorenzopaciello.awesomepizza.repository.UserAuthRepository;
import it.lorenzopaciello.awesomepizza.security.service.CustomUserDetailsService;
import it.lorenzopaciello.awesomepizza.service.interfaces.AuthServiceInterface;
import it.lorenzopaciello.awesomepizza.service.interfaces.JwtServiceInterface;
import it.lorenzopaciello.awesomepizza.service.interfaces.SearchServiceInterface;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthServiceInterface {

    private final UserAuthRepository userAuthRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtServiceInterface jwtService;
    private final PasswordEncoder passwordEncoder;
    private final SearchServiceInterface searchService;
    private final RefreshTokenService refreshTokenService;

    @Value("${security.jwt.refresh-expiration}")
    private long refreshExpirationMs;

    @Override
    public Map<String, String> login(LoginRequestDto request, HttpServletResponse response) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails user = userDetailsService.loadUserByUsername(request.getUsername());
        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/api/auth/refresh")
                .maxAge(refreshExpirationMs)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return Map.of(
                "accessToken", accessToken
        );
    }

    @Override
    public UserAuth registerUser(RegistrationRequestDto registrationRequestDto) {
        if (userAuthRepository.findByUsername(registrationRequestDto.getUsername()).isPresent()) {
            throw new ConflictException(ErrorCode.USER_ALREADY_EXISTS_USERNAME);
        }

        UserAuth user = UserAuth.builder()
                .username(registrationRequestDto.getUsername())
                .password(passwordEncoder.encode(registrationRequestDto.getPassword()))
                .roles(new HashSet<>())
                .build();

        Role role = this.searchService.findRoleByName(registrationRequestDto.getRole());
        user.getRoles().add(role);

        return userAuthRepository.save(user);
    }
}
