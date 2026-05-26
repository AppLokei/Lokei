package Lokei.aplication.infrastructure.beans;

import Lokei.aplication.application.usecases.denuncia.DenunciarAnuncioUseCase;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.DenunciaGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DenunciaBean {

    @Bean
    public DenunciarAnuncioUseCase denunciarAnuncioUseCase(DenunciaGateway denunciaGateway, AnuncioGateway anuncioGateway, UsuarioGateway usuarioGateway) {
        return new DenunciarAnuncioUseCase(denunciaGateway, anuncioGateway, usuarioGateway);
    }
}
