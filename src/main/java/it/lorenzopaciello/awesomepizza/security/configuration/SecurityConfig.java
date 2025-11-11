package it.lorenzopaciello.awesomepizza.security.configuration;

import it.lorenzopaciello.awesomepizza.security.bean.CustomAccessDeniedHandler;
import it.lorenzopaciello.awesomepizza.security.bean.JwtAuthenticationEntryPoint;
import it.lorenzopaciello.awesomepizza.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationEntryPoint authEntryPoint,
                                                   CustomAccessDeniedHandler accessDeniedHandler) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(SecurityWhitelist.PUBLIC_ENDPOINTS.toArray(new String[0])).permitAll()

                        // solo ADMIN può creare nuovi utenti (AL MOMENTO)
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/order/taken").hasAnyRole("ADMIN", "PIZZAIOLO")
                        .requestMatchers(HttpMethod.PUT, "/api/order/ready").hasAnyRole("ADMIN", "PIZZAIOLO")
                        .requestMatchers(HttpMethod.PUT, "/api/order/escape").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/order/search").hasAnyRole("ADMIN", "PIZZAIOLO")

                        .anyRequest().authenticated()
                )
                //.userDetailsService(userDetailsService)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
