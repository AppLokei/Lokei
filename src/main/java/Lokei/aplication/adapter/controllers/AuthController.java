package Lokei.aplication.adapter.controllers;

import Lokei.aplication.adapter.dto.req.EsqueciSenhaRequest;
import Lokei.aplication.adapter.dto.req.LoginRequest;
import Lokei.aplication.adapter.dto.req.RedefinirSenhaRequest;
import Lokei.aplication.adapter.dto.res.AuthResponse;
import Lokei.aplication.adapter.dto.res.MensagemResponse;
import Lokei.aplication.adapter.dto.res.MensagemResponseLog;
import Lokei.aplication.application.usecases.usuario.AutenticacaoUseCase;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AutenticacaoUseCase autenticacaoUseCase;

    public AuthController(AutenticacaoUseCase autenticacaoUseCase) {
        this.autenticacaoUseCase = autenticacaoUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(autenticacaoUseCase.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<MensagemResponseLog> logout() {
        return ResponseEntity.ok(autenticacaoUseCase.logout());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MensagemResponseLog> forgotPassword(@Valid @RequestBody EsqueciSenhaRequest request) {
        return ResponseEntity.ok(autenticacaoUseCase.esqueciSenha(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<MensagemResponseLog> resetPassword(@Valid @RequestBody RedefinirSenhaRequest request) {
        return ResponseEntity.ok(autenticacaoUseCase.redefinirSenha(request));
    }
}