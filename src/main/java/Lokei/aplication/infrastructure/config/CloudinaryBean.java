package Lokei.aplication.infrastructure.config;

import com.cloudinary.Cloudinary;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryBean {

    /**
     * Valor injetado pelo Spring a partir da propriedade cloudinary.url.
     * Em produção/dev: definir via .env ou variável de ambiente CLOUDINARY_URL.
     * Em testes: definido em application-test.properties como placeholder.
     */
    @Value("${cloudinary.url}")
    private String cloudinaryUrl;

    @Bean
    public Cloudinary cloudinary() {
        // Tenta carregar do .env (dev local); se não existir, usa a propriedade Spring
        try {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            String urlFromDotenv = dotenv.get("CLOUDINARY_URL");
            if (urlFromDotenv != null && !urlFromDotenv.isBlank()) {
                return new Cloudinary(urlFromDotenv);
            }
        } catch (Exception ignored) {
            // .env ausente ou inválido — usa o valor injetado pelo Spring
        }
        return new Cloudinary(cloudinaryUrl);
    }
}

