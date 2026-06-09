package Lokei.aplication.domain.entities;

import Lokei.aplication.domain.enums.StatusAluguelEnum;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

public class Aluguel {

    private final Long id;
    private final Date dataInicio;
    private final Date dataFim;
    private final BigDecimal valorTotal;
    private final StatusAluguelEnum status;
    private final Long anuncioId;

    public Aluguel(Long id, Date dataInicio, Date dataFim, BigDecimal valorTotal, StatusAluguelEnum status, Long anuncioId) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorTotal = valorTotal;
        this.status = status;
        this.anuncioId = anuncioId;
    }

    public Long getId() {
        return id;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public StatusAluguelEnum getStatus() {
        return status;
    }

    public Long getAnuncioId() {
        return anuncioId;
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

