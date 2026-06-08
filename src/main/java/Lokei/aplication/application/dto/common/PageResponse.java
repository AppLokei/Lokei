package Lokei.aplication.application.dto.common;

import java.util.List;

public record PageResponse<T>(
        List<T> itens,
        long totalItens,
        int pagina,
        int tamanho,
        int totalPaginas
) {
}
