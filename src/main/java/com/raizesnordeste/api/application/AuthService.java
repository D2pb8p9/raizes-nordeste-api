package com.raizesnordeste.api.application;

import com.raizesnordeste.api.api.dto.request.LoginRequest;
import com.raizesnordeste.api.api.dto.request.RegistroRequest;
import com.raizesnordeste.api.domain.Usuario;
import com.raizesnordeste.api.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public void registrar(RegistroRequest request) {
        log.info("Iniciando registro de usuário {}", request.email());

        Usuario usuario = new Usuario();

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setRole(request.role());
        usuarioRepository.save(usuario);

        log.info("Usuário {} registrado com sucesso", request.email());
    }

    public String executarLogin(LoginRequest request) {
        log.info("Iniciando login de usuário {}", request.email());

        var usuario = usuarioRepository.findByEmail(request.email());

        if (usuario.isEmpty()) {
            log.warn("Usuário {} não encontrado", request.email());
            throw new RuntimeException("Usuário não encontrado");
        }

        if (!passwordEncoder.matches(request.senha(), usuario.get().getSenha())) {
            log.warn("Senha incorreta para o usuário {}", request.email());
            throw new RuntimeException("Credenciais inválidas");
        }

        String token = jwtService.generateToken(usuario.get().getEmail());

        log.info("Usuário {} logado com sucesso, token gerado", request.email());

        return token;
    }
}
