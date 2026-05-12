package Lokei.aplication.application.useCases;

import Lokei.aplication.application.dto.SolicitarAluguelRequest;
import Lokei.aplication.application.dto.SolicitarAluguelResponse;
import Lokei.aplication.infrastructure.persistence.entity.Aluguel;
import Lokei.aplication.infrastructure.persistence.entity.Anuncio;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import Lokei.aplication.infrastructure.persistence.enums.statusAnuncioEnum;
import Lokei.aplication.infrastructure.persistence.repository.AluguelRepository;
import Lokei.aplication.infrastructure.persistence.repository.AnuncioRepository;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import Lokei.aplication.infrastructure.shared.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitarAluguelUseCaseTest {

    @Mock
    private AluguelRepository aluguelRepository;
    @Mock
    private AnuncioRepository anuncioRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private SolicitarAluguelUseCase useCase;

    @Test
    void deveCriarReservaComStatusEmAprovacao() {
        Usuario proprietario = new Usuario();
        proprietario.setId(1);

        Usuario locatario = new Usuario();
        locatario.setId(2);

        Anuncio anuncio = new Anuncio();
        anuncio.setId(5);
        anuncio.setStatus(statusAnuncioEnum.ATIVO);
        anuncio.setValorDiario(BigDecimal.valueOf(50));
        anuncio.setProprietario(proprietario);

        LocalDate inicio = LocalDate.now().plusDays(1);
        LocalDate fim = inicio.plusDays(3);

        when(anuncioRepository.findById(5)).thenReturn(Optional.of(anuncio));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(locatario));
        when(aluguelRepository.existsReservaSobreposta(eq(5), anyCollection(), any(), any())).thenReturn(false);
        when(aluguelRepository.save(any(Aluguel.class))).thenAnswer(invocation -> {
            Aluguel aluguel = invocation.getArgument(0);
            aluguel.setId(99);
            return aluguel;
        });

        SolicitarAluguelResponse response = useCase.executar(
                5,
                new SolicitarAluguelRequest(2, inicio.toString(), fim.toString())
        );

        ArgumentCaptor<Aluguel> captor = ArgumentCaptor.forClass(Aluguel.class);
        verify(aluguelRepository).save(captor.capture());

        assertEquals(99, response.aluguelId());
        assertEquals("EM_APROVACAO", response.status());
        assertEquals(4, response.quantidadeDias());
        assertEquals(BigDecimal.valueOf(200), response.valorTotal());
        assertEquals(2, captor.getValue().getLocatario().getId());
    }

    @Test
    void deveImpedirReservaDoProprioAnuncio() {
        Usuario proprietario = new Usuario();
        proprietario.setId(7);

        Anuncio anuncio = new Anuncio();
        anuncio.setId(5);
        anuncio.setStatus(statusAnuncioEnum.ATIVO);
        anuncio.setValorDiario(BigDecimal.valueOf(50));
        anuncio.setProprietario(proprietario);

        when(anuncioRepository.findById(5)).thenReturn(Optional.of(anuncio));
        when(usuarioRepository.findById(7)).thenReturn(Optional.of(proprietario));

        LocalDate inicio = LocalDate.now().plusDays(1);

        assertThrows(
                RegraDeNegocioException.class,
                () -> useCase.executar(5, new SolicitarAluguelRequest(7, inicio.toString(), inicio.plusDays(1).toString()))
        );
    }

    @Test
    void deveImpedirReservaComPeriodoSobreposto() {
        Usuario proprietario = new Usuario();
        proprietario.setId(1);

        Usuario locatario = new Usuario();
        locatario.setId(2);

        Anuncio anuncio = new Anuncio();
        anuncio.setId(5);
        anuncio.setStatus(statusAnuncioEnum.ATIVO);
        anuncio.setValorDiario(BigDecimal.valueOf(50));
        anuncio.setProprietario(proprietario);

        LocalDate inicio = LocalDate.now().plusDays(2);
        LocalDate fim = inicio.plusDays(2);

        when(anuncioRepository.findById(5)).thenReturn(Optional.of(anuncio));
        when(usuarioRepository.findById(2)).thenReturn(Optional.of(locatario));
        when(aluguelRepository.existsReservaSobreposta(eq(5), anyCollection(), any(), any())).thenReturn(true);

        assertThrows(
                RegraDeNegocioException.class,
                () -> useCase.executar(5, new SolicitarAluguelRequest(2, inicio.toString(), fim.toString()))
        );
    }
}
