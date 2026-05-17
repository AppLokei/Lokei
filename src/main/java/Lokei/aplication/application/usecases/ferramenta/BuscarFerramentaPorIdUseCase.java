package Lokei.aplication.application.usecases.ferramenta;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Ferramenta;
import Lokei.aplication.domain.exceptions.AnuncioNotFoundException;
import Lokei.aplication.domain.gateways.FerramentaGateway;

import java.util.Optional;

public class BuscarFerramentaPorIdUseCase {

    private final FerramentaGateway ferramentaGateway;

    public BuscarFerramentaPorIdUseCase(FerramentaGateway ferramentaGateway) {
        this.ferramentaGateway = ferramentaGateway;
    }

    public Ferramenta execute(Long id) {
        Optional<Ferramenta> ferramenta = ferramentaGateway.buscarFerramentaPorId(id);
        return ferramenta.get();
    }
}
