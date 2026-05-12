package Lokei.aplication.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table
public class Imagem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String imagemUrl;

    @ManyToOne
    @JoinColumn(name = "anuncio_id")
    private Anuncio anuncio;

    public Imagem(){

    }

    public Imagem(Integer id, String imagemUrl, Anuncio anuncio) {
        this.id = id;
        this.imagemUrl = imagemUrl;
        this.anuncio = anuncio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    @JsonIgnore
    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Imagem imagem = (Imagem) o;
        return Objects.equals(id, imagem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
