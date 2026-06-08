package Lokei.aplication.domain.exceptions;

public class NaoAutenticadoException extends RuntimeException {

    public NaoAutenticadoException(String message) {
        super(message);
    }
}
