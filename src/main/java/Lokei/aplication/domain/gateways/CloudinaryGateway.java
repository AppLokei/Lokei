package Lokei.aplication.domain.gateways;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryGateway {
    String upload(MultipartFile file) throws IOException;

}
