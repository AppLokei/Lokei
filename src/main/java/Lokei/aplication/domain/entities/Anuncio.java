package Lokei.aplication.domain.entities;

import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Anuncio {
    private Long id;
    private String titulo;
    private String descricao;
    private BigDecimal valorDiario;
    private StatusAnuncioEnum status;
    private LocalDateTime dataCriacao;
    private CategoriaEnum categoria;

    public Anuncio(Long id, String titulo, String descricao, BigDecimal valorDiario, StatusAnuncioEnum status, CategoriaEnum categoria) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.valorDiario = valorDiario;
        this.status = status;
        this.dataCriacao = LocalDateTime.now();
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public StatusAnuncioEnum getStatus() {
        return status;
    }

    public void setStatus(StatusAnuncioEnum status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public CategoriaEnum getCategoria() {
        return categoria;
    }

    public void setCategoriaEnum(CategoriaEnum categoria) {
        this.categoria = categoria;
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