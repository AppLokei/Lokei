package Lokei.aplication.infrastructure.beans;

import Lokei.aplication.application.usecases.anuncio.*;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.FerramentaGateway;
import Lokei.aplication.domain.gateways.ImagemGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AnuncioBean {

    @Bean
    public CriarAnuncioUseCase criarAnuncioUseCase(AnuncioGateway anuncioGateway, FerramentaGateway ferramentaGateway, ImagemGateway imagemGateway) {
        return new CriarAnuncioUseCase(anuncioGateway, ferramentaGateway, imagemGateway);
    }

    @Bean
    public AtualizarAnuncioUseCase atualizarAnuncioUseCase(AnuncioGateway anuncioGateway, FerramentaGateway ferramentaGateway) {
        return new AtualizarAnuncioUseCase(anuncioGateway, ferramentaGateway);
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
