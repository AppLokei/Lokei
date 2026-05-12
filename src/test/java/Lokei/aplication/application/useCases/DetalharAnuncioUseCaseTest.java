package Lokei.aplication.application.useCases;

import Lokei.aplication.application.dto.AnuncioDetalheResponse;
import Lokei.aplication.infrastructure.persistence.entity.Anuncio;
import Lokei.aplication.infrastructure.persistence.entity.Avaliacao;
import Lokei.aplication.infrastructure.persistence.entity.Imagem;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import Lokei.aplication.infrastructure.persistence.enums.categoriaEnum;
import Lokei.aplication.infrastructure.persistence.enums.statusAnuncioEnum;
import Lokei.aplication.infrastructure.persistence.repository.AnuncioRepository;
import Lokei.aplication.infrastructure.persistence.repository.AvaliacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DetalharAnuncioUseCaseTest {

    @Mock
    private AnuncioRepository anuncioRepository;
    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    @InjectMocks
    private DetalharAnuncioUseCase useCase;

    @Test
    void deveMontarDetalheDoAnuncioComAcaoDeEdicaoParaProprietario() {
        Usuario proprietario = new Usuario();
        proprietario.setId(3);
        proprietario.setNome("Maria");
        proprietario.setEmail("maria@email.com");
        proprietario.setTelefone("71999998888");

        Imagem imagem1 = new Imagem();
        imagem1.setId(2);
        imagem1.setImagemUrl("url-2");

        Imagem imagem2 = new Imagem();
        imagem2.setId(1);
        imagem2.setImagemUrl("url-1");

        Anuncio anuncio = new Anuncio();
        anuncio.setId(9);
        anuncio.setTitulo("Furadeira Bosch");
        anuncio.setDescricao("500W");
        anuncio.setValorDiario(BigDecimal.valueOf(45));
        anuncio.setCategoria(categoriaEnum.Furadeiras_e_Parafusadeiras);
        anuncio.setStatus(statusAnuncioEnum.ATIVO);
        anuncio.setProprietario(proprietario);
        anuncio.setImagens(new LinkedHashSet<>(List.of(imagem1, imagem2)));

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setId(1);
        avaliacao.setNota(5);
        avaliacao.setComentario("Excelente.");
        avaliacao.setDataCriacao(LocalDateTime.now());

        when(anuncioRepository.findById(9)).thenReturn(Optional.of(anuncio));
        when(avaliacaoRepository.findByAluguel_Anuncio_IdOrderByDataCriacaoDesc(9)).thenReturn(List.of(avaliacao));

        AnuncioDetalheResponse response = useCase.executar(9, 3);

        assertEquals("EDITAR_ANUNCIO", response.acaoPrimaria());
        assertFalse(response.disponivelParaReserva());
        assertEquals("url-1", response.imagens().get(0));
        assertEquals("m***@email.com", response.proprietario().emailMascarado());
        assertEquals("***-***-8888", response.proprietario().telefoneMascarado());
        assertEquals(BigDecimal.valueOf(5).setScale(2), response.notaMedia());
    }
}
