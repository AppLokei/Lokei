package Lokei.aplication.application.useCases;

import Lokei.aplication.application.dto.DisponibilidadeResponse;
import Lokei.aplication.infrastructure.persistence.entity.Aluguel;
import Lokei.aplication.infrastructure.persistence.entity.Anuncio;
import Lokei.aplication.infrastructure.persistence.enums.statusAluguelEnum;
import Lokei.aplication.infrastructure.persistence.enums.statusAnuncioEnum;
import Lokei.aplication.infrastructure.persistence.repository.AluguelRepository;
import Lokei.aplication.infrastructure.persistence.repository.AnuncioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarDisponibilidadeAnuncioUseCaseTest {

    @Mock
    private AnuncioRepository anuncioRepository;
    @Mock
    private AluguelRepository aluguelRepository;

    @InjectMocks
    private ConsultarDisponibilidadeAnuncioUseCase useCase;

    @Test
    void deveRetornarPeriodosReservadosOrdenados() {
        Anuncio anuncio = new Anuncio();
        anuncio.setId(10);
        anuncio.setStatus(statusAnuncioEnum.ATIVO);

        Aluguel primeiro = new Aluguel();
        primeiro.setId(1);
        primeiro.setDataInicio(LocalDate.of(2026, 5, 20));
        primeiro.setDataFim(LocalDate.of(2026, 5, 22));
        primeiro.setStatusAluguel(statusAluguelEnum.EM_APROVACAO);

        Aluguel segundo = new Aluguel();
        segundo.setId(2);
        segundo.setDataInicio(LocalDate.of(2026, 5, 25));
        segundo.setDataFim(LocalDate.of(2026, 5, 27));
        segundo.setStatusAluguel(statusAluguelEnum.CONFIRMADO);

        when(anuncioRepository.findById(10)).thenReturn(Optional.of(anuncio));
        when(aluguelRepository.findByAnuncio_IdAndStatusAluguelInOrderByDataInicioAsc(
                org.mockito.ArgumentMatchers.eq(10),
                org.mockito.ArgumentMatchers.anyCollection()
        )).thenReturn(List.of(primeiro, segundo));

        DisponibilidadeResponse response = useCase.executar(10);

        assertTrue(response.disponivelParaNovasReservas());
        assertEquals(2, response.periodosReservados().size());
        assertEquals("2026-05-20", response.periodosReservados().get(0).dataInicio());
        assertEquals("CONFIRMADO", response.periodosReservados().get(1).status());
    }
}
