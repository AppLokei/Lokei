package Lokei.aplication.application.usecases.imagem;

import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.gateways.ImagemGateway;

import java.util.List;

public class BuscarImagensPorAnuncioUseCase {
    private final ImagemGateway imagemGateway;

    public BuscarImagensPorAnuncioUseCase(ImagemGateway imagemGateway) {
        this.imagemGateway = imagemGateway;
    }

    public List<Imagem> execute(Long anuncioId){
        return imagemGateway.buscarImagensPorAnuncio(anuncioId);
    }
}
