package Lokei.aplication.infrastructure.beans;

import Lokei.aplication.application.usecases.AvaliarLocatarioUseCase;
import Lokei.aplication.domain.gateways.AluguelGateway;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.AvaliacaoLocatarioGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AvaliacaoLocatarioBean {

    @Bean
    public AvaliarLocatarioUseCase avaliarLocatarioUseCase(
            AvaliacaoLocatarioGateway avaliacaoGateway,
            AluguelGateway aluguelGateway,
            AnuncioGateway anuncioGateway) {
        return new AvaliarLocatarioUseCase(avaliacaoGateway, aluguelGateway, anuncioGateway);
    }
}
