package Lokei.aplication.application.usecases.imagem;

import Lokei.aplication.domain.gateways.ImagemGateway;

public class DeletarImagemPorAnuncioUseCase {
    private final ImagemGateway imagemGateway;

    public DeletarImagemPorAnuncioUseCase(ImagemGateway imagemGateway){
        this.imagemGateway = imagemGateway;
    }

    public void execute(Long id) {
        imagemGateway.deletarImagemPorAnuncio(id);
    }
}
