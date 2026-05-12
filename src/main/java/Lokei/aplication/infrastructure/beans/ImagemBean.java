package Lokei.aplication.infrastructure.beans;

import Lokei.aplication.application.usecases.imagem.ApagarImagemUseCase;
import Lokei.aplication.application.usecases.imagem.BuscarImagensPorAnuncioUseCase;
import Lokei.aplication.application.usecases.imagem.SalvarImagemUseCase;
import Lokei.aplication.domain.gateway.ImagemGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImagemBean {

    @Bean
    public SalvarImagemUseCase salvarImagemUseCase(ImagemGateway imagemGateway) {
        return new SalvarImagemUseCase(imagemGateway);
    }

    @Bean
    public ApagarImagemUseCase apagarImagemUseCase(ImagemGateway imagemGateway) {
        return new ApagarImagemUseCase(imagemGateway);
    }

    @Bean
    public BuscarImagensPorAnuncioUseCase buscarImagensPorAnuncioUseCase(ImagemGateway imagemGateway) {
        return new BuscarImagensPorAnuncioUseCase(imagemGateway);
    }

}
