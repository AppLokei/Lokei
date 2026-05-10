package Lokei.aplication.infrastructure.persistence.entity;

import Lokei.aplication.infrastructure.persistence.enums.categoriaEnum;
import Lokei.aplication.infrastructure.persistence.enums.statusAnuncioEnum;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@Entity
@Table
public class Anuncio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titulo;
    private String descricao;
    private BigDecimal valorDiario;
    @Enumerated(EnumType.STRING)
    private statusAnuncioEnum status;
    private Date dateCriacao;
    @Enumerated(EnumType.STRING)
    private categoriaEnum categoria;

    @OneToMany(mappedBy = "anuncio")
    private Set<Aluguel> aluguel= new HashSet<>();

    public Anuncio(){

    }

    public Anuncio(Integer id, String titulo, String descricao, BigDecimal valorDiario, statusAnuncioEnum status, Date dateCriacao, categoriaEnum categoria, Set<Aluguel> aluguel) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.valorDiario = valorDiario;
        this.status = status;
        this.dateCriacao = dateCriacao;
        this.categoria = categoria;
        this.aluguel = aluguel;
    }

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

    public Date getDateCriacao() {
        return dateCriacao;
    }

    public void setDateCriacao(Date dateCriacao) {
        this.dateCriacao = dateCriacao;
    }

    public categoriaEnum getCategoria() {
        return categoria;
    }

    public void setCategoria(categoriaEnum categoria) {
        this.categoria = categoria;
    }

    public Set<Aluguel> getAluguel() {
        return aluguel;
    }

    public void setAluguel(Set<Aluguel> aluguel) {
        this.aluguel = aluguel;
    }

    public statusAnuncioEnum getStatus() {
        return status;
    }

    public void setStatus(statusAnuncioEnum status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Anuncio anuncio = (Anuncio) o;
        return Objects.equals(id, anuncio.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

