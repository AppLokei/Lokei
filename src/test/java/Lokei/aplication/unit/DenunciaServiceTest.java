package Lokei.aplication.unit;

import Lokei.aplication.application.dto.denuncia.CriarDenunciaRequest;
import Lokei.aplication.application.dto.denuncia.DenunciaResponse;
import Lokei.aplication.application.service.AnuncioService;
import Lokei.aplication.application.service.DenunciaService;
import Lokei.aplication.application.service.NotificacaoService;
import Lokei.aplication.infrastructure.persistence.entity.Anuncio;
import Lokei.aplication.infrastructure.persistence.entity.Denuncia;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import Lokei.aplication.infrastructure.persistence.enums.motivoDenunciaEnum;
import Lokei.aplication.infrastructure.persistence.enums.statusDenunciaEnum;
import Lokei.aplication.infrastructure.persistence.repository.DenunciaRepository;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import Lokei.aplication.infrastructure.shared.exception.RecursoNaoEncontradoException;
import Lokei.aplication.infrastructure.shared.exception.RegraDeNegocioException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

/**
 * Teste unitario da funcionalidade "criar denuncia de anuncio".
 * Isola o {@link DenunciaService} com mocks de todas as dependencias,
 * sem subir contexto Spring nem banco de dados.
 */
@ExtendWith(MockitoExtension.class)
class DenunciaServiceTest {

    @Mock
    private DenunciaRepository denunciaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AnuncioService anuncioService;

    @Mock
    private NotificacaoService notificacaoService;

    @InjectMocks
    private DenunciaService denunciaService;

    @Test
    void deveCriarDenunciaComStatusEmAnaliseEDescricaoNormalizada() {
        Usuario denunciante = new Usuario();
        denunciante.setId(10);
        denunciante.setNome("Joao Denunciante");

        Anuncio anuncio = new Anuncio();
        anuncio.setId(2);
        anuncio.setTitulo("Furadeira de Impacto");

        when(usuarioRepository.findById(10)).thenReturn(Optional.of(denunciante));
        when(anuncioService.buscarAnuncioDetalhado(2)).thenReturn(anuncio);
        when(denunciaRepository.save(any(Denuncia.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        CriarDenunciaRequest request =
                new CriarDenunciaRequest("ANUNCIO_FALSO", "   Ferramenta diferente da anunciada   ", null);

        DenunciaResponse resposta = denunciaService.criar(10, 2, request);

        assertThat(resposta.status()).isEqualTo("EM_ANALISE");
        assertThat(resposta.motivo()).isEqualTo("ANUNCIO_FALSO");
        assertThat(resposta.descricao()).isEqualTo("Ferramenta diferente da anunciada");
        assertThat(resposta.anuncioId()).isEqualTo(2);
        assertThat(resposta.tituloAnuncio()).isEqualTo("Furadeira de Impacto");
        assertThat(resposta.denunciante()).isEqualTo("Joao Denunciante");
        assertThat(resposta.imagens()).isEmpty();

        ArgumentCaptor<Denuncia> captor = ArgumentCaptor.forClass(Denuncia.class);
        verify(denunciaRepository).save(captor.capture());
        Denuncia persistida = captor.getValue();
        assertThat(persistida.getStatus()).isEqualTo(statusDenunciaEnum.EM_ANALISE);
        assertThat(persistida.getMotivo()).isEqualTo(motivoDenunciaEnum.ANUNCIO_FALSO);

        // A criacao da denuncia nao deve disparar notificacao (isso so ocorre na moderacao).
        verifyNoInteractions(notificacaoService);
    }

    @Test
    void deveRejeitarMotivoInvalido() {
        Usuario denunciante = new Usuario();
        denunciante.setId(10);

        Anuncio anuncio = new Anuncio();
        anuncio.setId(2);

        when(usuarioRepository.findById(10)).thenReturn(Optional.of(denunciante));
        when(anuncioService.buscarAnuncioDetalhado(2)).thenReturn(anuncio);

        CriarDenunciaRequest request =
                new CriarDenunciaRequest("MOTIVO_INEXISTENTE", "qualquer descricao", List.of());

        assertThatThrownBy(() -> denunciaService.criar(10, 2, request))
                .isInstanceOf(RegraDeNegocioException.class)
                .hasMessageContaining("Motivo");

        verify(denunciaRepository, never()).save(any());
    }

    @Test
    void deveFalharQuandoDenuncianteNaoExiste() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        CriarDenunciaRequest request =
                new CriarDenunciaRequest("ANUNCIO_FALSO", "descricao", null);

        assertThatThrownBy(() -> denunciaService.criar(99, 2, request))
                .isInstanceOf(RecursoNaoEncontradoException.class);

        verify(denunciaRepository, never()).save(any());
    }
}
