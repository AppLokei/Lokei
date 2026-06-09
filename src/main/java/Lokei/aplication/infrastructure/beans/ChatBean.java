package Lokei.aplication.infrastructure.beans;

import Lokei.aplication.application.usecases.chat.*;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.ChatGateway;
import Lokei.aplication.domain.gateways.MensagemGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatBean {

    @Bean
    public IniciarOuBuscarChatUseCase iniciarOuBuscarChatUseCase(ChatGateway chatGateway,
                                                                   AnuncioGateway anuncioGateway,
                                                                   UsuarioGateway usuarioGateway) {
        return new IniciarOuBuscarChatUseCase(chatGateway, anuncioGateway, usuarioGateway);
    }

    @Bean
    public EnviarMensagemUseCase enviarMensagemUseCase(MensagemGateway mensagemGateway,
                                                        ChatGateway chatGateway) {
        return new EnviarMensagemUseCase(mensagemGateway, chatGateway);
    }

    @Bean
    public ListarMensagensUseCase listarMensagensUseCase(MensagemGateway mensagemGateway,
                                                          ChatGateway chatGateway) {
        return new ListarMensagensUseCase(mensagemGateway, chatGateway);
    }

    @Bean
    public ListarChatsPorUsuarioUseCase listarChatsPorUsuarioUseCase(ChatGateway chatGateway,
                                                                      UsuarioGateway usuarioGateway) {
        return new ListarChatsPorUsuarioUseCase(chatGateway, usuarioGateway);
    }
}
