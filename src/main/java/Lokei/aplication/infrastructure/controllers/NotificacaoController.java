package Lokei.aplication.infrastructure.controllers;

import Lokei.aplication.application.dto.common.MensagemResponse;
import Lokei.aplication.application.dto.notificacao.NotificacaoResponse;
import Lokei.aplication.application.service.NotificacaoService;
import Lokei.aplication.infrastructure.security.UsuarioAutenticadoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public NotificacaoController(NotificacaoService notificacaoService, UsuarioAutenticadoService usuarioAutenticadoService) {
        this.notificacaoService = notificacaoService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @GetMapping
    public ResponseEntity<List<NotificacaoResponse>> listar() {
        return ResponseEntity.ok(notificacaoService.listar(usuarioAutenticadoService.getUsuarioId()));
    }

    @PatchMapping("/{id}/lida")
    public ResponseEntity<MensagemResponse> marcarComoLida(@PathVariable Integer id) {
        notificacaoService.marcarComoLida(id, usuarioAutenticadoService.getUsuarioId());
        return ResponseEntity.ok(new MensagemResponse("Notificacao marcada como lida."));
    }
}
