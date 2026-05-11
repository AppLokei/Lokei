package Lokei.aplication.domain.gateway;

import Lokei.aplication.domain.entities.Imagem;

import java.util.List;

public interface ImagemGateway {
    Imagem salvarImagem(Imagem imagem);
    List<Imagem> buscarImagensPorAnuncio(Long anuncioId);
    void apagarImagem(Long id);
}
