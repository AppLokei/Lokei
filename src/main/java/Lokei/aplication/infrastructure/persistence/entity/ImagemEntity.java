package Lokei.aplication.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_imagem")
public class ImagemEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String imagemUrl;

    public ImagemEntity(){

    }

    public ImagemEntity(Integer id, String imagemUrl) {
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
        ImagemEntity imagemEntity = (ImagemEntity) o;
        return Objects.equals(id, imagemEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
