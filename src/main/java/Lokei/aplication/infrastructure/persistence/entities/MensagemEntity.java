package Lokei.aplication.infrastructure.persistence.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade JPA de Mensagem dentro de um Chat.
 */
@Entity
@Table(name = "tb_mensagem")
public class MensagemEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String conteudo;

    @CreationTimestamp
    @Column(name = "data_hora_envio", nullable = false, updatable = false)
    private LocalDateTime dataHoraEnvio;

    @Column(nullable = false)
    private boolean lida = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remetente_id", nullable = false)
    private UsuarioEntity remetente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private ChatEntity chat;

    public MensagemEntity() {}

    // ── Getters e Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getConteudo() { return conteudo; }
    public void setConteudo(String conteudo) { this.conteudo = conteudo; }

    public LocalDateTime getDataHoraEnvio() { return dataHoraEnvio; }
    public void setDataHoraEnvio(LocalDateTime dataHoraEnvio) { this.dataHoraEnvio = dataHoraEnvio; }

    public boolean isLida() { return lida; }
    public void setLida(boolean lida) { this.lida = lida; }

    public UsuarioEntity getRemetente() { return remetente; }
    public void setRemetente(UsuarioEntity remetente) { this.remetente = remetente; }

    public Long getRemetenteId() { return remetente != null ? remetente.getId() : null; }
    public void setRemetenteId(Long remetenteId) {
        if (remetenteId == null) { this.remetente = null; return; }
        if (this.remetente == null) this.remetente = new UsuarioEntity();
        this.remetente.setId(remetenteId);
    }

    public ChatEntity getChat() { return chat; }
    public void setChat(ChatEntity chat) { this.chat = chat; }

    public Long getChatId() { return chat != null ? chat.getId() : null; }
    public void setChatId(Long chatId) {
        if (chatId == null) { this.chat = null; return; }
        if (this.chat == null) this.chat = new ChatEntity();
        this.chat.setId(chatId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MensagemEntity that = (MensagemEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
