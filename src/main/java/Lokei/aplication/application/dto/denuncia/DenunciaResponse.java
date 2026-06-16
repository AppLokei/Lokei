package Lokei.aplication.application.dto.denuncia;

import java.util.List;

public record DenunciaResponse(
        Integer id,
        Integer anuncioId,
        String tituloAnuncio,
        String motivo,
        String descricao,
        String status,
        String denunciante,
        String administrador,
        String parecerAdministrativo,
        List<String> imagens,
        String dataCriacao,
        String dataDecisao
) {
}
