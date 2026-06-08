package Lokei.aplication.infrastructure.controllers;

import Lokei.aplication.application.dto.chat.ConversaChatResponse;
import Lokei.aplication.application.dto.chat.MensagemChatRequest;
import Lokei.aplication.application.dto.chat.MensagemChatResponse;
import Lokei.aplication.application.service.ChatService;
import Lokei.aplication.infrastructure.security.UsuarioAutenticadoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ChatController {

    private final ChatService chatService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public ChatController(ChatService chatService, UsuarioAutenticadoService usuarioAutenticadoService) {
        this.chatService = chatService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @GetMapping("/chats")
    public ResponseEntity<List<ConversaChatResponse>> listarConversas() {
        return ResponseEntity.ok(chatService.listarConversas(usuarioAutenticadoService.getUsuarioId()));
    }

    @GetMapping("/alugueis/{aluguelId}/chat")
    public ResponseEntity<ConversaChatResponse> obterOuCriar(@PathVariable Integer aluguelId) {
        return ResponseEntity.ok(chatService.buscarOuCriarPorAluguel(aluguelId, usuarioAutenticadoService.getUsuarioId()));
    }

    @GetMapping("/chats/{chatId}")
    public ResponseEntity<ConversaChatResponse> detalhar(@PathVariable Integer chatId) {
        return ResponseEntity.ok(chatService.detalhar(chatId, usuarioAutenticadoService.getUsuarioId()));
    }

    @PostMapping("/chats/{chatId}/mensagens")
    public ResponseEntity<MensagemChatResponse> enviarMensagem(@PathVariable Integer chatId, @Valid @RequestBody MensagemChatRequest request) {
        return ResponseEntity.ok(chatService.enviarMensagem(chatId, usuarioAutenticadoService.getUsuarioId(), request));
    }
}
