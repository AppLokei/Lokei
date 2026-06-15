package Lokei.aplication.application.usecases.aluguel;

import Lokei.aplication.domain.entities.Aluguel;
import Lokei.aplication.domain.enums.StatusAluguelEnum;
import Lokei.aplication.domain.exceptions.RecursoNaoEncontradoException;
import Lokei.aplication.domain.exceptions.RegraDeNegocioException;
import Lokei.aplication.domain.gateways.AluguelGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AtualizarStatusAluguelUseCaseTest {

    @Mock
    private AluguelGateway aluguelGateway;

    @InjectMocks
    private AtualizarStatusAluguelUseCase useCase;

    private Aluguel aluguelComStatus(StatusAluguelEnum status) {
        return new Aluguel(10L, new Date(), new Date(), BigDecimal.valueOf(100), status, 9L, 7L);
    }

    @Test
    void deveAprovarSolicitacaoEmAprovacao() {
        when(aluguelGateway.buscarPorId(10L)).thenReturn(Optional.of(aluguelComStatus(StatusAluguelEnum.EM_APROVACAO)));
        when(aluguelGateway.atualizarStatus(10L, StatusAluguelEnum.CONFIRMADO))
                .thenReturn(aluguelComStatus(StatusAluguelEnum.CONFIRMADO));

        Aluguel resultado = useCase.executar(10L, StatusAluguelEnum.CONFIRMADO);

        assertEquals(StatusAluguelEnum.CONFIRMADO, resultado.getStatus());
        verify(aluguelGateway).atualizarStatus(10L, StatusAluguelEnum.CONFIRMADO);
    }

    @Test
    void deveRejeitarTransicaoInvalida() {
        when(aluguelGateway.buscarPorId(10L)).thenReturn(Optional.of(aluguelComStatus(StatusAluguelEnum.CONCLUIDO)));

        assertThrows(RegraDeNegocioException.class,
                () -> useCase.executar(10L, StatusAluguelEnum.CONFIRMADO));

        verify(aluguelGateway, never()).atualizarStatus(eq(10L), org.mockito.ArgumentMatchers.any());
    }

    @Test
    void deveLancarQuandoAluguelNaoExiste() {
        when(aluguelGateway.buscarPorId(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class,
                () -> useCase.executar(99L, StatusAluguelEnum.CONFIRMADO));
    }
}
