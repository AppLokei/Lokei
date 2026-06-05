package Lokei.aplication.infrastructure.persistence.specification;

import Lokei.aplication.domain.enums.CategoriaEnum;
import Lokei.aplication.infrastructure.persistence.entities.AnuncioEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class AnuncioSpecification {

    public static Specification<AnuncioEntity> nomeContem(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isBlank()) {
                return null;
            }

            return cb.or(
                    cb.like(cb.lower(root.get("titulo")), "%" + nome.toLowerCase() + "%"),
                    cb.like(cb.lower(root.join("ferramenta").get("nome")), "%" + nome.toLowerCase() + "%")
            );
        };
    }

    public static Specification<AnuncioEntity> categoriaIgual(CategoriaEnum categoria) {
        return (root, query, cb) -> {
            if (categoria == null) {
                return null;
            }

            return cb.equal(root.join("ferramenta").get("categoria"), categoria);
        };
    }

    public static Specification<AnuncioEntity> valorEntre(BigDecimal min, BigDecimal max) {
        return (root, query, cb) -> {

            if (min == null && max == null) {
                return null;
            }

            if (min != null && max != null) {
                return cb.between(root.get("valorDiario"), min, max);
            }

            if (min != null) {
                return cb.greaterThanOrEqualTo(root.get("valorDiario"), min);
            }

            return cb.lessThanOrEqualTo(root.get("valorDiario"), max);
        };
    }
}
