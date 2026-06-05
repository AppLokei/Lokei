package Lokei.aplication.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_imagem")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImagemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "imagem_url")
    private String imagemUrl;

    @Column(name = "public_id")
    private String publicId;

    @ManyToOne
    @JoinColumn(name = "anuncio_id")
    private AnuncioEntity anuncio;
}
