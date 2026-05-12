package Lokei.aplication.infrastructure.persistence.entity;

import Lokei.aplication.infrastructure.persistence.enums.statusAluguelEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table
public class Aluguel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private BigDecimal valorTotal;
    @Enumerated(EnumType.STRING)
    private statusAluguelEnum statusAluguel;
    private LocalDateTime datacriacao;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avaliacao_id")
    private Avaliacao avaliacao;

    @ManyToOne
    @JoinColumn(name = "anuncio_id")
    private Anuncio anuncio;

    @ManyToOne
    @JoinColumn(name = "locatario_id")
    private Usuario locatario;

    public Aluguel(){

    }

    public Aluguel(Integer id, LocalDate dataInicio, LocalDate dataFim, BigDecimal valorTotal, statusAluguelEnum statusAluguel, LocalDateTime datacriacao, Avaliacao avaliacao, Anuncio anuncio, Usuario locatario) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorTotal = valorTotal;
        this.statusAluguel = statusAluguel;
        this.datacriacao = datacriacao;
        this.avaliacao = avaliacao;
        this.anuncio = anuncio;
        this.locatario = locatario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDateTime getDatacriacao() {
        return datacriacao;
    }

    public void setDatacriacao(LocalDateTime datacriacao) {
        this.datacriacao = datacriacao;
    }

    public statusAluguelEnum getStatusAluguel() {
        return statusAluguel;
    }

    public void setStatusAluguel(statusAluguelEnum statusAluguel) {
        this.statusAluguel = statusAluguel;
    }

    public Avaliacao getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Avaliacao avaliacao) {
        this.avaliacao = avaliacao;
    }

    @JsonIgnore
    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    public Usuario getLocatario() {
        return locatario;
    }

    public void setLocatario(Usuario locatario) {
        this.locatario = locatario;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Aluguel aluguel = (Aluguel) o;
        return Objects.equals(id, aluguel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
