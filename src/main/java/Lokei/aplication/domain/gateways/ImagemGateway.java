package Lokei.aplication.domain.gateways;

import Lokei.aplication.domain.entities.Imagem;

import java.util.List;

public interface ImagemGateway {
    Imagem salvarImagem(Imagem imagem, Long anuncioId);
    List<Imagem> buscarImagensPorAnuncio(Long anuncioId);
    void deletarImagemPorAnuncio(Long id);
    void deletarImagemPorIdEAnuncioId(Long imagemId, Long anuncioId);
}
