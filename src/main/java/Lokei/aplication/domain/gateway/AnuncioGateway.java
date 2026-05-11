package Lokei.aplication.domain.gateway;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.enums.CategoriaEnum;

import java.util.List;
import java.util.Optional;

public interface AnuncioGateway {
    Anuncio criarAnuncio(Anuncio anuncio);
    Anuncio atualizarAnuncio(Anuncio anuncio);
    void excluirAnuncio(Long id);
    List<Anuncio> buscarTodosAnuncios();
    Optional<Anuncio> buscarAnuncioPorId(Long id);
    List<Anuncio> buscarAnuncioPorCategoria(CategoriaEnum categoria);
}
