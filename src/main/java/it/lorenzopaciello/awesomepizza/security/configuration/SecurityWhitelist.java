package it.lorenzopaciello.awesomepizza.security.configuration;

import java.util.List;

public class SecurityWhitelist {

    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/api/auth/login",
            "/api/auth/refresh",
            "/api/auth/logout",
            "/api/pizzas",
            "/api/order",
            "/api/order/detail"
    );

}
