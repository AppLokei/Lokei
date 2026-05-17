package Lokei.aplication.domain.gateways;

import Lokei.aplication.domain.entities.Imagem;

import java.util.List;

public interface ImagemGateway {
    Imagem salvarImagem(Imagem imagem, Long anuncioId);
    List<Imagem> buscarImagensPorAnuncio(Long anuncioId);
    void apagarImagem(Long id);
}
