package Lokei.aplication.adapter.dto.res;

import java.time.LocalDate;

public record DenunciaResponse(
        Long usuarioId,
        String motivo,
        String descricao,
        LocalDate dataDenuncia
) {
}
