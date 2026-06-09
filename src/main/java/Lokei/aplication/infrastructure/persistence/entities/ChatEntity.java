package Lokei.aplication.infrastructure.persistence.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entidade JPA de Chat entre locador e locatário, vinculado a um anúncio.
 */
@Entity
@Table(
    name = "tb_chat",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_chat_anuncio_locatario",
        columnNames = {"anuncio_id", "locatario_id"}
    )
)
public class ChatEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locatario_id", nullable = false)
    private UsuarioEntity locatario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locador_id", nullable = false)
    private UsuarioEntity locador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anuncio_id", nullable = false)
    private AnuncioEntity anuncio;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MensagemEntity> mensagens = new ArrayList<>();

    public ChatEntity() {}

    // ── Getters e Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public UsuarioEntity getLocatario() { return locatario; }
    public void setLocatario(UsuarioEntity locatario) { this.locatario = locatario; }

    public Long getLocatarioId() { return locatario != null ? locatario.getId() : null; }
    public void setLocatarioId(Long locatarioId) {
        if (locatarioId == null) { this.locatario = null; return; }
        if (this.locatario == null) this.locatario = new UsuarioEntity();
        this.locatario.setId(locatarioId);
    }

    public UsuarioEntity getLocador() { return locador; }
    public void setLocador(UsuarioEntity locador) { this.locador = locador; }

    public Long getLocadorId() { return locador != null ? locador.getId() : null; }
    public void setLocadorId(Long locadorId) {
        if (locadorId == null) { this.locador = null; return; }
        if (this.locador == null) this.locador = new UsuarioEntity();
        this.locador.setId(locadorId);
    }

    public AnuncioEntity getAnuncio() { return anuncio; }
    public void setAnuncio(AnuncioEntity anuncio) { this.anuncio = anuncio; }

    public Long getAnuncioId() { return anuncio != null ? anuncio.getId() : null; }
    public void setAnuncioId(Long anuncioId) {
        if (anuncioId == null) { this.anuncio = null; return; }
        if (this.anuncio == null) this.anuncio = new AnuncioEntity();
        this.anuncio.setId(anuncioId);
    }

    public List<MensagemEntity> getMensagens() { return mensagens; }
    public void setMensagens(List<MensagemEntity> mensagens) { this.mensagens = mensagens; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChatEntity that = (ChatEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
