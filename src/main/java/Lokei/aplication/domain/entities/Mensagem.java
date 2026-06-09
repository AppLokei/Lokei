package Lokei.aplication.domain.entities;

import Lokei.aplication.domain.exceptions.ChatInvalidoException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade de domínio que representa uma Mensagem dentro de um Chat.
 */
public class Mensagem {

    private final Long id;
    private final String conteudo;
    private final LocalDateTime dataHoraEnvio;
    private boolean lida;

    /** ID do usuário que enviou a mensagem (remetente) */
    private final Long remetenteId;

    /** ID do chat ao qual esta mensagem pertence */
    private final Long chatId;

    public Mensagem(Long id, String conteudo, LocalDateTime dataHoraEnvio,
                    boolean lida, Long remetenteId, Long chatId) {
        validar(conteudo, remetenteId, chatId);

        this.id = id;
        this.conteudo = conteudo;
        this.dataHoraEnvio = dataHoraEnvio != null ? dataHoraEnvio : LocalDateTime.now();
        this.lida = lida;
        this.remetenteId = remetenteId;
        this.chatId = chatId;
    }

    /**
     * Marca a mensagem como lida.
     */
    public void marcarComoLida() {
        this.lida = true;
    }

    private void validar(String conteudo, Long remetenteId, Long chatId) {
        if (conteudo == null || conteudo.isBlank()) {
            throw new ChatInvalidoException("O conteúdo da mensagem não pode ser vazio.");
        }
        if (remetenteId == null) {
            throw new ChatInvalidoException("O remetente da mensagem é obrigatório.");
        }
        if (chatId == null) {
            throw new ChatInvalidoException("O chat de destino é obrigatório.");
        }
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public Long getId() { return id; }
    public String getConteudo() { return conteudo; }
    public LocalDateTime getDataHoraEnvio() { return dataHoraEnvio; }
    public boolean isLida() { return lida; }
    public Long getRemetenteId() { return remetenteId; }
    public Long getChatId() { return chatId; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Mensagem mensagem = (Mensagem) o;
        return Objects.equals(id, mensagem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
