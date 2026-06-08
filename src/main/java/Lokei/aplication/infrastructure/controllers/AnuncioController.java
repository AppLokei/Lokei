package Lokei.aplication.infrastructure.controllers;

import Lokei.aplication.application.dto.aluguel.SolicitarAluguelRequest;
import Lokei.aplication.application.dto.aluguel.SolicitarAluguelResponse;
import Lokei.aplication.application.dto.anuncio.*;
import Lokei.aplication.application.dto.common.MensagemResponse;
import Lokei.aplication.application.dto.common.PageResponse;
import Lokei.aplication.application.service.AnuncioService;
import Lokei.aplication.application.useCases.ConsultarDisponibilidadeAnuncioUseCase;
import Lokei.aplication.application.useCases.DetalharAnuncioUseCase;
import Lokei.aplication.application.useCases.SolicitarAluguelUseCase;
import Lokei.aplication.infrastructure.security.UsuarioAutenticado;
import Lokei.aplication.infrastructure.security.UsuarioAutenticadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/anuncios")
public class AnuncioController {

    private final AnuncioService anuncioService;
    private final DetalharAnuncioUseCase detalharAnuncioUseCase;
    private final ConsultarDisponibilidadeAnuncioUseCase consultarDisponibilidadeAnuncioUseCase;
    private final SolicitarAluguelUseCase solicitarAluguelUseCase;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public AnuncioController(
            AnuncioService anuncioService,
            DetalharAnuncioUseCase detalharAnuncioUseCase,
            ConsultarDisponibilidadeAnuncioUseCase consultarDisponibilidadeAnuncioUseCase,
            SolicitarAluguelUseCase solicitarAluguelUseCase,
            UsuarioAutenticadoService usuarioAutenticadoService
    ) {
        this.anuncioService = anuncioService;
        this.detalharAnuncioUseCase = detalharAnuncioUseCase;
        this.consultarDisponibilidadeAnuncioUseCase = consultarDisponibilidadeAnuncioUseCase;
        this.solicitarAluguelUseCase = solicitarAluguelUseCase;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @PostMapping
    public ResponseEntity<AnuncioResumoResponse> criar(@Valid @RequestBody CriarAnuncioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(anuncioService.criar(usuarioAutenticadoService.getUsuarioId(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnuncioResumoResponse> atualizar(@PathVariable Integer id, @Valid @RequestBody AtualizarAnuncioRequest request) {
        return ResponseEntity.ok(anuncioService.atualizar(id, usuarioAutenticadoService.getUsuarioId(), request));
    }

    @PatchMapping("/{id}/pausar")
    public ResponseEntity<MensagemResponse> pausar(@PathVariable Integer id) {
        anuncioService.pausar(id, usuarioAutenticadoService.getUsuarioId());
        return ResponseEntity.ok(new MensagemResponse("Anuncio pausado com sucesso."));
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<MensagemResponse> reativar(@PathVariable Integer id) {
        anuncioService.reativar(id, usuarioAutenticadoService.getUsuarioId());
        return ResponseEntity.ok(new MensagemResponse("Anuncio reativado com sucesso."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        anuncioService.excluir(id, usuarioAutenticadoService.getUsuarioId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PageResponse<AnuncioResumoResponse>> buscar(
            @RequestParam(value = "q", required = false) String termo,
            @RequestParam(value = "categoria", required = false) String categoria,
            @RequestParam(value = "precoMin", required = false) BigDecimal precoMin,
            @RequestParam(value = "precoMax", required = false) BigDecimal precoMax,
            @RequestParam(value = "cidade", required = false) String cidade,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "recente") String sort
    ) {
        return ResponseEntity.ok(anuncioService.buscarCatalogo(termo, categoria, precoMin, precoMax, cidade, page, size, sort));
    }

    @GetMapping("/principais")
    public ResponseEntity<List<AnuncioResumoResponse>> principais(@RequestParam(value = "limite", defaultValue = "10") int limite) {
        return ResponseEntity.ok(anuncioService.principais(limite));
    }

    @GetMapping("/meus")
    public ResponseEntity<List<AnuncioResumoResponse>> meus() {
        return ResponseEntity.ok(anuncioService.meusAnuncios(usuarioAutenticadoService.getUsuarioId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnuncioDetalheResponse> detalhar(@PathVariable Integer id, Authentication authentication) {
        Integer usuarioId = authentication != null && authentication.getPrincipal() instanceof UsuarioAutenticado usuario
                ? usuario.getId()
                : null;
        return ResponseEntity.ok(detalharAnuncioUseCase.executar(id, usuarioId));
    }

    @GetMapping("/{id}/disponibilidade")
    public ResponseEntity<DisponibilidadeResponse> disponibilidade(@PathVariable Integer id) {
        return ResponseEntity.ok(consultarDisponibilidadeAnuncioUseCase.executar(id));
    }

    @PostMapping("/{id}/reservas")
    public ResponseEntity<SolicitarAluguelResponse> solicitarAluguel(@PathVariable Integer id, @Valid @RequestBody SolicitarAluguelRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(solicitarAluguelUseCase.executar(id, usuarioAutenticadoService.getUsuarioId(), request));
    }
}
