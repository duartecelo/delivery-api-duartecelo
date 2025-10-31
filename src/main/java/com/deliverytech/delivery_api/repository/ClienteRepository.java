package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmail(String email);

    @Query("SELECT c FROM Cliente c WHERE c.ativo = true ORDER BY c.nome ASC")
    List<Cliente> findAllAtivos();

    @Query("SELECT c FROM Cliente c WHERE c.email = :email AND c.ativo = true")
    Optional<Cliente> findByEmailAndAtivoTrue(@Param("email") String email);

    long countByAtivoTrue();
}
