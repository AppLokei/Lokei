package Lokei.aplication.infrastructure.controllers;

import Lokei.aplication.application.dto.common.MensagemResponse;
import Lokei.aplication.application.dto.profile.*;
import Lokei.aplication.application.service.PerfilService;
import Lokei.aplication.infrastructure.security.UsuarioAutenticadoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PerfilController {

    private final PerfilService perfilService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public PerfilController(PerfilService perfilService, UsuarioAutenticadoService usuarioAutenticadoService) {
        this.perfilService = perfilService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @GetMapping("/perfil")
    public ResponseEntity<PerfilResponse> consultarPerfil() {
        return ResponseEntity.ok(perfilService.consultarPerfil(usuarioAutenticadoService.getUsuarioId()));
    }

    @PutMapping("/perfil")
    public ResponseEntity<PerfilResponse> atualizarPerfil(@Valid @RequestBody AtualizarPerfilRequest request) {
        return ResponseEntity.ok(perfilService.atualizarPerfil(usuarioAutenticadoService.getUsuarioId(), request));
    }

    @PostMapping("/perfil/email/solicitar")
    public ResponseEntity<MensagemResponse> solicitarAlteracaoEmail(@Valid @RequestBody SolicitarAlteracaoEmailRequest request) {
        return ResponseEntity.ok(perfilService.solicitarAlteracaoEmail(usuarioAutenticadoService.getUsuarioId(), request));
    }

    @PostMapping("/perfil/email/confirmar")
    public ResponseEntity<MensagemResponse> confirmarAlteracaoEmail(@Valid @RequestBody ConfirmarAlteracaoEmailRequest request) {
        return ResponseEntity.ok(perfilService.confirmarAlteracaoEmail(usuarioAutenticadoService.getUsuarioId(), request));
    }

    @GetMapping("/enderecos/cep/{cep}")
    public ResponseEntity<ConsultaCepResponse> consultarCep(@PathVariable String cep) {
        return ResponseEntity.ok(perfilService.consultarCep(cep));
    }
}
