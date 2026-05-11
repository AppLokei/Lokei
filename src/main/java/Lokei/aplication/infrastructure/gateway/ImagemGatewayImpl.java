package Lokei.aplication.infrastructure.gateway;

import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.gateway.ImagemGateway;
import Lokei.aplication.infrastructure.persistence.repository.ImagemEntityRepository;

import java.util.List;

public class ImagemGatewayImpl implements ImagemGateway {

    private final ImagemEntityRepository repository;

    public ImagemGatewayImpl(ImagemEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public Imagem salvarImagem(Imagem imagem) {
        return null;
    }

    @Override
    public List<Imagem> buscarImagensPorAnuncio(Long anuncioId) {
        return List.of();
    }

    @Override
    public void apagarImagem(Long id) {

    }
}
