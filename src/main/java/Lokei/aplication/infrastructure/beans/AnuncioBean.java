package Lokei.aplication.infrastructure.beans;

import Lokei.aplication.application.usecases.anuncio.*;
import Lokei.aplication.domain.gateways.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnuncioBean {

    @Bean
    public CriarAnuncioUseCase criarAnuncioUseCase(AnuncioGateway anuncioGateway, UsuarioGateway usuarioGateway) {
        return new CriarAnuncioUseCase(anuncioGateway, usuarioGateway);
    }

    @Bean
    public AtualizarAnuncioUseCase atualizarAnuncioUseCase(AnuncioGateway anuncioGateway, AluguelGateway aluguelGateway, UsuarioGateway usuarioGateway) {
        return new AtualizarAnuncioUseCase(anuncioGateway, aluguelGateway, usuarioGateway);
    }

    @Bean
    public PausarAnuncioUseCase pausarAnuncioUseCase(AnuncioGateway anuncioGateway, UsuarioGateway usuarioGateway, AluguelGateway aluguelGateway) {
        return new PausarAnuncioUseCase(anuncioGateway, usuarioGateway, aluguelGateway);
    }

    @Bean
    public ReativarAnuncioUseCase reativarAnuncioUseCase(AnuncioGateway anuncioGateway, UsuarioGateway usuarioGateway) {
        return new ReativarAnuncioUseCase(anuncioGateway, usuarioGateway);
    }

    @Bean
    public DesativarAnuncioUseCase desativarAnuncioUseCase(AnuncioGateway anuncioGateway, UsuarioGateway usuarioGateway, AluguelGateway aluguelGateway) {
        return new DesativarAnuncioUseCase(anuncioGateway, usuarioGateway, aluguelGateway);
    }

    @Bean
    public BuscarAnunciosUseCase buscarAnunciosUseCase(AnuncioGateway anuncioGateway) {
        return new BuscarAnunciosUseCase(anuncioGateway);
    }

}
