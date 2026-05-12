package Lokei.aplication.application.usecases.imagem;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.gateway.ImagemGateway;

public class SalvarImagemUseCase {

    private final ImagemGateway imagemGateway;

    public SalvarImagemUseCase(ImagemGateway imagemGateway) {
        this.imagemGateway = imagemGateway;
    }

    public Imagem execute(Imagem imagem, Long anuncioId){
        return imagemGateway.salvarImagem(imagem, anuncioId);
    }
}
