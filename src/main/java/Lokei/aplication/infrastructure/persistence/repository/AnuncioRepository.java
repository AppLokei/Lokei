package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entity.Anuncio;
import Lokei.aplication.infrastructure.persistence.enums.categoriaEnum;
import Lokei.aplication.infrastructure.persistence.enums.statusAnuncioEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AnuncioRepository extends JpaRepository<Anuncio, Integer> {

    @EntityGraph(attributePaths = {"proprietario", "proprietario.endereco", "imagens"})
    @Query("select a from Anuncio a where a.id = :id")
    Optional<Anuncio> findDetailedById(@Param("id") Integer id);

    @EntityGraph(attributePaths = {"imagens", "proprietario", "proprietario.endereco"})
    List<Anuncio> findByProprietario_IdOrderByDataCriacaoDesc(Integer proprietarioId);

    @Query("""
            select a from Anuncio a
            join a.proprietario p
            left join p.endereco e
            where a.status = Lokei.aplication.infrastructure.persistence.enums.statusAnuncioEnum.ATIVO
              and (:termo is null or lower(a.titulo) like lower(concat('%', :termo, '%'))
                   or lower(a.descricao) like lower(concat('%', :termo, '%')))
              and (:categoria is null or a.categoria = :categoria)
              and (:precoMin is null or a.valorDiario >= :precoMin)
              and (:precoMax is null or a.valorDiario <= :precoMax)
              and (:cidade is null or lower(e.cidade) = lower(:cidade))
            """)
    Page<Anuncio> buscarCatalogo(
            @Param("termo") String termo,
            @Param("categoria") categoriaEnum categoria,
            @Param("precoMin") BigDecimal precoMin,
            @Param("precoMax") BigDecimal precoMax,
            @Param("cidade") String cidade,
            Pageable pageable
    );

    @Query("""
            select a from Anuncio a
            left join Aluguel aluguel on aluguel.anuncio = a
            where a.status = Lokei.aplication.infrastructure.persistence.enums.statusAnuncioEnum.ATIVO
            group by a
            order by count(aluguel.id) desc, a.dataCriacao desc
            """)
    List<Anuncio> buscarPrincipais(Pageable pageable);
}
