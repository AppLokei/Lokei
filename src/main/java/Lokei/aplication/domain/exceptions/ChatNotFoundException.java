package Lokei.aplication.domain.exceptions;

public class ChatNotFoundException extends RuntimeException {
    public ChatNotFoundException(Long id) {
        super("Chat não encontrado com id: " + id);
    }
}
