package it.lorenzopaciello.awesomepizza.repository;

import it.lorenzopaciello.awesomepizza.model.RefreshToken;
import it.lorenzopaciello.awesomepizza.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    void deleteByUser(User user);
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findByUserAndRevokedFalse(User user);
    List<RefreshToken> findByUser(User user);
}
