package Lokei.aplication.application.usecases.aluguel;

import Lokei.aplication.domain.entities.Aluguel;
import Lokei.aplication.domain.enums.StatusAluguelEnum;
import Lokei.aplication.domain.exceptions.RecursoNaoEncontradoException;
import Lokei.aplication.domain.exceptions.RegraDeNegocioException;
import Lokei.aplication.domain.gateways.AluguelGateway;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class AtualizarStatusAluguelUseCase {

    private static final Map<StatusAluguelEnum, Set<StatusAluguelEnum>> TRANSICOES_PERMITIDAS = Map.of(
            StatusAluguelEnum.EM_APROVACAO, Set.of(
                    StatusAluguelEnum.CONFIRMADO,
                    StatusAluguelEnum.REPROVADO,
                    StatusAluguelEnum.CANCELADO
            ),
            StatusAluguelEnum.CONFIRMADO, Set.of(
                    StatusAluguelEnum.EM_ANDAMENTO,
                    StatusAluguelEnum.CANCELADO
            ),
            StatusAluguelEnum.EM_ANDAMENTO, Set.of(
                    StatusAluguelEnum.CONCLUIDO
            )
    );

    private final AluguelGateway aluguelGateway;

    public AtualizarStatusAluguelUseCase(AluguelGateway aluguelGateway) {
        this.aluguelGateway = aluguelGateway;
    }

    public Aluguel executar(Long aluguelId, StatusAluguelEnum novoStatus) {
        if (novoStatus == null) {
            throw new RegraDeNegocioException("O novo status do aluguel é obrigatório.");
        }

        Aluguel aluguel = aluguelGateway.buscarPorId(aluguelId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluguel não encontrado."));

        validarTransicao(aluguel.getStatus(), novoStatus);

        return aluguelGateway.atualizarStatus(aluguelId, novoStatus);
    }

    private void validarTransicao(StatusAluguelEnum statusAtual, StatusAluguelEnum novoStatus) {
        if (statusAtual == novoStatus) {
            throw new RegraDeNegocioException("O aluguel já está no status informado.");
        }

        Set<StatusAluguelEnum> permitidos = TRANSICOES_PERMITIDAS.getOrDefault(statusAtual, Set.of());

        if (!permitidos.contains(novoStatus)) {
            throw new RegraDeNegocioException(
                    "Transição de status inválida: de " + statusAtual + " para " + novoStatus + ".");
        }
    }
}
