package Lokei.aplication.domain.gateways;

import Lokei.aplication.domain.entities.AvaliacaoLocatario;
import org.springframework.data.domain.Page;

public interface AvaliacaoLocatarioGateway {
    AvaliacaoLocatario salvar(AvaliacaoLocatario avaliacao);
    boolean existeAvaliacaoParaAluguel(Long aluguelId, Long avaliadorId);
    Page<AvaliacaoLocatario> buscarPorAvaliado(Long avaliadoId, int pagina, int tamanho);
}
