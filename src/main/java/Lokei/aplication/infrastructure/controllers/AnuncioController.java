package Lokei.aplication.infrastructure.controllers;

import Lokei.aplication.application.dto.AnuncioDetalheResponse;
import Lokei.aplication.application.dto.DisponibilidadeResponse;
import Lokei.aplication.application.dto.SolicitarAluguelRequest;
import Lokei.aplication.application.dto.SolicitarAluguelResponse;
import Lokei.aplication.application.useCases.ConsultarDisponibilidadeAnuncioUseCase;
import Lokei.aplication.application.useCases.DetalharAnuncioUseCase;
import Lokei.aplication.application.useCases.SolicitarAluguelUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/anuncios")
public class AnuncioController {

    private final DetalharAnuncioUseCase detalharAnuncioUseCase;
    private final ConsultarDisponibilidadeAnuncioUseCase consultarDisponibilidadeAnuncioUseCase;
    private final SolicitarAluguelUseCase solicitarAluguelUseCase;

    public AnuncioController(
            DetalharAnuncioUseCase detalharAnuncioUseCase,
            ConsultarDisponibilidadeAnuncioUseCase consultarDisponibilidadeAnuncioUseCase,
            SolicitarAluguelUseCase solicitarAluguelUseCase
    ) {
        this.detalharAnuncioUseCase = detalharAnuncioUseCase;
        this.consultarDisponibilidadeAnuncioUseCase = consultarDisponibilidadeAnuncioUseCase;
        this.solicitarAluguelUseCase = solicitarAluguelUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnuncioDetalheResponse> detalhar(
            @PathVariable("id") Integer anuncioId,
            @RequestParam(value = "usuarioId", required = false) Integer usuarioId
    ) {
        return ResponseEntity.ok(detalharAnuncioUseCase.executar(anuncioId, usuarioId));
    }

    @GetMapping("/{id}/disponibilidade")
    public ResponseEntity<DisponibilidadeResponse> consultarDisponibilidade(@PathVariable("id") Integer anuncioId) {
        return ResponseEntity.ok(consultarDisponibilidadeAnuncioUseCase.executar(anuncioId));
    }

    @PostMapping("/{id}/reservas")
    public ResponseEntity<SolicitarAluguelResponse> solicitarAluguel(
            @PathVariable("id") Integer anuncioId,
            @RequestBody SolicitarAluguelRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitarAluguelUseCase.executar(anuncioId, request));
    }
}
