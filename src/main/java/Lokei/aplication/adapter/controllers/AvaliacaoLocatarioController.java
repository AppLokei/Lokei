package Lokei.aplication.adapter.controllers;

import Lokei.aplication.adapter.dto.req.AvaliacaoLocatarioRequest;
import Lokei.aplication.adapter.dto.res.AvaliacaoLocatarioResponse;
import Lokei.aplication.application.usecases.AvaliarLocatarioUseCase;
import Lokei.aplication.domain.entities.AvaliacaoLocatario;
import Lokei.aplication.domain.gateways.AvaliacaoLocatarioGateway;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/avaliacoes-locatarios")
public class AvaliacaoLocatarioController {

    private final AvaliarLocatarioUseCase avaliarLocatarioUseCase;
    private final AvaliacaoLocatarioGateway avaliacaoGateway;

    public AvaliacaoLocatarioController(AvaliarLocatarioUseCase avaliarLocatarioUseCase, AvaliacaoLocatarioGateway avaliacaoGateway) {
        this.avaliarLocatarioUseCase = avaliarLocatarioUseCase;
        this.avaliacaoGateway = avaliacaoGateway;
    }

    @PostMapping
    public ResponseEntity<AvaliacaoLocatarioResponse> avaliar(@RequestBody AvaliacaoLocatarioRequest request) {
        AvaliacaoLocatario avaliacao = avaliarLocatarioUseCase.avaliar(
                request.getAluguelId(),
                request.getAvaliadorId(),
                request.getNota(),
                request.getComentario()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(new AvaliacaoLocatarioResponse(avaliacao));
    }

    @GetMapping("/usuarios/{avaliadoId}")
    public ResponseEntity<Page<AvaliacaoLocatarioResponse>> listarPorAvaliado(
            @PathVariable Long avaliadoId,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        Page<AvaliacaoLocatario> avaliacoes = avaliacaoGateway.buscarPorAvaliado(avaliadoId, pagina, tamanho);
        return ResponseEntity.ok(avaliacoes.map(AvaliacaoLocatarioResponse::new));
    }
}
