package it.lorenzopaciello.awesomepizza.service.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface JwtServiceInterface {

    /**
     * Genera un Access Token JWT con informazioni di autenticazione e ruolo.
     */
    String generateAccessToken(UserDetails user);

    /**
     * Genera un Refresh Token JWT a lunga durata.
     */
    String generateRefreshToken(UserDetails user);

    /**
     * Estrae lo username (subject) dal token JWT.
     */
    String extractUsername(String token);

    /**
     * Estrae i ruoli/authorities dal token, se presenti.
     */
    List<String> extractRoles(String token);

    /**
     * Verifica se il token JWT è valido.
     */
    boolean isTokenValid(String token, UserDetails userDetails);

}