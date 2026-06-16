package Lokei.aplication.infrastructure.persistence.entity;

import Lokei.aplication.infrastructure.persistence.enums.categoriaEnum;
import Lokei.aplication.infrastructure.persistence.enums.statusAnuncioEnum;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "anuncios")
public class Anuncio extends EntidadeAuditavel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false, columnDefinition = "text")
    private String descricao;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal valorDiario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private statusAnuncioEnum status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private categoriaEnum categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proprietario_id", nullable = false)
    private Usuario proprietario;

    @OneToMany(mappedBy = "anuncio", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordem ASC, id ASC")
    private Set<Imagem> imagens = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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

    public statusAnuncioEnum getStatus() {
        return status;
    }

    public void setStatus(statusAnuncioEnum status) {
        this.status = status;
    }

    public categoriaEnum getCategoria() {
        return categoria;
    }

    public void setCategoria(categoriaEnum categoria) {
        this.categoria = categoria;
    }

    public Usuario getProprietario() {
        return proprietario;
    }

    public void setProprietario(Usuario proprietario) {
        this.proprietario = proprietario;
    }

    public Set<Imagem> getImagens() {
        return imagens;
    }

    public void setImagens(Set<Imagem> imagens) {
        this.imagens = imagens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Anuncio anuncio)) {
            return false;
        }
        return Objects.equals(id, anuncio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
