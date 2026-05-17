package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.domain.gateways.AnuncioGateway;

public class ExcluirAnuncioUseCase {
    private final AnuncioGateway anuncioGateway;

    public ExcluirAnuncioUseCase(AnuncioGateway anuncioGateway) {
        this.anuncioGateway = anuncioGateway;
    }
    public void execute(Long id) {
        anuncioGateway.excluirAnuncio(id);
    }
}
