package Lokei.aplication.infrastructure.persistence.entities;

import Lokei.aplication.domain.enums.StatusAluguelEnum;
import Lokei.aplication.domain.exceptions.AluguelException;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tb_avaliacao")
public class AvaliacaoEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer nota;
    private String comentario;
    private Date dataCriacao;

    @OneToOne
    @JoinColumn(name = "aluguel_id")
    private AluguelEntity aluguelEntity;


    public AvaliacaoEntity(){

    }

    public AvaliacaoEntity(Integer id, Integer nota, String comentario, Date dataCriacao, AluguelEntity aluguelEntity) {
        this.id = id;
        this.nota = nota;
        this.comentario = comentario;
        this.dataCriacao = dataCriacao;
        this.aluguelEntity = aluguelEntity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public AluguelEntity getAluguel() {
        return aluguelEntity;
    }

    public void setAluguel(AluguelEntity aluguelEntity) {
        this.aluguelEntity = aluguelEntity;
    }


    public void statusavaliacao(StatusAluguelEnum status) {

        if (status == StatusAluguelEnum.CONCLUIDO) {
            return;
        } else if (status == StatusAluguelEnum.CONFIRMADO) {
            throw new AluguelException("ainda não é possível avaliar este anúncio, seu periodo de reserva ainda não finalizou");
        } else if (status == StatusAluguelEnum.EM_ANDAMENTO) {
            throw new AluguelException("não é possível avaliar esse anúncio, a reserva ainda não foi finalizada");
        } else {
            throw new AluguelException("não é possível avaliar esse anúncio, a reserva não foi utilizada");
        }

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AvaliacaoEntity avaliacaoEntity = (AvaliacaoEntity) o;
        return Objects.equals(id, avaliacaoEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}


