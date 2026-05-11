package Lokei.aplication.infrastructure.beans;

import Lokei.aplication.application.usecases.anuncio.*;
import Lokei.aplication.domain.gateway.AnuncioGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnuncioBean {

    @Bean
    public CriarAnuncioUseCase criarAnuncioUseCase(AnuncioGateway anuncioGateway) {
        return new CriarAnuncioUseCase(anuncioGateway);
    }

    @Bean
    public AtualizarAnuncioUseCase atualizarAnuncioUseCase(AnuncioGateway anuncioGateway) {
        return new AtualizarAnuncioUseCase(anuncioGateway);
    }

    @Bean
    public ExcluirAnuncioUseCase excluirAnuncioUseCase(AnuncioGateway anuncioGateway) {
        return new ExcluirAnuncioUseCase(anuncioGateway);
    }

    @Bean
    public BuscarAnuncioPorIdUseCase buscarAnuncioPorIdUseCase(AnuncioGateway anuncioGateway) {
        return new BuscarAnuncioPorIdUseCase(anuncioGateway);
    }

    @Bean
    public BuscarTodosAnunciosUseCase buscarTodosAnunciosUseCase(AnuncioGateway anuncioGateway) {
        return new BuscarTodosAnunciosUseCase(anuncioGateway);
    }

    @Bean
    public BuscarAnunciosPorCategoriaUseCase buscarAnunciosPorCategoriaUseCase(AnuncioGateway anuncioGateway){
        return new BuscarAnunciosPorCategoriaUseCase(anuncioGateway);
    }
}
