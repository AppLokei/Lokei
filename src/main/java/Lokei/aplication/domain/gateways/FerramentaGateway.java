package Lokei.aplication.domain.gateways;

import Lokei.aplication.domain.entities.Ferramenta;

import java.util.Optional;

public interface FerramentaGateway {
    Ferramenta criarFerramenta(Ferramenta ferramenta);
    Optional<Ferramenta> buscarFerramentaPorId(Long id);
}
