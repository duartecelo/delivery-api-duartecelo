package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Restaurante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Long> {

    Optional<Restaurante> findByNome(String nome);

    @Query("SELECT r FROM Restaurante r WHERE LOWER(r.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY r.nome ASC")
    List<Restaurante> findByNomeContaining(@Param("nome") String nome);

    @Query("SELECT r FROM Restaurante r WHERE r.categoria = :categoria AND r.ativo = true ORDER BY r.avaliacao DESC")
    List<Restaurante> findByCategoria(@Param("categoria") String categoria);

    @Query("SELECT r FROM Restaurante r WHERE r.ativo = true ORDER BY r.avaliacao DESC, r.nome ASC")
    List<Restaurante> findAllAtivosOrderByAvaliacao();

    Page<Restaurante> findByAtivoTrue(Pageable pageable);

    @Query("SELECT r FROM Restaurante r WHERE r.categoria = :categoria AND r.ativo = true")
    Page<Restaurante> findByCategoriaAndAtivoTrue(@Param("categoria") String categoria, Pageable pageable);

    long countByCategoriaAndAtivoTrue(String categoria);
}
