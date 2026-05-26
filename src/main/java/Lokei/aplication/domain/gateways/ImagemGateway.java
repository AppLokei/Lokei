package Lokei.aplication.domain.gateways;

import Lokei.aplication.domain.entities.Imagem;

import java.util.List;
import java.util.Optional;

public interface ImagemGateway {
    Imagem salvarImagem(Imagem imagem, Long anuncioId);
    List<Imagem> buscarImagensPorAnuncio(Long anuncioId);
    Optional<Imagem> buscarImagemPorId(Long id);
    void apagarImagem(Long id);
}
