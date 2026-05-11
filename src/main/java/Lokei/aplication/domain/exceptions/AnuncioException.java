package Lokei.aplication.domain.exceptions;

public class AnuncioException extends RuntimeException {
    public AnuncioException(Long id) {
        super("Anuncio não encontrado com id: " + id);
    }
}
