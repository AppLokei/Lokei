package Lokei.aplication.domain.entities;

import Lokei.aplication.domain.enums.StatusAnuncioEnum;
import Lokei.aplication.domain.exceptions.AnuncioLimiteFotosException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Anuncio {
    private Long id;
    private String descricao;
    private BigDecimal valorDiario;
    private StatusAnuncioEnum status;
    private LocalDateTime dataCriacao;

    private List<Imagem> imagens = new ArrayList<>();

    private Ferramenta ferramenta;

    public Anuncio(Long id, String descricao, BigDecimal valorDiario, StatusAnuncioEnum status, Ferramenta ferramenta) {
        this.id = id;
        this.descricao = descricao;
        this.valorDiario = valorDiario;
        this.status = status;
        this.dataCriacao = LocalDateTime.now();
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

    public StatusAnuncioEnum getStatus() {
        return status;
    }

    public void setStatus(StatusAnuncioEnum status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public List<Imagem> getImagens() {
        return imagens;
    }

    public Ferramenta getFerramenta() {
        return ferramenta;
    }

    public void setFerramenta(Ferramenta ferramenta) {
        this.ferramenta = ferramenta;
    }

    public void adicionarImagem(Imagem imagem) {
        if (imagens.size() >= 5) {
            throw new AnuncioLimiteFotosException();
        }
        imagens.add(imagem);
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