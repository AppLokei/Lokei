package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.gateway.AnuncioGateway;

public class CriarAnuncioUseCase {

    private final AnuncioGateway anuncioGateway;

    public CriarAnuncioUseCase(AnuncioGateway anuncioGateway) {
        this.anuncioGateway = anuncioGateway;
    }

    public Anuncio execute(Anuncio anuncio) {
        return anuncioGateway.criarAnuncio(anuncio);
    }

}
