package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId ORDER BY p.dataCriacao DESC")
    List<Pedido> findByClienteId(@Param("clienteId") Long clienteId);

    Page<Pedido> findByClienteIdOrderByDataCriacaoDesc(Long clienteId, Pageable pageable);

    @Query("SELECT p FROM Pedido p WHERE p.status = :status ORDER BY p.dataCriacao DESC")
    List<Pedido> findByStatus(@Param("status") String status);

    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.status = :status ORDER BY p.dataCriacao DESC")
    List<Pedido> findByClienteIdAndStatus(
            @Param("clienteId") Long clienteId,
            @Param("status") String status
    );

    @Query("SELECT p FROM Pedido p WHERE p.dataCriacao >= :dataInicio AND p.dataCriacao <= :dataFim ORDER BY p.dataCriacao DESC")
    List<Pedido> findByDataCriacaoBetween(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    @Query("SELECT p FROM Pedido p WHERE p.cliente.id = :clienteId AND p.dataCriacao >= :dataInicio AND p.dataCriacao <= :dataFim ORDER BY p.dataCriacao DESC")
    List<Pedido> findByClienteIdAndDataCriacaoBetween(
            @Param("clienteId") Long clienteId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    @Query("SELECT p FROM Pedido p ORDER BY p.status, p.dataCriacao DESC")
    List<Pedido> findAllOrderByStatusAndData();

    @Query("SELECT SUM(p.valorTotal) FROM Pedido p WHERE p.dataCriacao >= :dataInicio AND p.dataCriacao <= :dataFim AND (p.status = 'CONFIRMADO' OR p.status = 'ENTREGUE')")
    Double findFaturamentoTotalByPeriodo(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    long countByStatus(String status);
}
