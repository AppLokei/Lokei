package Lokei.aplication.domain.exceptions;

public class UsuarioNaoAutorizadoException extends RuntimeException {
    public UsuarioNaoAutorizadoException(String message) {
        super(message);
    }
}
