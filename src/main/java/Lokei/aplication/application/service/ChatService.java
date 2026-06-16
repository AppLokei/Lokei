package Lokei.aplication.application.service;

import Lokei.aplication.application.dto.chat.ConversaChatResponse;
import Lokei.aplication.application.dto.chat.MensagemChatRequest;
import Lokei.aplication.application.dto.chat.MensagemChatResponse;
import Lokei.aplication.application.support.DataFormatUtils;
import Lokei.aplication.infrastructure.persistence.entity.Aluguel;
import Lokei.aplication.infrastructure.persistence.entity.ConversaChat;
import Lokei.aplication.infrastructure.persistence.entity.MensagemChat;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import Lokei.aplication.infrastructure.persistence.enums.tipoNotificacaoEnum;
import Lokei.aplication.infrastructure.persistence.repository.ConversaChatRepository;
import Lokei.aplication.infrastructure.persistence.repository.MensagemChatRepository;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import Lokei.aplication.infrastructure.shared.exception.AcessoNegadoException;
import Lokei.aplication.infrastructure.shared.exception.RegraDeNegocioException;
import Lokei.aplication.infrastructure.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatService {

    private final ConversaChatRepository conversaChatRepository;
    private final MensagemChatRepository mensagemChatRepository;
    private final UsuarioRepository usuarioRepository;
    private final AluguelService aluguelService;
    private final NotificacaoService notificacaoService;

    public ChatService(
            ConversaChatRepository conversaChatRepository,
            MensagemChatRepository mensagemChatRepository,
            UsuarioRepository usuarioRepository,
            AluguelService aluguelService,
            NotificacaoService notificacaoService
    ) {
        this.conversaChatRepository = conversaChatRepository;
        this.mensagemChatRepository = mensagemChatRepository;
        this.usuarioRepository = usuarioRepository;
        this.aluguelService = aluguelService;
        this.notificacaoService = notificacaoService;
    }

    @Transactional(readOnly = true)
    public List<ConversaChatResponse> listarConversas(Integer usuarioId) {
        return conversaChatRepository.findByLocador_IdOrLocatario_IdOrderByDataCriacaoDesc(usuarioId, usuarioId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ConversaChatResponse buscarOuCriarPorAluguel(Integer aluguelId, Integer usuarioId) {
        Aluguel aluguel = aluguelService.buscarDetalhado(aluguelId);
        validarParticipante(aluguel, usuarioId);
        if (!aluguelService.chatDisponivel(aluguel)) {
            throw new RegraDeNegocioException("O chat so fica disponivel apos a confirmacao do aluguel.");
        }

        ConversaChat conversa = conversaChatRepository.findByAluguel_Id(aluguelId)
                .orElseGet(() -> criarConversa(aluguel));
        return toResponse(conversaChatRepository.findDetailedById(conversa.getId()).orElse(conversa));
    }

    @Transactional(readOnly = true)
    public ConversaChatResponse detalhar(Integer conversaId, Integer usuarioId) {
        ConversaChat conversa = conversaChatRepository.findDetailedById(conversaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conversa nao encontrada."));
        validarParticipante(conversa, usuarioId);
        return toResponse(conversa);
    }

    @Transactional
    public MensagemChatResponse enviarMensagem(Integer conversaId, Integer usuarioId, MensagemChatRequest request) {
        ConversaChat conversa = conversaChatRepository.findDetailedById(conversaId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Conversa nao encontrada."));
        validarParticipante(conversa, usuarioId);

        Usuario remetente = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado."));
        MensagemChat mensagem = new MensagemChat();
        mensagem.setConversa(conversa);
        mensagem.setRemetente(remetente);
        mensagem.setConteudo(request.conteudo().trim());
        MensagemChat salva = mensagemChatRepository.save(mensagem);

        Usuario destino = conversa.getLocador().getId().equals(usuarioId) ? conversa.getLocatario() : conversa.getLocador();
        notificacaoService.notificar(
                destino,
                tipoNotificacaoEnum.NOVA_MENSAGEM,
                "Nova mensagem",
                "Voce recebeu uma nova mensagem sobre o anuncio " + conversa.getAluguel().getAnuncio().getTitulo() + "."
        );

        return new MensagemChatResponse(
                salva.getId(),
                remetente.getId(),
                remetente.getNome(),
                salva.getConteudo(),
                DataFormatUtils.formatarDataHora(salva.getDataCriacao())
        );
    }

    private ConversaChat criarConversa(Aluguel aluguel) {
        ConversaChat conversa = new ConversaChat();
        conversa.setAluguel(aluguel);
        conversa.setLocador(aluguel.getAnuncio().getProprietario());
        conversa.setLocatario(aluguel.getLocatario());
        return conversaChatRepository.save(conversa);
    }

    private void validarParticipante(Aluguel aluguel, Integer usuarioId) {
        if (!aluguel.getLocatario().getId().equals(usuarioId) && !aluguel.getAnuncio().getProprietario().getId().equals(usuarioId)) {
            throw new AcessoNegadoException("Voce nao participa desta conversa.");
        }
    }

    private void validarParticipante(ConversaChat conversa, Integer usuarioId) {
        if (!conversa.getLocador().getId().equals(usuarioId) && !conversa.getLocatario().getId().equals(usuarioId)) {
            throw new AcessoNegadoException("Voce nao participa desta conversa.");
        }
    }

    private ConversaChatResponse toResponse(ConversaChat conversa) {
        return new ConversaChatResponse(
                conversa.getId(),
                conversa.getAluguel().getId(),
                conversa.getAluguel().getAnuncio().getId(),
                conversa.getAluguel().getAnuncio().getTitulo(),
                conversa.getAluguel().getAnuncio().getImagens().stream().map(imagem -> imagem.getImagemUrl()).findFirst().orElse(null),
                conversa.getLocador().getNome(),
                conversa.getLocatario().getNome(),
                conversa.getMensagens().stream()
                        .map(mensagem -> new MensagemChatResponse(
                                mensagem.getId(),
                                mensagem.getRemetente().getId(),
                                mensagem.getRemetente().getNome(),
                                mensagem.getConteudo(),
                                DataFormatUtils.formatarDataHora(mensagem.getDataCriacao())
                        ))
                        .toList()
        );
    }
}
