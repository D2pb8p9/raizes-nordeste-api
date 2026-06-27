package com.raizesnordeste.api.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raizesnordeste.api.api.dto.response.ErroResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException ex) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String usuario = auth != null ? auth.getName() : "desconhecido";

        log.warn("ACESSO_NEGADO | usuario={} | rota={}", usuario, request.getRequestURI());

        ErroResponse erro = new ErroResponse(
                "ACESSO_NEGADO",
                "Você não tem permissão para acessar este recurso.",
                LocalDateTime.now().toString(),
                request.getRequestURI()
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), erro);
    }
}
