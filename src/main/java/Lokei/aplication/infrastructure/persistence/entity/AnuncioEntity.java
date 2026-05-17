package Lokei.aplication.infrastructure.persistence.entity;

import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_anuncio")
public class AnuncioEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    private BigDecimal valorDiario;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @Enumerated(EnumType.STRING)
    private StatusAnuncioEnum status;

    @OneToOne
    @JoinColumn(name = "ferramenta_id")
    private FerramentaEntity ferramenta;

    @OneToMany(mappedBy = "anuncio", cascade = CascadeType.ALL)
    private List<ImagemEntity> imagens = new ArrayList<>();

    public AnuncioEntity(){}

    public AnuncioEntity(Long id, String descricao, BigDecimal valorDiario, LocalDateTime dataCriacao, StatusAnuncioEnum status, FerramentaEntity ferramenta) {
        this.id = id;
        this.descricao = descricao;
        this.valorDiario = valorDiario;
        this.dataCriacao = dataCriacao;
        this.status = status;
        this.ferramenta = ferramenta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorDiario() {
        return valorDiario;
    }

    public void setValorDiario(BigDecimal valorDiario) {
        this.valorDiario = valorDiario;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public StatusAnuncioEnum getStatus() {
        return status;
    }

    public void setStatus(StatusAnuncioEnum status) {
        this.status = status;
    }

    public FerramentaEntity getFerramenta() {
        return ferramenta;
    }

    public void setFerramenta(FerramentaEntity ferramenta) {
        this.ferramenta = ferramenta;
    }

    public List<ImagemEntity> getImagens() {
        return imagens;
    }

    public void setImagens(List<ImagemEntity> imagens) {
        this.imagens = imagens;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AnuncioEntity entity = (AnuncioEntity) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

