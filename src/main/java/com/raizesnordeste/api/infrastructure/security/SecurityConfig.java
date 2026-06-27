package com.raizesnordeste.api.infrastructure.security;

import com.raizesnordeste.api.application.service.UsuarioDetailsService;
import com.raizesnordeste.api.infrastructure.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UsuarioDetailsService usuarioDetailsService;

    private final JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/login", "/auth/registro").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/unidades", "/unidades/**").hasAnyRole("GERENTE", "FRANQUEADORA")
                        .requestMatchers(HttpMethod.POST, "/unidades").hasAnyRole("GERENTE", "FRANQUEADORA")
                        .requestMatchers(HttpMethod.PUT, "/unidades/**").hasAnyRole("GERENTE", "FRANQUEADORA")
                        .requestMatchers(HttpMethod.GET, "/produtos", "/produtos/**").hasAnyRole("GERENTE", "FRANQUEADORA")
                        .requestMatchers(HttpMethod.POST, "/produtos").hasAnyRole("GERENTE", "FRANQUEADORA")
                        .requestMatchers(HttpMethod.PUT, "/produtos/**").hasAnyRole("GERENTE", "FRANQUEADORA")
                        .requestMatchers(HttpMethod.GET, "/cardapio/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/cardapio").hasAnyRole("GERENTE", "FRANQUEADORA")
                        .requestMatchers(HttpMethod.PUT, "/cardapio/**").hasAnyRole("GERENTE", "FRANQUEADORA")
                        .requestMatchers(HttpMethod.POST, "/estoque/entrada").hasRole("GERENTE")
                        .requestMatchers(HttpMethod.GET, "/estoque/**").hasAnyRole("GERENTE", "FRANQUEADORA")
                        .requestMatchers(HttpMethod.POST, "/pedidos").hasAnyRole("CLIENTE", "ATENDENTE")
                        .requestMatchers(HttpMethod.GET, "/pedidos/**").hasAnyRole("ATENDENTE", "COZINHEIRO", "GERENTE")
                        .requestMatchers(HttpMethod.PATCH, "/pedidos/*/status").hasAnyRole("COZINHEIRO", "ATENDENTE")
                        .requestMatchers(HttpMethod.PATCH, "/pedidos/*/cancelar").hasAnyRole("CLIENTE", "ATENDENTE", "GERENTE")
                        .anyRequest().authenticated())
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(usuarioDetailsService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
