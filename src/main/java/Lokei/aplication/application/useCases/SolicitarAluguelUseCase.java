package Lokei.aplication.application.useCases;

import Lokei.aplication.application.dto.aluguel.SolicitarAluguelRequest;
import Lokei.aplication.application.dto.aluguel.SolicitarAluguelResponse;
import Lokei.aplication.application.service.AluguelService;
import org.springframework.stereotype.Service;

@Service
public class SolicitarAluguelUseCase {

    private final AluguelService aluguelService;

    public SolicitarAluguelUseCase(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    public SolicitarAluguelResponse executar(Integer anuncioId, Integer usuarioId, SolicitarAluguelRequest request) {
        return aluguelService.solicitar(anuncioId, usuarioId, request);
    }
}
