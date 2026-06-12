package Lokei.aplication.domain.entities;

import Lokei.aplication.domain.exceptions.ChatInvalidoException;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade de domínio que representa um Canal de Chat entre locador e locatário.
 * Regras de negócio HU-019 e RN-011: apenas as partes envolvidas no anúncio podem acessar o chat.
 */
public class Chat {

    private final Long id;
    private final LocalDateTime dataCriacao;

    /** ID do locatário (quem aluga / quem iniciou o contato) */
    private final Long locatarioId;

    /** ID do locador (dono do anúncio) */
    private final Long locadorId;

    /** ID do anúncio que originou o chat */
    private final Long anuncioId;

    public Chat(Long id, LocalDateTime dataCriacao, Long locatarioId, Long locadorId, Long anuncioId) {
        validar(locatarioId, locadorId, anuncioId);

        this.id = id;
        this.dataCriacao = dataCriacao != null ? dataCriacao : LocalDateTime.now();
        this.locatarioId = locatarioId;
        this.locadorId = locadorId;
        this.anuncioId = anuncioId;
    }

    /**
     * Verifica se um usuário tem permissão para acessar este chat (RN-011).
     * Apenas locador e locatário podem participar.
     */
    public boolean usuarioPodeAcessar(Long usuarioId) {
        return Objects.equals(usuarioId, locatarioId) || Objects.equals(usuarioId, locadorId);
    }

    private void validar(Long locatarioId, Long locadorId, Long anuncioId) {
        if (locatarioId == null) {
            throw new ChatInvalidoException("O locatário é obrigatório para iniciar um chat.");
        }
        if (locadorId == null) {
            throw new ChatInvalidoException("O locador é obrigatório para o chat.");
        }
        if (anuncioId == null) {
            throw new ChatInvalidoException("O anúncio é obrigatório para o chat.");
        }
        if (Objects.equals(locatarioId, locadorId)) {
            throw new ChatInvalidoException("O locatário e o locador não podem ser o mesmo usuário.");
        }
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public Long getId() { return id; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public Long getLocatarioId() { return locatarioId; }
    public Long getLocadorId() { return locadorId; }
    public Long getAnuncioId() { return anuncioId; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Chat chat = (Chat) o;
        return Objects.equals(id, chat.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
