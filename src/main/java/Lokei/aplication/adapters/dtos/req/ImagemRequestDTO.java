package Lokei.aplication.adapters.dtos.req;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record ImagemRequestDTO(
        @NotNull(message = "Adicione pelo menos uma foto para publicar seu anúncio")
        MultipartFile file
) {
}
