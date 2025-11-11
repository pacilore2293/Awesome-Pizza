package it.lorenzopaciello.awesomepizza.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // --- Autenticazione ---
    AUTH_INVALID_CREDENTIALS("AUTH_001", "NotAuthorized.badCredential"),
    AUTH_TOKEN_EXPIRED("AUTH_002", "NotAuthorized.tokenExpired"),
    AUTH_TOKEN_INVALID("AUTH_003", "NotAuthorized.tokenNotValid"),
    AUTH_BLOCKED("AUTH_004", "NotAuthorized.blocked"),
    AUTH_DISABLED("AUTH_005", "NotAuthorized.disabled"),
    AUTH_REFRESH_TOKEN_NOT_FOUND("AUTH_005", "NotAuthorized.refreshTokenNotFound"),

    // --- Autorizzazione ---
    ACCESS_DENIED("AUTH_006", "NotAuthorized.accessDenied"),

    // --- Utente ---
    USER_NOT_FOUND_USERNAME("USR_001", "NotFound.user.username"),
    USER_ALREADY_EXISTS_USERNAME("USR_002", "Conflict.user.username"),
    USER_ALREADY_HAS_TAKEN("USR_003", "Conflict.user.taken"),

    // --- Pizza  ---
    PIZZA_NOT_FOUND_ID("PIZZA_001", "NotFound.pizza.id"),

    // --- Ruoli  ---
    ROLE_NOT_FOUND_NAME("ROLE_001", "NotFound.role.name"),

    // --- ordini  ---
    ORDER_NOT_FOUND_ID("ORDER_001", "NotFound.order.id"),
    ORDER_NOT_FOUND_TAKEN("ORDER_002", "NotFound.order.taken"),
    ORDER_NOT_FOUND_READY("ORDER_003", "NotFound.order.ready"),
    ORDER_NOT_FOUND_ESCAPE("ORDER_004", "NotFound.order.escape"),
    ORDER_ACCESS_DENIED_READY("ORDER_005", "NotAuthorized.order.ready"),

    // --- Generico ---
    INTERNAL_ERROR("GEN_001", "InternalServer.error");

    private final String code;
    private final String messageKey;

}
