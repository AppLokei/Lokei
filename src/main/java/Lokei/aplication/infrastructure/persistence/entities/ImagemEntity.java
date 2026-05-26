package Lokei.aplication.infrastructure.persistence.entities;

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
    private Long id;
    private String imagemUrl;
    private String publicId;

    @ManyToOne
    @JoinColumn(name = "anuncio_id")
    private AnuncioEntity anuncio;

    public ImagemEntity(){
    }

    public ImagemEntity(Long id, String imagemUrl, String publicId, AnuncioEntity anuncio) {
        this.id = id;
        this.imagemUrl = imagemUrl;
        this.publicId = publicId;
        this.anuncio = anuncio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public AnuncioEntity getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(AnuncioEntity anuncio) {
        this.anuncio = anuncio;
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
