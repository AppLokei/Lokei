package Lokei.aplication.domain.exceptions;

public class ImagemNotFoundException extends RuntimeException {
    public ImagemNotFoundException(Long id) {
        super("Imagem não encontrado com id: " + id);
    }
}
