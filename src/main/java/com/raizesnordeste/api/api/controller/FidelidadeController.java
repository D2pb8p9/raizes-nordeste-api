package com.raizesnordeste.api.api.controller;

import com.raizesnordeste.api.api.dto.response.FidelidadeResponse;
import com.raizesnordeste.api.application.service.FidelidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Fidelidade",
        description = "Programa de pontos — acúmulo com consentimento do cliente")
@SecurityRequirement(name = "bearer-auth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/fidelidade")
public class FidelidadeController {

    private final FidelidadeService fidelidadeService;

    @Operation(summary = "Consultar pontos",
            description = "Retorna o saldo de pontos do cliente autenticado. Acesso: CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Saldo retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Registro de fidelidade não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/meus-pontos")
    public ResponseEntity<FidelidadeResponse> consultarMeusPontos(Authentication authentication) {
        return ResponseEntity.ok(fidelidadeService.consultarMeusPontos(authentication.getName()));
    }

    @Operation(summary = "Aceitar programa de fidelidade",
            description = "Registra o consentimento do cliente e cria o registro de pontos. Acesso: CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consentimento registrado com sucesso"),
            @ApiResponse(responseCode = "409", description = "Cliente já aderiu ao programa"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping("/consentimento")
    public ResponseEntity<FidelidadeResponse> aceitarConsentimento(Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fidelidadeService.aceitarConsentimento(authentication.getName()));
    }
}
