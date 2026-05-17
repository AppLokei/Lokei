package Lokei.aplication.infrastructure.beans;

import Lokei.aplication.adapters.mapper.FerramentaDTOMapper;
import Lokei.aplication.application.usecases.anuncio.AtualizarAnuncioUseCase;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.FerramentaGateway;
import Lokei.aplication.infrastructure.gateways.FerramentaRepositoryGateway;
import Lokei.aplication.infrastructure.persistence.mapper.FerramentaMapper;
import Lokei.aplication.infrastructure.persistence.repository.FerramentaRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FerramentaBean {

    @Bean
    FerramentaGateway ferramentaGateway(FerramentaRepository ferramentaRepository) {
        return new FerramentaRepositoryGateway(ferramentaRepository);
    }

    @Bean
    FerramentaMapper ferramentaMapper() {
        return new FerramentaMapper();
    }

    @Bean
    FerramentaDTOMapper ferramentaDTOMapper() {
        return new FerramentaDTOMapper();
    }
}
