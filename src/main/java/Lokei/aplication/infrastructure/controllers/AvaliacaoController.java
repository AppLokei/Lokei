package Lokei.aplication.infrastructure.controllers;

import Lokei.aplication.application.dto.avaliacao.AvaliacaoResponse;
import Lokei.aplication.application.dto.avaliacao.CriarAvaliacaoAnuncioRequest;
import Lokei.aplication.application.dto.avaliacao.CriarAvaliacaoPerfilRequest;
import Lokei.aplication.application.service.AvaliacaoService;
import Lokei.aplication.infrastructure.security.UsuarioAutenticadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService, UsuarioAutenticadoService usuarioAutenticadoService) {
        this.avaliacaoService = avaliacaoService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @PostMapping("/anuncios")
    public ResponseEntity<AvaliacaoResponse> avaliarAnuncio(@Valid @RequestBody CriarAvaliacaoAnuncioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(avaliacaoService.avaliarAnuncio(usuarioAutenticadoService.getUsuarioId(), request));
    }

    @PostMapping("/perfis")
    public ResponseEntity<AvaliacaoResponse> avaliarPerfil(@Valid @RequestBody CriarAvaliacaoPerfilRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(avaliacaoService.avaliarContraparte(usuarioAutenticadoService.getUsuarioId(), request));
    }

    @GetMapping("/anuncios/{anuncioId}")
    public ResponseEntity<List<AvaliacaoResponse>> listarAvaliacoesAnuncio(@PathVariable Integer anuncioId) {
        return ResponseEntity.ok(avaliacaoService.listarAvaliacoesDoAnuncio(anuncioId));
    }

    @GetMapping("/usuarios/{usuarioId}")
    public ResponseEntity<List<AvaliacaoResponse>> listarAvaliacoesUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(avaliacaoService.listarAvaliacoesDoUsuario(usuarioId));
    }
}
