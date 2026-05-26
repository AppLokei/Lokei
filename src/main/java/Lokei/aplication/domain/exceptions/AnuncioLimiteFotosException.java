package Lokei.aplication.domain.exceptions;

public class AnuncioLimiteFotosException extends RuntimeException {
    public AnuncioLimiteFotosException() {
        super("Limite máximo de 5 fotos atingido.");
    }
}
