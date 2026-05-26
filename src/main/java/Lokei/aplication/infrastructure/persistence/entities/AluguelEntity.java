package Lokei.aplication.infrastructure.persistence.entities;

import Lokei.aplication.domain.enums.StatusAluguelEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tb_aluguel")
public class AluguelEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date dataInicio;
    private Date dataFim;
    private BigDecimal valorTotal;
    @Enumerated(EnumType.STRING)
    private StatusAluguelEnum statusAluguel;
    private Date datacriacao;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "avaliacao_id")
    private AvaliacaoEntity avaliacaoEntity;

    @ManyToOne
    @JoinColumn(name = "anuncio_id")
    private AnuncioEntity anuncio;
    public AluguelEntity(){

    }

    public AluguelEntity(Integer id, Date dataInicio, Date dataFim, BigDecimal valorTotal, StatusAluguelEnum statusAluguel, Date datacriacao, AvaliacaoEntity avaliacaoEntity, AnuncioEntity anuncio) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.valorTotal = valorTotal;
        this.statusAluguel = statusAluguel;
        this.datacriacao = datacriacao;
        this.avaliacaoEntity = avaliacaoEntity;
        this.anuncio = anuncio;
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

    public StatusAluguelEnum getStatusAluguel() {
        return statusAluguel;
    }

    public void setStatusAluguel(StatusAluguelEnum statusAluguel) {
        this.statusAluguel = statusAluguel;
    }

    public AvaliacaoEntity getAvaliacao() {
        return avaliacaoEntity;
    }

    public void setAvaliacao(AvaliacaoEntity avaliacaoEntity) {
        this.avaliacaoEntity = avaliacaoEntity;
    }

    @JsonIgnore
    public AnuncioEntity getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(AnuncioEntity anuncio) {
        this.anuncio = anuncio;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AluguelEntity aluguelEntity = (AluguelEntity) o;
        return Objects.equals(id, aluguelEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
