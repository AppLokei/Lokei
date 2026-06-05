package Lokei.aplication.domain.exceptions;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Long id) {
        super("Não foi possivel encontrar o id: " + id);
    }
}
