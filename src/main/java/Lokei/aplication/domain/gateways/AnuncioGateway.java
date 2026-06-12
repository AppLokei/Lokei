package Lokei.aplication.domain.gateways;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.enums.CategoriaEnum;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Optional;

public interface AnuncioGateway {
    Anuncio criarAnuncio(Anuncio anuncio);
    Anuncio atualizarAnuncio(Anuncio anuncio);
    Optional<Anuncio> buscarAnuncioPorId(Long id);
    Page<Anuncio> buscarAnuncioPorUsuario(Long usuarioId, int pagina, int tamanho);

    Page<Anuncio> buscarAnunciosComFiltro(
            String titulo,
            CategoriaEnum categoria,
            BigDecimal valorMin,
            BigDecimal valorMax,
            int pagina,
            int tamanho
    );
}
