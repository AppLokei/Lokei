package Lokei.aplication.application.usecases.avaliacao;

import Lokei.aplication.domain.entities.Aluguel;
import Lokei.aplication.domain.entities.AvaliacaoLocatario;
import Lokei.aplication.domain.enums.StatusAluguelEnum;
import Lokei.aplication.domain.exceptions.AvaliacaoInvalidaException;
import Lokei.aplication.domain.gateways.AluguelGateway;
import Lokei.aplication.domain.gateways.AvaliacaoLocatarioGateway;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.entities.Anuncio;

public class AvaliarLocatarioUseCase {

    private final AvaliacaoLocatarioGateway avaliacaoGateway;
    private final AluguelGateway aluguelGateway;
    private final AnuncioGateway anuncioGateway;

    public AvaliarLocatarioUseCase(AvaliacaoLocatarioGateway avaliacaoGateway, AluguelGateway aluguelGateway, AnuncioGateway anuncioGateway) {
        this.avaliacaoGateway = avaliacaoGateway;
        this.aluguelGateway = aluguelGateway;
        this.anuncioGateway = anuncioGateway;
    }

    public AvaliacaoLocatario avaliar(Long aluguelId, Long avaliadorId, Integer nota, String comentario) {
        Aluguel aluguel = aluguelGateway.buscarPorId(aluguelId)
                .orElseThrow(() -> new AvaliacaoInvalidaException("Aluguel não encontrado."));

        if (!StatusAluguelEnum.CONCLUIDO.equals(aluguel.getStatus())) {
            throw new AvaliacaoInvalidaException("Apenas aluguéis concluídos podem ser avaliados.");
        }

        Anuncio anuncio = anuncioGateway.buscarAnuncioPorId(aluguel.getAnuncioId())
                .orElseThrow(() -> new AvaliacaoInvalidaException("Anúncio não encontrado."));

        if (!avaliadorId.equals(anuncio.getUsuarioId())) {
             throw new AvaliacaoInvalidaException("Apenas o locador (dono da ferramenta) pode avaliar o locatário.");
        }

        if (avaliacaoGateway.existeAvaliacaoParaAluguel(aluguelId, avaliadorId)) {
            throw new AvaliacaoInvalidaException("Você já enviou uma avaliação para este aluguel.");
        }

        AvaliacaoLocatario avaliacao = new AvaliacaoLocatario(
                null, nota, comentario, null, aluguelId, avaliadorId, aluguel.getLocatarioId()
        );

        return avaliacaoGateway.salvar(avaliacao);
    }
}
