package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.exceptions.AnuncioNotFoundException;
import Lokei.aplication.domain.gateway.AnuncioGateway;

import java.util.Optional;

public class BuscarAnuncioPorIdUseCase {
    private final AnuncioGateway anuncioGateway;

    public BuscarAnuncioPorIdUseCase(AnuncioGateway anuncioGateway) {
        this.anuncioGateway = anuncioGateway;
    }

    public Anuncio execute(Long id) {
        Optional<Anuncio> anuncio = anuncioGateway.buscarAnuncioPorId(id);
        if (anuncio.isEmpty()) {
            throw new AnuncioNotFoundException(id);
        }
        return anuncio.get();
    }
}
