package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId ORDER BY p.nome ASC")
    List<Produto> findByRestauranteId(@Param("restauranteId") Long restauranteId);

    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.disponivel = true ORDER BY p.nome ASC")
    List<Produto> findByRestauranteIdAndDisponivel(@Param("restauranteId") Long restauranteId);

    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.categoria = :categoria AND p.disponivel = true ORDER BY p.nome ASC")
    List<Produto> findByRestauranteIdAndCategoria(
            @Param("restauranteId") Long restauranteId,
            @Param("categoria") String categoria
    );

    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.categoria = :categoria AND p.disponivel = true")
    Page<Produto> findByRestauranteIdAndCategoriaWithPagination(
            @Param("restauranteId") Long restauranteId,
            @Param("categoria") String categoria,
            Pageable pageable
    );

    @Query("SELECT p FROM Produto p WHERE p.restaurante.id = :restauranteId AND p.disponivel = false ORDER BY p.nome ASC")
    List<Produto> findByRestauranteIdAndIndisponivel(@Param("restauranteId") Long restauranteId);

    long countByRestauranteIdAndDisponivel(Long restauranteId, boolean disponivel);
}
