package Lokei.aplication.adapter.controllers;

import Lokei.aplication.adapter.dto.req.MensagemRequest;
import Lokei.aplication.adapter.dto.res.ChatResponse;
import Lokei.aplication.adapter.dto.res.MensagemResponse;
import Lokei.aplication.adapter.mapper.ChatControllerMapper;
import Lokei.aplication.application.usecases.chat.*;
import Lokei.aplication.domain.entities.Chat;
import Lokei.aplication.domain.entities.Mensagem;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações de Chat e Mensagem.
 *
 * Endpoints:
 *   POST   /anuncios/{anuncioId}/chats                      — Inicia ou recupera chat (HU-019)
 *   GET    /chats?usuarioId={id}                            — Lista chats do usuário
 *   POST   /chats/{chatId}/mensagens                        — Envia mensagem (RN-011)
 *   GET    /chats/{chatId}/mensagens?usuarioId={id}         — Lista mensagens (RN-011)
 */
@RestController
public class ChatController {

    private final IniciarOuBuscarChatUseCase iniciarOuBuscarChatUseCase;
    private final EnviarMensagemUseCase enviarMensagemUseCase;
    private final ListarMensagensUseCase listarMensagensUseCase;
    private final ListarChatsPorUsuarioUseCase listarChatsPorUsuarioUseCase;

    public ChatController(IniciarOuBuscarChatUseCase iniciarOuBuscarChatUseCase,
                           EnviarMensagemUseCase enviarMensagemUseCase,
                           ListarMensagensUseCase listarMensagensUseCase,
                           ListarChatsPorUsuarioUseCase listarChatsPorUsuarioUseCase) {
        this.iniciarOuBuscarChatUseCase = iniciarOuBuscarChatUseCase;
        this.enviarMensagemUseCase = enviarMensagemUseCase;
        this.listarMensagensUseCase = listarMensagensUseCase;
        this.listarChatsPorUsuarioUseCase = listarChatsPorUsuarioUseCase;
    }

    /**
     * Inicia um novo chat ou retorna o existente entre o locatário e o locador do anúncio.
     * HU-019: Operação idempotente — não cria duplicatas.
     *
     * @param anuncioId   ID do anúncio de interesse
     * @param locatarioId ID do locatário que deseja contato
     *                    TODO: Substituir por autenticação JWT quando implementado
     */
    @PostMapping("/anuncios/{anuncioId}/chats")
    public ResponseEntity<ChatResponse> iniciarChat(
            @PathVariable Long anuncioId,
            @RequestParam Long locatarioId) {

        Chat chat = iniciarOuBuscarChatUseCase.execute(anuncioId, locatarioId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ChatControllerMapper.toChatResponse(chat));
    }

    /**
     * Lista todos os chats de um usuário (como locador ou locatário).
     */
    @GetMapping("/chats")
    public ResponseEntity<List<ChatResponse>> listarChats(@RequestParam Long usuarioId) {
        List<ChatResponse> chats = listarChatsPorUsuarioUseCase.execute(usuarioId)
                .stream()
                .map(ChatControllerMapper::toChatResponse)
                .toList();
        return ResponseEntity.ok(chats);
    }

    /**
     * Envia uma mensagem em um chat existente.
     * RN-011: Apenas as partes do chat podem enviar mensagens.
     */
    @PostMapping("/chats/{chatId}/mensagens")
    public ResponseEntity<MensagemResponse> enviarMensagem(
            @PathVariable Long chatId,
            @RequestBody @Valid MensagemRequest request) {

        Mensagem mensagem = enviarMensagemUseCase.execute(
                chatId,
                request.remetenteId(),   // TODO: substituir por autenticação
                request.conteudo()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ChatControllerMapper.toMensagemResponse(mensagem));
    }

    /**
     * Lista todas as mensagens de um chat, marcando automaticamente como lidas.
     * RN-011: Apenas as partes do chat podem visualizar as mensagens.
     */
    @GetMapping("/chats/{chatId}/mensagens")
    public ResponseEntity<List<MensagemResponse>> listarMensagens(
            @PathVariable Long chatId,
            @RequestParam Long usuarioId) {   // TODO: substituir por autenticação

        List<MensagemResponse> mensagens = listarMensagensUseCase.execute(chatId, usuarioId)
                .stream()
                .map(ChatControllerMapper::toMensagemResponse)
                .toList();

        return ResponseEntity.ok(mensagens);
    }
}
