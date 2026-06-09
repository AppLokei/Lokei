package Lokei.aplication.infrastructure.beans;

import Lokei.aplication.application.usecases.denuncia.*;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.DenunciaGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import Lokei.aplication.domain.services.NotificacaoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DenunciaBean {

    @Bean
    public EnviarDenunciaUseCase enviarDenunciaUseCase(DenunciaGateway denunciaGateway,
                                                        AnuncioGateway anuncioGateway,
                                                        UsuarioGateway usuarioGateway) {
        return new EnviarDenunciaUseCase(denunciaGateway, anuncioGateway, usuarioGateway);
    }

    @Bean
    public AprovarDenunciaUseCase aprovarDenunciaUseCase(DenunciaGateway denunciaGateway,
                                                          AnuncioGateway anuncioGateway,
                                                          NotificacaoService notificacaoService) {
        return new AprovarDenunciaUseCase(denunciaGateway, anuncioGateway, notificacaoService);
    }

    @Bean
    public RecusarDenunciaUseCase recusarDenunciaUseCase(DenunciaGateway denunciaGateway) {
        return new RecusarDenunciaUseCase(denunciaGateway);
    }

    @Bean
    public ListarDenunciasUseCase listarDenunciasUseCase(DenunciaGateway denunciaGateway) {
        return new ListarDenunciasUseCase(denunciaGateway);
    }
}
