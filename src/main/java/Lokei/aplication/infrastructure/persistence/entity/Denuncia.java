package Lokei.aplication.infrastructure.persistence.entity;

import Lokei.aplication.infrastructure.persistence.enums.motivoDenunciaEnum;
import Lokei.aplication.infrastructure.persistence.enums.statusDenunciaEnum;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "denuncias")
public class Denuncia extends EntidadeAuditavel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private motivoDenunciaEnum motivo;

    @Column(nullable = false, columnDefinition = "text")
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private statusDenunciaEnum status;

    private LocalDateTime dataDecisao;

    @Column(columnDefinition = "text")
    private String parecerAdministrativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anuncio_id", nullable = false)
    private Anuncio anuncio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "denunciante_id", nullable = false)
    private Usuario denunciante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrador_id")
    private Usuario administrador;

    @ElementCollection
    @CollectionTable(name = "denuncia_imagens", joinColumns = @JoinColumn(name = "denuncia_id"))
    @Column(name = "imagem_url", nullable = false)
    private List<String> imagens = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public motivoDenunciaEnum getMotivo() {
        return motivo;
    }

    public void setMotivo(motivoDenunciaEnum motivo) {
        this.motivo = motivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public statusDenunciaEnum getStatus() {
        return status;
    }

    public void setStatus(statusDenunciaEnum status) {
        this.status = status;
    }

    public LocalDateTime getDataDecisao() {
        return dataDecisao;
    }

    public void setDataDecisao(LocalDateTime dataDecisao) {
        this.dataDecisao = dataDecisao;
    }

    public String getParecerAdministrativo() {
        return parecerAdministrativo;
    }

    public void setParecerAdministrativo(String parecerAdministrativo) {
        this.parecerAdministrativo = parecerAdministrativo;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    public Usuario getDenunciante() {
        return denunciante;
    }

    public void setDenunciante(Usuario denunciante) {
        this.denunciante = denunciante;
    }

    public Usuario getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Usuario administrador) {
        this.administrador = administrador;
    }

    public List<String> getImagens() {
        return imagens;
    }

    public void setImagens(List<String> imagens) {
        this.imagens = imagens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Denuncia denuncia)) {
            return false;
        }
        return Objects.equals(id, denuncia.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
