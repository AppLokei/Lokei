package Lokei.aplication.application.usecases.usuario;

import Lokei.aplication.adapter.dto.res.NotificacaoResponse;
import Lokei.aplication.adapter.dto.support.DataFormatUtils;
import Lokei.aplication.infrastructure.persistence.entities.NotificacaoEntity;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;
import Lokei.aplication.domain.enums.tipoNotificacaoEnum;
import Lokei.aplication.infrastructure.persistence.repository.NotificacaoRepository;
import Lokei.aplication.domain.exceptions.AcessoNegadoException;
import Lokei.aplication.domain.exceptions.RecursoNaoEncontradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificacaoUseCase {

    private final NotificacaoRepository notificacaoRepository;

    public NotificacaoUseCase(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    public void notificar(UsuarioEntity usuario, tipoNotificacaoEnum tipo, String titulo, String mensagem) {
        NotificacaoEntity notificacao = new NotificacaoEntity();
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
                        notificacao.getTitulo(),
                        notificacao.getMensagem(),
                        notificacao.isLida(),
                        DataFormatUtils.formatarDataHora(notificacao.getDataCriacao())
                ))
                .toList();
    }

    @Transactional
    public void marcarComoLida(Integer notificacaoId, Integer usuarioId) {
        NotificacaoEntity notificacao = notificacaoRepository.findById(notificacaoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Notificacao nao encontrada."));

        if (!notificacao.getUsuario().getId().equals(usuarioId)) {
            throw new AcessoNegadoException("Voce nao pode alterar esta notificacao.");
        }

        notificacao.setLida(true);
    }
}
