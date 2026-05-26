package Lokei.aplication.infrastructure.gateways;

import Lokei.aplication.domain.gateways.AluguelGateway;
import org.springframework.stereotype.Component;

@Component
public class AluguelRepositoryGateway implements AluguelGateway {
    @Override
    public boolean existeAluguelEmAndamentoPorAnuncio(Long anuncioId) {
        return false; // temporario para que todo anuncio nao tenha aluguel em andamento. favor implementar logica
    }
}
