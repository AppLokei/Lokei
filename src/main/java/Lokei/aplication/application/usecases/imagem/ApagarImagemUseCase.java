package Lokei.aplication.application.usecases.imagem;

import Lokei.aplication.domain.gateway.ImagemGateway;

public class ApagarImagemUseCase {
    private final ImagemGateway imagemGateway;

    public ApagarImagemUseCase(ImagemGateway imagemGateway){
        this.imagemGateway = imagemGateway;
    }

    public void execute(Long id) {
        imagemGateway.apagarImagem(id);
    }
}
