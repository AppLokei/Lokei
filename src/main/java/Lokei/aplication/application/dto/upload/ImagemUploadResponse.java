package Lokei.aplication.application.dto.upload;

public record ImagemUploadResponse(
        Integer imagemId,
        String nomeArquivo,
        String url,
        String contentType,
        long tamanhoBytes
) {
}
