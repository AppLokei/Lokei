package Lokei.aplication.domain.entity;

import Lokei.aplication.domain.enums.statusAluguelEnum;
import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table
public class Aluguel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date dataInicio;
    private Date dataFim;
    private BigDecimal valorTotal;
    private statusAluguelEnum statusAluguel;
    private Date datacriacao;
    public Aluguel(){

    }
    public Aluguel(Integer id, Date dataInicio, Date dataFim, BigDecimal valorTotal, statusAluguelEnum statusAluguel, Date datacriacao) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorTotal = valorTotal;
        this.statusAluguel = statusAluguel;
        this.datacriacao = datacriacao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Date getDatacriacao() {
        return datacriacao;
    }

    public void setDatacriacao(Date datacriacao) {
        this.datacriacao = datacriacao;
    }

    public statusAluguelEnum getStatusAluguel() {
        return statusAluguel;
    }

    public void setStatusAluguel(statusAluguelEnum statusAluguel) {
        this.statusAluguel = statusAluguel;
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
