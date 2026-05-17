package Lokei.aplication.domain.gateways;

import Lokei.aplication.domain.entities.Ferramenta;

public interface FerramentaGateway {
    Ferramenta criarFerramenta(Ferramenta ferramenta);
    Ferramenta atualizarFerramenta(Ferramenta ferramenta);
}
