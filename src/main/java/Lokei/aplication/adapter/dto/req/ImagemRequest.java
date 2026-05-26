package Lokei.aplication.adapter.dto.req;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record ImagemRequest(
        @NotNull(message = "Adicione pelo menos uma foto para publicar seu anúncio")
        MultipartFile file
) {
}
