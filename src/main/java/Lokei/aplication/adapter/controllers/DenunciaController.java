package Lokei.aplication.adapter.controllers;

import Lokei.aplication.adapter.dto.req.DenunciaRequest;
import Lokei.aplication.adapter.dto.res.DenunciaResponse;
import Lokei.aplication.adapter.mapper.DenunciaControllerMapper;
import Lokei.aplication.application.usecases.denuncia.*;
import Lokei.aplication.domain.entities.Denuncia;
import Lokei.aplication.domain.enums.StatusDenunciaEnum;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações de Denúncia.
 *
 * Endpoints:
 *   POST   /anuncios/{anuncioId}/denuncias         — Usuário denuncia um anúncio (RN-034)
 *   PATCH  /denuncias/{id}/aprovar                 — Admin aprova denúncia (RN-035)
 *   PATCH  /denuncias/{id}/recusar                 — Admin recusa denúncia (RN-035)
 *   GET    /denuncias?status=PENDENTE              — Admin lista denúncias por status
 */
@RestController
public class DenunciaController {

    private final EnviarDenunciaUseCase enviarDenunciaUseCase;
    private final AprovarDenunciaUseCase aprovarDenunciaUseCase;
    private final RecusarDenunciaUseCase recusarDenunciaUseCase;
    private final ListarDenunciasUseCase listarDenunciasUseCase;

    public DenunciaController(EnviarDenunciaUseCase enviarDenunciaUseCase,
                               AprovarDenunciaUseCase aprovarDenunciaUseCase,
                               RecusarDenunciaUseCase recusarDenunciaUseCase,
                               ListarDenunciasUseCase listarDenunciasUseCase) {
        this.enviarDenunciaUseCase = enviarDenunciaUseCase;
        this.aprovarDenunciaUseCase = aprovarDenunciaUseCase;
        this.recusarDenunciaUseCase = recusarDenunciaUseCase;
        this.listarDenunciasUseCase = listarDenunciasUseCase;
    }

    /**
     * Usuário envia uma denúncia sobre um anúncio específico.
     * RN-034: motivo deve ser um valor válido do enum MotivoDenunciaEnum.
     */
    @PostMapping("/anuncios/{anuncioId}/denuncias")
    public ResponseEntity<DenunciaResponse> denunciar(
            @PathVariable Long anuncioId,
            @RequestBody @Valid DenunciaRequest request) {

        Denuncia denuncia = enviarDenunciaUseCase.execute(
                anuncioId,
                request.denuncianteId(),   // TODO: substituir por autenticação
                request.motivo(),
                request.descricao()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DenunciaControllerMapper.toResponse(denuncia));
    }

    /**
     * Administrador aprova uma denúncia.
     * RN-035: o anúncio é desativado e o locador é notificado.
     */
    @PatchMapping("/denuncias/{id}/aprovar")
    public ResponseEntity<DenunciaResponse> aprovar(@PathVariable Long id) {
        Denuncia denuncia = aprovarDenunciaUseCase.execute(id);
        return ResponseEntity.ok(DenunciaControllerMapper.toResponse(denuncia));
    }

    /**
     * Administrador recusa uma denúncia.
     * O anúncio permanece no status atual.
     */
    @PatchMapping("/denuncias/{id}/recusar")
    public ResponseEntity<DenunciaResponse> recusar(@PathVariable Long id) {
        Denuncia denuncia = recusarDenunciaUseCase.execute(id);
        return ResponseEntity.ok(DenunciaControllerMapper.toResponse(denuncia));
    }

    /**
     * Administrador lista denúncias filtradas por status.
     */
    @GetMapping("/denuncias")
    public ResponseEntity<List<DenunciaResponse>> listar(
            @RequestParam(defaultValue = "PENDENTE") StatusDenunciaEnum status) {

        List<DenunciaResponse> respostas = listarDenunciasUseCase.execute(status)
                .stream()
                .map(DenunciaControllerMapper::toResponse)
                .toList();

        return ResponseEntity.ok(respostas);
    }
}
