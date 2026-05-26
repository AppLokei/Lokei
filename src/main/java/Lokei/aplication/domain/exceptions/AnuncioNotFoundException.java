package Lokei.aplication.domain.exceptions;

public class AnuncioNotFoundException extends RuntimeException {
    public AnuncioNotFoundException(Long id) {
       super("Anuncio não encontrado com id: " + id);
    }
}
