package Lokei.aplication.application.service;

import Lokei.aplication.application.dto.notificacao.NotificacaoResponse;
import Lokei.aplication.application.support.DataFormatUtils;
import Lokei.aplication.infrastructure.persistence.entity.Notificacao;
import Lokei.aplication.infrastructure.persistence.entity.Usuario;
import Lokei.aplication.infrastructure.persistence.enums.tipoNotificacaoEnum;
import Lokei.aplication.infrastructure.persistence.repository.NotificacaoRepository;
import Lokei.aplication.infrastructure.shared.exception.AcessoNegadoException;
import Lokei.aplication.infrastructure.shared.exception.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    public NotificacaoService(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    public void notificar(Usuario usuario, tipoNotificacaoEnum tipo, String titulo, String mensagem) {
        Notificacao notificacao = new Notificacao();
        notificacao.setUsuario(usuario);
        notificacao.setTipo(tipo);
        notificacao.setTitulo(titulo);
        notificacao.setMensagem(mensagem);
        notificacao.setLida(false);
        notificacaoRepository.save(notificacao);
    }

    @Transactional(readOnly = true)
    public List<NotificacaoResponse> listar(Integer usuarioId) {
        return notificacaoRepository.findByUsuario_IdOrderByDataCriacaoDesc(usuarioId)
                .stream()
                .map(notificacao -> new NotificacaoResponse(
                        notificacao.getId(),
                        notificacao.getTipo().name(),
                        notificacao.getTitulo(),
                        notificacao.getMensagem(),
                        notificacao.isLida(),
                        DataFormatUtils.formatarDataHora(notificacao.getDataCriacao())
                ))
                .toList();
    }

    @Transactional
    public void marcarComoLida(Integer notificacaoId, Integer usuarioId) {
        Notificacao notificacao = notificacaoRepository.findById(notificacaoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Notificacao nao encontrada."));

        if (!notificacao.getUsuario().getId().equals(usuarioId)) {
            throw new AcessoNegadoException("Voce nao pode alterar esta notificacao.");
        }

        notificacao.setLida(true);
    }
}
