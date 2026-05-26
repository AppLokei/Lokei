package Lokei.aplication.infrastructure.beans;

import Lokei.aplication.application.usecases.imagem.AdicionarImagemUseCase;
import Lokei.aplication.application.usecases.imagem.DeletarImagemUseCase;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.ImagemGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImagemBean {

    @Bean
    public AdicionarImagemUseCase adicionarImagemUseCase(ImagemGateway imagemGateway, AnuncioGateway anuncioGateway, UsuarioGateway usuarioGateway) {
        return new AdicionarImagemUseCase(imagemGateway, usuarioGateway, anuncioGateway);
    }

    @Bean
    public DeletarImagemUseCase deletarImagemUseCase(ImagemGateway imagemGateway, AnuncioGateway anuncioGateway, UsuarioGateway usuarioGateway) {
        return new DeletarImagemUseCase(imagemGateway, usuarioGateway, anuncioGateway);
    }

}
