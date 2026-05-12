package Lokei.aplication.infrastructure.gateway;

import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.gateway.CloudinaryGateway;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class CloudinaryGatewayImpl implements CloudinaryGateway {

    private final Cloudinary cloudinary;

    public CloudinaryGatewayImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "use_filename", true,
                        "unique_filename", true,
                        "overwrite", true
                )
        );

        return uploadResult.get("secure_url").toString();
    }
}
