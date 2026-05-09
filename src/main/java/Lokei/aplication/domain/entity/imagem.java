package Lokei.aplication.domain.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.IdentityHashMap;
import java.util.Objects;

@Entity
@Table
public class imagem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String imagemUrl;

    public imagem(){

    }

    public imagem(Integer id, String imagemUrl) {
        this.id = id;
        this.imagemUrl = imagemUrl;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        imagem imagem = (imagem) o;
        return Objects.equals(id, imagem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
