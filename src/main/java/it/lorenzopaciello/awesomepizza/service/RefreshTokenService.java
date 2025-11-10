package it.lorenzopaciello.awesomepizza.service;

import it.lorenzopaciello.awesomepizza.exception.ErrorCode;
import it.lorenzopaciello.awesomepizza.exception.custom.NotFoundException;
import it.lorenzopaciello.awesomepizza.model.RefreshToken;
import it.lorenzopaciello.awesomepizza.model.User;
import it.lorenzopaciello.awesomepizza.repository.RefreshTokenRepository;
import it.lorenzopaciello.awesomepizza.repository.UserRepository;
import it.lorenzopaciello.awesomepizza.service.interfaces.RefreshTokenServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenServiceInterface {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND_USERNAME));

        refreshTokenRepository.deleteByUser(user);

        String tokenString = jwtService.generateRefreshToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .build()
        );

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(tokenString)
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

}
