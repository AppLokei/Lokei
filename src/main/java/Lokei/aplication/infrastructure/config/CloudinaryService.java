package Lokei.aplication.infrastructure.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    public Map<String, String> uploadImagem(MultipartFile imagem) {
        try {
            Map resultado = cloudinary.uploader().upload(imagem.getBytes(), ObjectUtils.emptyMap());
            Map<String, String> dados = new HashMap<>();
            dados.put("url", resultado.get("url").toString());
            dados.put("publicId", resultado.get("public_id").toString());
            return dados;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao fazer upload da imagem", e);
        }
    }

    public void deletarImagem(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao deletar imagem", e);
        }
    }
}