package Lokei.aplication.domain.gateways;

import Lokei.aplication.domain.enums.StatusAluguelEnum;

public interface AluguelGateway {
     boolean existeAluguelEmAndamentoPorAnuncio(Long anuncioId);
}
