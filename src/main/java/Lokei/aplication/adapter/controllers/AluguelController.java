package Lokei.aplication.adapter.controllers;

import Lokei.aplication.adapter.dto.req.AtualizarStatusAluguelRequest;
import Lokei.aplication.adapter.dto.res.AluguelResponse;
import Lokei.aplication.adapter.mapper.AluguelControllerMapper;
import Lokei.aplication.application.usecases.aluguel.AtualizarStatusAluguelUseCase;
import Lokei.aplication.application.usecases.aluguel.BuscarAluguelPorUsuarioUseCase;
import Lokei.aplication.domain.entities.Aluguel;
import Lokei.aplication.domain.enums.StatusAluguelEnum;
import Lokei.aplication.domain.exceptions.RegraDeNegocioException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AluguelController {

    private final BuscarAluguelPorUsuarioUseCase buscarAluguelPorUsuarioUseCase;
    private final AtualizarStatusAluguelUseCase atualizarStatusAluguelUseCase;

    public AluguelController(BuscarAluguelPorUsuarioUseCase buscarAluguelPorUsuarioUseCase,
                            AtualizarStatusAluguelUseCase atualizarStatusAluguelUseCase) {
        this.buscarAluguelPorUsuarioUseCase = buscarAluguelPorUsuarioUseCase;
        this.atualizarStatusAluguelUseCase = atualizarStatusAluguelUseCase;
    }

    @GetMapping(value = "/alugueis-por-usuario")
    public ResponseEntity<Page<AluguelResponse>> buscarAlugueisPorUsuario(
            @RequestParam String identificador,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "12") int tamanho) {

        Page<Aluguel> alugueis = buscarAluguelPorUsuarioUseCase.buscarAluguelPorUsuario(identificador, pagina, tamanho);

        return ResponseEntity.ok(alugueis.map(AluguelControllerMapper::toResponse));
    }

    @PatchMapping(value = "/alugueis/{id}/status")
    public ResponseEntity<AluguelResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizarStatusAluguelRequest request) {

        StatusAluguelEnum novoStatus = parseStatus(request == null ? null : request.status());
        Aluguel atualizado = atualizarStatusAluguelUseCase.executar(id, novoStatus);

        return ResponseEntity.ok(AluguelControllerMapper.toResponse(atualizado));
    }

    private StatusAluguelEnum parseStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new RegraDeNegocioException("O status do aluguel é obrigatório.");
        }

        try {
            return StatusAluguelEnum.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new RegraDeNegocioException("Status de aluguel inválido: " + status + ".");
        }
    }
}
