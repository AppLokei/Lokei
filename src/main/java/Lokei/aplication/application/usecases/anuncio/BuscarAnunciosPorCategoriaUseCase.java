package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.domain.gateways.AnuncioGateway;

import java.util.List;

public class BuscarAnunciosPorCategoriaUseCase {
    private final AnuncioGateway anuncioGateway;

    public BuscarAnunciosPorCategoriaUseCase(AnuncioGateway anuncioGateway) {
        this.anuncioGateway = anuncioGateway;
    }

    public List<Anuncio> execute(CategoriaEnum categoria) {
        return anuncioGateway.buscarAnuncioPorCategoria(categoria);
    }
}
