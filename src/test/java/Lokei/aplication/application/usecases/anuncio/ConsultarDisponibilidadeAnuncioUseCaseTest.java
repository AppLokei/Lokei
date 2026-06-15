package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.application.dto.DisponibilidadeResponse;
import Lokei.aplication.domain.entities.Aluguel;
import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Ferramenta;
import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.domain.enums.StatusAluguelEnum;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;
import Lokei.aplication.domain.exceptions.RecursoNaoEncontradoException;
import Lokei.aplication.domain.gateways.AluguelGateway;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarDisponibilidadeAnuncioUseCaseTest {

    @Mock
    private AnuncioGateway anuncioGateway;
    @Mock
    private AluguelGateway aluguelGateway;

    @InjectMocks
    private ConsultarDisponibilidadeAnuncioUseCase useCase;

    @Test
    void deveListarPeriodosReservadosDeUmAnuncioAtivo() {
        Anuncio anuncio = new Anuncio(
                9L,
                "Furadeira",
                "Descricao",
                BigDecimal.valueOf(30),
                StatusAnuncioEnum.ATIVO,
                new Ferramenta(5L, "Furadeira", CategoriaEnum.FURADEIRAS_E_PARAFUSADEIRAS),
                List.of(new Imagem(1L, "url-1", "public-1")),
                3L
        );

        Aluguel aluguel = new Aluguel(
                50L,
                converter(LocalDate.of(2026, 7, 1)),
                converter(LocalDate.of(2026, 7, 5)),
                BigDecimal.valueOf(150),
                StatusAluguelEnum.CONFIRMADO,
                9L,
                7L
        );

        when(anuncioGateway.buscarAnuncioPorId(9L)).thenReturn(Optional.of(anuncio));
        when(aluguelGateway.buscarReservasPorAnuncioEStatus(eq(9L), any())).thenReturn(List.of(aluguel));

        DisponibilidadeResponse response = useCase.executar(9L);

        assertTrue(response.disponivelParaNovasReservas());
        assertEquals("ATIVO", response.statusAnuncio());
        assertEquals(1, response.periodosReservados().size());
        assertEquals("2026-07-01", response.periodosReservados().get(0).dataInicio());
        assertEquals("2026-07-05", response.periodosReservados().get(0).dataFim());
        assertEquals("CONFIRMADO", response.periodosReservados().get(0).status());
    }

    @Test
    void deveLancarExcecaoQuandoAnuncioNaoExiste() {
        when(anuncioGateway.buscarAnuncioPorId(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> useCase.executar(99L));
    }

    private Date converter(LocalDate data) {
        return Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
