package Lokei.aplication.application.usecases.aluguel;

import Lokei.aplication.application.dto.SolicitarAluguelRequest;
import Lokei.aplication.application.dto.SolicitarAluguelResponse;
import Lokei.aplication.domain.entities.Aluguel;
import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Ferramenta;
import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.entities.Usuario;
import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.domain.enums.StatusAluguelEnum;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;
import Lokei.aplication.domain.exceptions.RegraDeNegocioException;
import Lokei.aplication.domain.gateways.AluguelGateway;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitarAluguelUseCaseTest {

    @Mock
    private AluguelGateway aluguelGateway;
    @Mock
    private AnuncioGateway anuncioGateway;
    @Mock
    private UsuarioGateway usuarioGateway;

    @InjectMocks
    private SolicitarAluguelUseCase useCase;

    private Anuncio anuncioAtivo(Long donoId) {
        return new Anuncio(
                9L,
                "Furadeira",
                "Descricao",
                BigDecimal.valueOf(45),
                StatusAnuncioEnum.ATIVO,
                new Ferramenta(5L, "Furadeira", CategoriaEnum.FURADEIRAS_E_PARAFUSADEIRAS),
                List.of(new Imagem(1L, "url-1", "public-1")),
                donoId
        );
    }

    @Test
    void deveCriarSolicitacaoDeAluguelComSucesso() {
        LocalDate inicio = LocalDate.now().plusDays(1);
        LocalDate fim = LocalDate.now().plusDays(3);

        SolicitarAluguelRequest request = new SolicitarAluguelRequest(7L, inicio.toString(), fim.toString());

        when(anuncioGateway.buscarAnuncioPorId(9L)).thenReturn(Optional.of(anuncioAtivo(3L)));
        when(usuarioGateway.buscarUsuarioPorId(7L)).thenReturn(Optional.of(
                new Usuario(7L, "Joao", "joao@email.com", "98765432100", "71988887777", "senha")));
        when(aluguelGateway.existeReservaSobreposta(eq(9L), anyCollection(), any(Date.class), any(Date.class)))
                .thenReturn(false);
        when(aluguelGateway.salvar(any(Aluguel.class), eq(9L), eq(7L))).thenAnswer(invocation -> {
            Aluguel original = invocation.getArgument(0);
            return new Aluguel(100L, original.getDataInicio(), original.getDataFim(),
                    original.getValorTotal(), original.getStatus(), 9L, 7L);
        });

        SolicitarAluguelResponse response = useCase.executar(9L, request);

        assertEquals(100L, response.aluguelId());
        assertEquals("EM_APROVACAO", response.status());
        assertEquals(3L, response.quantidadeDias());
        assertEquals(BigDecimal.valueOf(135), response.valorTotal());
    }

    @Test
    void deveRejeitarQuandoProprietarioTentaAlugarProprioAnuncio() {
        LocalDate inicio = LocalDate.now().plusDays(1);
        LocalDate fim = LocalDate.now().plusDays(2);

        SolicitarAluguelRequest request = new SolicitarAluguelRequest(3L, inicio.toString(), fim.toString());

        when(anuncioGateway.buscarAnuncioPorId(9L)).thenReturn(Optional.of(anuncioAtivo(3L)));
        when(usuarioGateway.buscarUsuarioPorId(3L)).thenReturn(Optional.of(
                new Usuario(3L, "Maria", "maria@email.com", "12345678901", "71999998888", "senha")));

        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(9L, request));
    }

    @Test
    void deveRejeitarPeriodoSuperiorA30Dias() {
        LocalDate inicio = LocalDate.now().plusDays(1);
        LocalDate fim = LocalDate.now().plusDays(40);

        SolicitarAluguelRequest request = new SolicitarAluguelRequest(7L, inicio.toString(), fim.toString());

        assertThrows(RegraDeNegocioException.class, () -> useCase.executar(9L, request));
    }
}
