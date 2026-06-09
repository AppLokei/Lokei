package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Ferramenta;
import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.entities.Usuario;
import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;
import Lokei.aplication.domain.exceptions.UsuarioNotFoundException;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriarAnuncioUseCaseTest {

    @Mock
    private AnuncioGateway anuncioGateway;

    @Mock
    private UsuarioGateway usuarioGateway;

    @InjectMocks
    private CriarAnuncioUseCase criarAnuncioUseCase;

    @Test
    void deveCriarAnuncioComSucesso() {
        Usuario usuario = new Usuario();

        Ferramenta ferramenta = new Ferramenta(null, "Martelo", CategoriaEnum.MARTELOS);
        Imagem imagem = new Imagem(null, "url", "publicId");
        Anuncio anuncio = new Anuncio(null, "Martelo", "Descrição", BigDecimal.valueOf(20), StatusAnuncioEnum.ATIVO, ferramenta, List.of(imagem), 1L);

        when(usuarioGateway.buscarUsuarioPorId(1L)).thenReturn(Optional.of(usuario));
        when(anuncioGateway.criarAnuncio(any())).thenReturn(anuncio);

        Anuncio resultado = criarAnuncioUseCase.execute(anuncio);

        assertNotNull(resultado);
        verify(anuncioGateway).criarAnuncio(any());
    }

    @Test
    void naoDeveCriarAnuncioComUsuarioInexistente() {
        Ferramenta ferramenta = new Ferramenta(null, "Martelo", CategoriaEnum.MARTELOS);
        Imagem imagem = new Imagem(null, "url", "publicId");
        Anuncio anuncio = new Anuncio(null, "Martelo", "Descrição", BigDecimal.valueOf(20), StatusAnuncioEnum.ATIVO, ferramenta, List.of(imagem), 1L);

        when(usuarioGateway.buscarUsuarioPorId(1L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNotFoundException.class, () -> criarAnuncioUseCase.execute(anuncio));
    }
}