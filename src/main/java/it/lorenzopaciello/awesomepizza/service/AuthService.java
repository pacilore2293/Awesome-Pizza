package it.lorenzopaciello.awesomepizza.service;

import it.lorenzopaciello.awesomepizza.controller.dto.request.LoginRequestDto;
import it.lorenzopaciello.awesomepizza.controller.dto.request.RegistrationRequestDto;
import it.lorenzopaciello.awesomepizza.controller.dto.response.UserResponseDto;
import it.lorenzopaciello.awesomepizza.exception.ErrorCode;
import it.lorenzopaciello.awesomepizza.exception.custom.BadRequestException;
import it.lorenzopaciello.awesomepizza.exception.custom.ConflictException;
import it.lorenzopaciello.awesomepizza.model.RefreshToken;
import it.lorenzopaciello.awesomepizza.model.Role;
import it.lorenzopaciello.awesomepizza.model.User;
import it.lorenzopaciello.awesomepizza.repository.UserRepository;
import it.lorenzopaciello.awesomepizza.security.service.CustomUserDetailsService;
import it.lorenzopaciello.awesomepizza.service.interfaces.AuthServiceInterface;
import it.lorenzopaciello.awesomepizza.service.interfaces.JwtServiceInterface;
import it.lorenzopaciello.awesomepizza.service.interfaces.SearchServiceInterface;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService implements AuthServiceInterface {

    private final UserRepository userAuthRepository;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtServiceInterface jwtService;
    private final PasswordEncoder passwordEncoder;
    private final SearchServiceInterface searchService;
    private final RefreshTokenService refreshTokenService;

    @Value("${security.jwt.refresh-expiration}")
    private long refreshExpirationMs;

    @Override
    @Transactional
    public Map<String, String> login(LoginRequestDto request, HttpServletResponse response) {

        User user = (User) userDetailsService.loadUserByUsername(request.getUsername());
        user.setEnabled(true);
        this.userAuthRepository.save(user);

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        //UserDetails userDeatils = userDetailsService.loadUserByUsername(request.getUsername());
        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(refreshExpirationMs)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return Map.of(
                "accessToken", accessToken
        );
    }

    @Override
    public Map<String, String> refresh(String refreshTokenIn, HttpServletResponse response) {

        if (refreshTokenIn == null) {
            throw new BadRequestException(ErrorCode.AUTH_REFRESH_TOKEN_NOT_FOUND);
        }

        RefreshToken refreshToken = this.searchService.findRefreshTokenByToken(refreshTokenIn);

        if (!refreshTokenService.validateRefreshToken(refreshToken)) {
            User user = refreshToken.getUser();

            String newAccessToken = jwtService.generateAccessToken(user);
            RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user.getUsername());
            refreshTokenService.revokeToken(refreshToken.getToken());

            ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken.getToken())
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .maxAge(refreshExpirationMs)
                    .sameSite("Strict")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return Map.of("accessToken", newAccessToken);

        }else{
            throw new BadRequestException(ErrorCode.AUTH_TOKEN_INVALID);
        }
    }

    @Override
    public UserResponseDto registerUser(RegistrationRequestDto registrationRequestDto) {
        if (userAuthRepository.findByUsername(registrationRequestDto.getUsername()).isPresent()) {
            throw new ConflictException(ErrorCode.USER_ALREADY_EXISTS_USERNAME);
        }

        User user = User.builder()
                .username(registrationRequestDto.getUsername())
                .password(passwordEncoder.encode(registrationRequestDto.getPassword()))
                .roles(new HashSet<>())
                .build();

        Role role = this.searchService.findRoleByName(registrationRequestDto.getRole());
        user.getRoles().add(role);

        User savedUser = userAuthRepository.save(user);
        return new UserResponseDto(savedUser);
    }

    @Override
    @Transactional
    public Boolean logout(String refreshToken, HttpServletResponse response) {

        if(refreshToken == null){
            throw new BadRequestException(ErrorCode.AUTH_REFRESH_TOKEN_NOT_FOUND);
        }else{
            String username = this.jwtService.extractUsername(refreshToken);
            User user = (User) this.userDetailsService.loadUserByUsername(username);
            user.setEnabled(false);
            this.userAuthRepository.save(user);

            refreshTokenService.revokeToken(refreshToken);

            ResponseCookie clearedCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .path("/")
                    .maxAge(0)
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, clearedCookie.toString());

            return true;
        }
    }
}
