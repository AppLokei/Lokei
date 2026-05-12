package Lokei.aplication.domain.entities;

import java.util.Objects;

public class Imagem {
    private Long id;
    private String imagemUrl;

    private Anuncio anuncio;

    public Imagem(){}

    public Imagem(Long id, String imagemUrl, Anuncio anuncio) {
        this.id = id;
        this.imagemUrl = imagemUrl;
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
