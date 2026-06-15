package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.application.dto.AnuncioDetalheResponse;
import Lokei.aplication.application.usecases.avaliacao.ListarAvaliacaoAnuncioUseCase;
import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Ferramenta;
import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.entities.Usuario;
import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import Lokei.aplication.infrastructure.persistence.entities.AvaliacaoEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DetalharAnuncioUseCaseTest {

    @Mock
    private AnuncioGateway anuncioGateway;
    @Mock
    private UsuarioGateway usuarioGateway;
    @Mock
    private ListarAvaliacaoAnuncioUseCase listarAvaliacaoAnuncioUseCase;

    @InjectMocks
    private DetalharAnuncioUseCase useCase;

    @Test
    void deveMontarDetalheDoAnuncioComAcaoDeEdicaoParaProprietario() {
        Imagem imagem1 = new Imagem(2L, "url-2", "public-2");
        Imagem imagem2 = new Imagem(1L, "url-1", "public-1");

        Anuncio anuncio = new Anuncio(
                9L,
                "Furadeira Bosch",
                "500W",
                BigDecimal.valueOf(45),
                StatusAnuncioEnum.ATIVO,
                new Ferramenta(5L, "Furadeira", CategoriaEnum.FURADEIRAS_E_PARAFUSADEIRAS),
                List.of(imagem1, imagem2),
                3L
        );

        Usuario proprietario = new Usuario(3L, "Maria", "maria@email.com", "12345678901", "71999998888", "senha");

        AvaliacaoEntity avaliacao = new AvaliacaoEntity();
        avaliacao.setId(1);
        avaliacao.setNota(5);
        avaliacao.setComentario("Excelente.");
        avaliacao.setDataCriacao(new Date());

        when(anuncioGateway.buscarAnuncioPorId(9L)).thenReturn(Optional.of(anuncio));
        when(usuarioGateway.buscarUsuarioPorId(3L)).thenReturn(Optional.of(proprietario));
        when(listarAvaliacaoAnuncioUseCase.listarAvaliacoes(9L)).thenReturn(List.of(avaliacao));

        AnuncioDetalheResponse response = useCase.executar(9L, 3L);

        assertEquals(3L, response.usuarioId());
        assertEquals("EDITAR_ANUNCIO", response.acaoPrimaria());
        assertFalse(response.disponivelParaReserva());
        assertEquals("url-1", response.imagens().get(0));
        assertEquals("FURADEIRAS_E_PARAFUSADEIRAS", response.categoria());
        assertEquals("m***@email.com", response.proprietario().emailMascarado());
        assertEquals("***-***-8888", response.proprietario().telefoneMascarado());
        assertEquals(BigDecimal.valueOf(5).setScale(2), response.notaMedia());
    }

    @Test
    void devePermitirReservaParaUsuarioDiferenteDoProprietario() {
        Anuncio anuncio = new Anuncio(
                9L,
                "Furadeira Bosch",
                "500W",
                BigDecimal.valueOf(45),
                StatusAnuncioEnum.ATIVO,
                new Ferramenta(5L, "Furadeira", CategoriaEnum.FURADEIRAS_E_PARAFUSADEIRAS),
                List.of(new Imagem(1L, "url-1", "public-1")),
                3L
        );

        when(anuncioGateway.buscarAnuncioPorId(9L)).thenReturn(Optional.of(anuncio));
        when(usuarioGateway.buscarUsuarioPorId(3L)).thenReturn(Optional.of(
                new Usuario(3L, "Maria", "maria@email.com", "12345678901", "71999998888", "senha")));
        when(listarAvaliacaoAnuncioUseCase.listarAvaliacoes(9L)).thenReturn(List.of());

        AnuncioDetalheResponse response = useCase.executar(9L, 7L);

        assertEquals("SOLICITAR_ALUGUEL", response.acaoPrimaria());
        assertEquals(BigDecimal.ZERO, response.notaMedia());
    }
}
