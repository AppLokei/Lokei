package Lokei.aplication.infrastructure.persistence.entity;

import Lokei.aplication.infrastructure.exception.AluguelException;
import Lokei.aplication.infrastructure.persistence.enums.statusAluguelEnum;
import jakarta.persistence.*;
import org.springframework.dao.DataIntegrityViolationException;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table
public class Avaliacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer nota;
    private String comentario;
    private Date dataCriacao;

    @OneToOne
    @JoinColumn(name = "aluguel_id", nullable = false)
    private Aluguel aluguel;


    public Avaliacao() {

    }

    public Avaliacao(Integer id, Integer nota, String comentario, Date dataCriacao, Aluguel aluguel) {
        this.id = id;
        this.nota = nota;
        this.comentario = comentario;
        this.dataCriacao = dataCriacao;
        this.aluguel = aluguel;
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

    public Aluguel getAluguel() {
        return aluguel;
    }

    public void setAluguel(Aluguel aluguel) {
        this.aluguel = aluguel;
    }

    public void statusavaliacao(statusAluguelEnum status) {

        if (status == statusAluguelEnum.CONCLUIDO) {
            return;
        } else if (status == statusAluguelEnum.ATIVO || status == statusAluguelEnum.CONFIRMADO) {
            throw new AluguelException("ainda não é possível avaliar este anúncio, seu periodo de reserva ainda não finalizou");
        } else if (status == statusAluguelEnum.EM_APROVACAO) {
            throw new AluguelException("não é possível avaliar esse anúncio, a reserva ainda não foi finalizada");
        } else {
            throw new AluguelException("não é possível avaliar esse anúncio, a reserva não foi utilizada");
        }

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Avaliacao avaliacao = (Avaliacao) o;
        return Objects.equals(id, avaliacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}


