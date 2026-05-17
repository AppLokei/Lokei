package Lokei.aplication.application.usecases.ferramenta;

import Lokei.aplication.domain.entities.Ferramenta;
import Lokei.aplication.domain.gateways.FerramentaGateway;

public class CriarFerramentaUseCase {

    private final FerramentaGateway ferramentaGateway;

    public CriarFerramentaUseCase(FerramentaGateway ferramentaGateway) {
        this.ferramentaGateway = ferramentaGateway;
    }

    public Ferramenta execute(Ferramenta ferramenta) {
        return ferramentaGateway.criarFerramenta(ferramenta);
    }

}
