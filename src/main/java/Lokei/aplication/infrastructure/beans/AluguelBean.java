package Lokei.aplication.infrastructure.beans;

import Lokei.aplication.application.usecases.aluguel.BuscarAluguelPorUsuarioUseCase;
import Lokei.aplication.domain.gateways.AluguelGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AluguelBean {

    @Bean
    public BuscarAluguelPorUsuarioUseCase buscarAluguelPorUsuarioUseCase(AluguelGateway aluguelGateway, UsuarioGateway usuarioGateway) {
        return new BuscarAluguelPorUsuarioUseCase(aluguelGateway, usuarioGateway);
    }
}

