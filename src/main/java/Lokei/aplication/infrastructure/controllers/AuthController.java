package Lokei.aplication.infrastructure.controllers;

import Lokei.aplication.application.dto.auth.*;
import Lokei.aplication.application.dto.common.MensagemResponse;
import Lokei.aplication.application.service.AutenticacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AutenticacaoService autenticacaoService;

    public AuthController(AutenticacaoService autenticacaoService) {
        this.autenticacaoService = autenticacaoService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody CadastroUsuarioRequest request) {
        return ResponseEntity.ok(autenticacaoService.cadastrar(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(autenticacaoService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<MensagemResponse> logout() {
        return ResponseEntity.ok(autenticacaoService.logout());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MensagemResponse> forgotPassword(@Valid @RequestBody EsqueciSenhaRequest request) {
        return ResponseEntity.ok(autenticacaoService.esqueciSenha(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MensagemResponse> resetPassword(@Valid @RequestBody RedefinirSenhaRequest request) {
        return ResponseEntity.ok(autenticacaoService.redefinirSenha(request));
    }
}
