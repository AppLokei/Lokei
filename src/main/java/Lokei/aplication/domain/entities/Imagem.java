package Lokei.aplication.domain.entities;

import Lokei.aplication.domain.exceptions.ImagemInvalidaException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Imagem {
    private Long id;
    private String imagemUrl;
    private String publicId;

    public Imagem(Long id, String imagemUrl, String publicId) {
        this.id = id;
        this.imagemUrl = imagemUrl;
        this.publicId = publicId;
    }

    public Long getId() {
        return id;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public String getPublicId() {
        return publicId;
    }

    public static void validaImagem(String extensao, long tamanho) {
        List<String> extensoesPermitidas = Arrays.asList("image/jpg", "image/jpeg", "image/png");
        long tamanhoMaximo = 5 * 1024 * 1024;

        if (extensao == null || !extensoesPermitidas.contains(extensao.toLowerCase())){
            throw new ImagemInvalidaException("Formato não suportado. Envie imagens nos formatos JPG, JPEG ou PNG.");
        }

        if (tamanho > tamanhoMaximo) {
            throw new ImagemInvalidaException("A imagem excede o tamanho máximo permitido de 5MB.");
        }
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
