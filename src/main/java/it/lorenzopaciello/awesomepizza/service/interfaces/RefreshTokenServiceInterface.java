package it.lorenzopaciello.awesomepizza.service.interfaces;

import it.lorenzopaciello.awesomepizza.model.RefreshToken;

public interface RefreshTokenServiceInterface {
    public RefreshToken createRefreshToken(String username);
    public void revokeToken(String token);
}
