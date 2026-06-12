package Lokei.aplication.infrastructure.persistence.entities;

import Lokei.aplication.domain.enums.MotivoDenunciaEnum;
import Lokei.aplication.domain.enums.StatusDenunciaEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade JPA de Denuncia — inclui relacionamentos com AnuncioEntity e UsuarioEntity.
 * Versão atualizada para suportar os fluxos RN-034 e RN-035.
 */
@Entity
@Table(name = "tb_denuncia")
public class DenunciaEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MotivoDenunciaEnum motivo;

    @Column(nullable = false, length = 1000)
    private String descricao;

    @CreationTimestamp
    @Column(name = "data_denuncia", nullable = false, updatable = false)
    private LocalDateTime dataDenuncia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusDenunciaEnum status = StatusDenunciaEnum.PENDENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anuncio_id", nullable = false)
    private AnuncioEntity anuncio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "denunciante_id", nullable = false)
    private UsuarioEntity denunciante;

    public DenunciaEntity() {}

    // ── Getters e Setters ─────────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MotivoDenunciaEnum getMotivo() { return motivo; }
    public void setMotivo(MotivoDenunciaEnum motivo) { this.motivo = motivo; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataDenuncia() { return dataDenuncia; }
    public void setDataDenuncia(LocalDateTime dataDenuncia) { this.dataDenuncia = dataDenuncia; }

    public StatusDenunciaEnum getStatus() { return status; }
    public void setStatus(StatusDenunciaEnum status) { this.status = status; }

    public AnuncioEntity getAnuncio() { return anuncio; }
    public void setAnuncio(AnuncioEntity anuncio) { this.anuncio = anuncio; }

    public Long getAnuncioId() { return anuncio != null ? anuncio.getId() : null; }
    public void setAnuncioId(Long anuncioId) {
        if (anuncioId == null) { this.anuncio = null; return; }
        if (this.anuncio == null) this.anuncio = new AnuncioEntity();
        this.anuncio.setId(anuncioId);
    }

    public UsuarioEntity getDenunciante() { return denunciante; }
    public void setDenunciante(UsuarioEntity denunciante) { this.denunciante = denunciante; }

    public Long getDenuncianteId() { return denunciante != null ? denunciante.getId() : null; }
    public void setDenuncianteId(Long denuncianteId) {
        if (denuncianteId == null) { this.denunciante = null; return; }
        if (this.denunciante == null) this.denunciante = new UsuarioEntity();
        this.denunciante.setId(denuncianteId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DenunciaEntity that = (DenunciaEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hashCode(id); }
}
