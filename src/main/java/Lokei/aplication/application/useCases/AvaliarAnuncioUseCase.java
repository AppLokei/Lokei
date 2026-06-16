package Lokei.aplication.application.useCases;

import Lokei.aplication.application.dto.avaliacao.AvaliacaoResponse;
import Lokei.aplication.application.dto.avaliacao.CriarAvaliacaoAnuncioRequest;
import Lokei.aplication.application.service.AvaliacaoService;
import org.springframework.stereotype.Service;

@Service
public class AvaliarAnuncioUseCase {

    private final AvaliacaoService avaliacaoService;

    public AvaliarAnuncioUseCase(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    public AvaliacaoResponse avaliarAnuncio(Integer usuarioId, CriarAvaliacaoAnuncioRequest request) {
        return avaliacaoService.avaliarAnuncio(usuarioId, request);
    }
}
