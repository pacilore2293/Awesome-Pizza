package it.lorenzopaciello.awesomepizza.service;

import it.lorenzopaciello.awesomepizza.exception.ErrorCode;
import it.lorenzopaciello.awesomepizza.exception.custom.NotFoundException;
import it.lorenzopaciello.awesomepizza.model.RefreshToken;
import it.lorenzopaciello.awesomepizza.model.User;
import it.lorenzopaciello.awesomepizza.repository.RefreshTokenRepository;
import it.lorenzopaciello.awesomepizza.repository.UserRepository;
import it.lorenzopaciello.awesomepizza.service.interfaces.RefreshTokenServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefreshTokenService implements RefreshTokenServiceInterface {

    @Value("${security.jwt.refresh-expiration}")
    private long refreshExpirationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(String username) {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND_USERNAME));

        List<RefreshToken> refreshTokenListNotRevoked = this.refreshTokenRepository.findByUserAndRevokedFalse(user);
        if(refreshTokenListNotRevoked != null && !refreshTokenListNotRevoked.isEmpty()){
            refreshTokenListNotRevoked.forEach(refreshToken -> {
                refreshToken.setRevoked(true);
                refreshTokenRepository.save(refreshToken);
            });
        }

        String tokenString = jwtService.generateRefreshToken(
                org.springframework.security.core.userdetails.User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .build()
        );

        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(tokenString)
                .expiryDate(Instant.ofEpochSecond(refreshExpirationMs))
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public void revokeToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(rt -> {
            rt.setRevoked(true);
            refreshTokenRepository.save(rt);
        });
    }

    public boolean validateRefreshToken(RefreshToken token) {
        return !token.isRevoked() && token.getExpiryDate().isAfter(Instant.now());
    }

}
