package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.gateway.AnuncioGateway;

import java.util.List;

public class BuscarTodosAnunciosUseCase {

    private final AnuncioGateway anuncioGateway;

    public BuscarTodosAnunciosUseCase(AnuncioGateway anuncioGateway) {
        this.anuncioGateway = anuncioGateway;
    }

    public List<Anuncio> execute() {
        return anuncioGateway.buscarTodosAnuncios();
    }
}
