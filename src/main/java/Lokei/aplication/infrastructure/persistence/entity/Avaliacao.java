package Lokei.aplication.infrastructure.persistence.entity;

import Lokei.aplication.infrastructure.persistence.enums.tipoAvaliacaoEnum;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "avaliacoes")
public class Avaliacao extends EntidadeAuditavel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer nota;

    @Column(nullable = false, columnDefinition = "text")
    private String comentario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private tipoAvaliacaoEnum tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluguel_id", nullable = false)
    private Aluguel aluguel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Usuario autor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alvo_usuario_id")
    private Usuario alvoUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anuncio_id")
    private Anuncio anuncio;

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

    public tipoAvaliacaoEnum getTipo() {
        return tipo;
    }

    public void setTipo(tipoAvaliacaoEnum tipo) {
        this.tipo = tipo;
    }

    public Aluguel getAluguel() {
        return aluguel;
    }

    public void setAluguel(Aluguel aluguel) {
        this.aluguel = aluguel;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }

    public Usuario getAlvoUsuario() {
        return alvoUsuario;
    }

    public void setAlvoUsuario(Usuario alvoUsuario) {
        this.alvoUsuario = alvoUsuario;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Avaliacao avaliacao)) {
            return false;
        }
        return Objects.equals(id, avaliacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
