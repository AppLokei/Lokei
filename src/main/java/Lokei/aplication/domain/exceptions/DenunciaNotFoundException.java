package Lokei.aplication.domain.exceptions;

public class DenunciaNotFoundException extends RuntimeException {
    public DenunciaNotFoundException(Long id) {
        super("Denúncia não encontrada com id: " + id);
    }
}
