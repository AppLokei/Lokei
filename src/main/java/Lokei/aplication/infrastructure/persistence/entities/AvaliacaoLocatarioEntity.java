package Lokei.aplication.infrastructure.persistence.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "tb_avaliacao_locatario")
public class AvaliacaoLocatarioEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer nota;
    private String comentario;
    private Date dataCriacao;

    @ManyToOne
    @JoinColumn(name = "aluguel_id")
    private AluguelEntity aluguel;

    @ManyToOne
    @JoinColumn(name = "avaliador_id")
    private UsuarioEntity avaliador;

    @ManyToOne
    @JoinColumn(name = "avaliado_id")
    private UsuarioEntity avaliado;

    public AvaliacaoLocatarioEntity() {}

    public AvaliacaoLocatarioEntity(Long id, Integer nota, String comentario, Date dataCriacao, AluguelEntity aluguel, UsuarioEntity avaliador, UsuarioEntity avaliado) {
        this.id = id;
        this.nota = nota;
        this.comentario = comentario;
        this.dataCriacao = dataCriacao;
        this.aluguel = aluguel;
        this.avaliador = avaliador;
        this.avaliado = avaliado;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public Date getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(Date dataCriacao) { this.dataCriacao = dataCriacao; }
    public AluguelEntity getAluguel() { return aluguel; }
    public void setAluguel(AluguelEntity aluguel) { this.aluguel = aluguel; }
    public UsuarioEntity getAvaliador() { return avaliador; }
    public void setAvaliador(UsuarioEntity avaliador) { this.avaliador = avaliador; }
    public UsuarioEntity getAvaliado() { return avaliado; }
    public void setAvaliado(UsuarioEntity avaliado) { this.avaliado = avaliado; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AvaliacaoLocatarioEntity that = (AvaliacaoLocatarioEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
