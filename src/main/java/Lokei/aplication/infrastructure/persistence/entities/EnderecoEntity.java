package Lokei.aplication.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_endereco")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EnderecoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String logradouro;
    private String bairro;
    private Integer numero;
    private String cidade;
    private String estado;
    private String cep;
}
