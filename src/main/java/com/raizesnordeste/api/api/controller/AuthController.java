package com.raizesnordeste.api.api.controller;

import com.raizesnordeste.api.api.dto.request.LoginRequest;
import com.raizesnordeste.api.api.dto.request.RegistroRequest;
import com.raizesnordeste.api.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticação", description = "Registro e login de usuários")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Registrar usuário",
            description = "Cria um novo usuário com nome, e-mail, senha e perfil")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou role inexistente"),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
    })
    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody @Valid RegistroRequest request) {
        authService.registrar(request);
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Realizar login",
            description = "Autentica o usuário e retorna um token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado — retorna token JWT"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<?> executarLogin(@RequestBody @Valid LoginRequest request) {
        String token = authService.executarLogin(request);
        return ResponseEntity.status(200).body(Map.of("token", token));
    }
}
