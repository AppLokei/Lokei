package Lokei.aplication.infrastructure.beans;

import Lokei.aplication.adapter.mapper.FerramentaControllerMapper;
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
    FerramentaControllerMapper ferramentaDTOMapper() {
        return new FerramentaControllerMapper();
    }
}
