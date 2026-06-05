package Lokei.aplication.domain.gateways;

import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

public interface ImagemGateway {
    Imagem salvarImagem(Imagem imagem, Long anuncioId) throws NotFoundException;
    Optional<Imagem> buscarImagemPorId(Long id);
    void apagarImagem(Long id);
}
