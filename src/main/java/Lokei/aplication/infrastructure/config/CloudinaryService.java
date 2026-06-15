package Lokei.aplication.infrastructure.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Value("${app.cloudinary.enabled:true}")
    private boolean cloudinaryEnabled;

    public Map<String, String> uploadImagem(MultipartFile imagem) {
        if (!cloudinaryEnabled) {
            Map<String, String> dados = new HashMap<>();
            String nome = imagem.getOriginalFilename();
            dados.put("url", "https://local.placeholder/" + nome);
            dados.put("publicId", "local-" + nome);
            return dados;
        }
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
        if (!cloudinaryEnabled) {
            return;
        }
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            throw new RuntimeException("Erro ao deletar imagem", e);
        }
    }
}