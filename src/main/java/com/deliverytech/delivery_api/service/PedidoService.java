package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service para gerenciar operações de Pedido.
 * Implementa criação, cálculo de valores, mudança de status e validações.
 */
@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Status válidos do pedido
    public static final String STATUS_PENDENTE = "PENDENTE";
    public static final String STATUS_CONFIRMADO = "CONFIRMADO";
    public static final String STATUS_EM_PREPARACAO = "EM_PREPARACAO";
    public static final String STATUS_SAIU_PARA_ENTREGA = "SAIU_PARA_ENTREGA";
    public static final String STATUS_ENTREGUE = "ENTREGUE";
    public static final String STATUS_CANCELADO = "CANCELADO";

    private static final BigDecimal VALOR_MINIMO = new BigDecimal("0.01");
    private static final BigDecimal VALOR_MAXIMO = new BigDecimal("999999.99");

    /**
     * Cria um novo pedido com validações.
     *
     * @param pedido Pedido a ser criado
     * @return Pedido criado
     * @throws IllegalArgumentException Se validações falharem
     */
    public Pedido criar(Pedido pedido) {
        validarCriacao(pedido);
        pedido.setStatus(STATUS_PENDENTE);
        pedido.setDataCriacao(LocalDateTime.now());
        return pedidoRepository.save(pedido);
    }

    /**
     * Valida os dados para criação de pedido.
     *
     * @param pedido Pedido a validar
     * @throws IllegalArgumentException Se dados inválidos
     */
    private void validarCriacao(Pedido pedido) {
        if (pedido.getCliente() == null || pedido.getCliente().getId() == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }

        // Verifica se cliente existe e está ativo
        Cliente cliente = clienteRepository.findById(pedido.getCliente().getId())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        if (!cliente.getAtivo()) {
            throw new IllegalArgumentException("Não é possível criar pedido para cliente inativo");
        }

        pedido.setCliente(cliente);

        validarValor(pedido.getValorTotal());
    }

    /**
     * Valida o valor do pedido.
     *
     * @param valor Valor a validar
     * @throws IllegalArgumentException Se valor inválido
     */
    private void validarValor(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor total é obrigatório");
        }

        if (valor.compareTo(VALOR_MINIMO) < 0) {
            throw new IllegalArgumentException("Valor total deve ser maior ou igual a R$ 0,01");
        }

        if (valor.compareTo(VALOR_MAXIMO) > 0) {
            throw new IllegalArgumentException("Valor total não pode exceder R$ 999.999,99");
        }
    }

    /**
     * Calcula o valor total do pedido com desconto (opcional).
     *
     * @param subtotal Subtotal do pedido
     * @param percentualDesconto Percentual de desconto (0-100)
     * @return Valor total calculado
     * @throws IllegalArgumentException Se valores inválidos
     */
    public BigDecimal calcularValorTotal(BigDecimal subtotal, Double percentualDesconto) {
        if (subtotal == null) {
            throw new IllegalArgumentException("Subtotal é obrigatório");
        }

        if (subtotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Subtotal deve ser maior que zero");
        }

        if (percentualDesconto == null) {
            percentualDesconto = 0.0;
        }

        if (percentualDesconto < 0 || percentualDesconto > 100) {
            throw new IllegalArgumentException("Percentual de desconto deve estar entre 0 e 100");
        }

        BigDecimal desconto = subtotal.multiply(new BigDecimal(percentualDesconto / 100));
        return subtotal.subtract(desconto);
    }

    /**
     * Calcula o valor total do pedido com desconto em valor fixo.
     *
     * @param subtotal Subtotal do pedido
     * @param descontoFixo Desconto em valor fixo
     * @return Valor total calculado
     * @throws IllegalArgumentException Se valores inválidos
     */
    public BigDecimal calcularValorTotalComDescontoFixo(BigDecimal subtotal, BigDecimal descontoFixo) {
        if (subtotal == null) {
            throw new IllegalArgumentException("Subtotal é obrigatório");
        }

        if (subtotal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Subtotal deve ser maior que zero");
        }

        if (descontoFixo == null) {
            descontoFixo = BigDecimal.ZERO;
        }

        if (descontoFixo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Desconto não pode ser negativo");
        }

        if (descontoFixo.compareTo(subtotal) > 0) {
            throw new IllegalArgumentException("Desconto não pode ser maior que o subtotal");
        }

        return subtotal.subtract(descontoFixo);
    }

    /**
     * Busca um pedido por ID.
     *
     * @param id ID do pedido
     * @return Pedido encontrado
     * @throws IllegalArgumentException Se pedido não existe
     */
    public Pedido buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID do pedido inválido");
        }

        return pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
    }

    /**
     * Lista todos os pedidos de um cliente.
     *
     * @param clienteId ID do cliente
     * @return Lista de pedidos do cliente
     */
    public List<Pedido> listarPorCliente(Long clienteId) {
        if (clienteId == null || clienteId <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        return pedidoRepository.findByClienteId(clienteId);
    }

    /**
     * Lista pedidos de um cliente com status específico.
     *
     * @param clienteId ID do cliente
     * @param status Status do pedido
     * @return Lista de pedidos filtrados
     */
    public List<Pedido> listarPorClienteEStatus(Long clienteId, String status) {
        if (clienteId == null || clienteId <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        validarStatus(status);

        clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        return pedidoRepository.findByClienteIdAndStatus(clienteId, status);
    }

    /**
     * Lista todos os pedidos com um status específico.
     *
     * @param status Status do pedido
     * @return Lista de pedidos com o status
     */
    public List<Pedido> listarPorStatus(String status) {
        validarStatus(status);
        return pedidoRepository.findByStatus(status);
    }

    /**
     * Lista todos os pedidos ordenados por status e data de criação.
     *
     * @return Lista de todos os pedidos
     */
    public List<Pedido> listarTodos() {
        return pedidoRepository.findAllOrderByStatusAndData();
    }

    /**
     * Busca pedidos por período de datas.
     *
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @return Lista de pedidos no período
     */
    public List<Pedido> listarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de início e fim são obrigatórias");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data inicial não pode ser após data final");
        }

        return pedidoRepository.findByDataCriacaoBetween(dataInicio, dataFim);
    }

    /**
     * Busca pedidos de um cliente em um período específico.
     *
     * @param clienteId ID do cliente
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @return Lista de pedidos filtrados
     */
    public List<Pedido> listarPorClienteEPeriodo(Long clienteId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (clienteId == null || clienteId <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de início e fim são obrigatórias");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data inicial não pode ser após data final");
        }

        clienteRepository.findById(clienteId)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        return pedidoRepository.findByClienteIdAndDataCriacaoBetween(clienteId, dataInicio, dataFim);
    }

    /**
     * Valida um status de pedido.
     *
     * @param status Status a validar
     * @throws IllegalArgumentException Se status inválido
     */
    private void validarStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status do pedido é obrigatório");
        }

        String[] statusValidos = {STATUS_PENDENTE, STATUS_CONFIRMADO, STATUS_EM_PREPARACAO,
                STATUS_SAIU_PARA_ENTREGA, STATUS_ENTREGUE, STATUS_CANCELADO};

        boolean statusValido = false;
        for (String s : statusValidos) {
            if (s.equals(status)) {
                statusValido = true;
                break;
            }
        }

        if (!statusValido) {
            throw new IllegalArgumentException("Status inválido. Status válidos: " +
                    "PENDENTE, CONFIRMADO, EM_PREPARACAO, SAIU_PARA_ENTREGA, ENTREGUE, CANCELADO");
        }
    }

    /**
     * Altera o status de um pedido.
     *
     * @param id ID do pedido
     * @param novoStatus Novo status
     * @return Pedido com status alterado
     * @throws IllegalArgumentException Se transição de status inválida
     */
    public Pedido alterarStatus(Long id, String novoStatus) {
        Pedido pedido = buscarPorId(id);
        validarStatus(novoStatus);

        // Valida transições de status permitidas
        validarTransicaoStatus(pedido.getStatus(), novoStatus);

        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }

    /**
     * Valida transições de status permitidas.
     *
     * @param statusAtual Status atual do pedido
     * @param novoStatus Novo status desejado
     * @throws IllegalArgumentException Se transição não permitida
     */
    private void validarTransicaoStatus(String statusAtual, String novoStatus) {
        if (statusAtual.equals(novoStatus)) {
            throw new IllegalArgumentException("Pedido já possui este status");
        }

        // Pedidos cancelados não podem ser alterados
        if (STATUS_CANCELADO.equals(statusAtual)) {
            throw new IllegalArgumentException("Pedidos cancelados não podem ser alterados");
        }

        // Pedidos entregues não podem ser alterados
        if (STATUS_ENTREGUE.equals(statusAtual)) {
            throw new IllegalArgumentException("Pedidos entregues não podem ser alterados");
        }

        // Define transições válidas
        switch (statusAtual) {
            case STATUS_PENDENTE:
                if (!STATUS_CONFIRMADO.equals(novoStatus) && !STATUS_CANCELADO.equals(novoStatus)) {
                    throw new IllegalArgumentException("Status PENDENTE só pode transitar para CONFIRMADO ou CANCELADO");
                }
                break;

            case STATUS_CONFIRMADO:
                if (!STATUS_EM_PREPARACAO.equals(novoStatus) && !STATUS_CANCELADO.equals(novoStatus)) {
                    throw new IllegalArgumentException("Status CONFIRMADO só pode transitar para EM_PREPARACAO ou CANCELADO");
                }
                break;

            case STATUS_EM_PREPARACAO:
                if (!STATUS_SAIU_PARA_ENTREGA.equals(novoStatus) && !STATUS_CANCELADO.equals(novoStatus)) {
                    throw new IllegalArgumentException("Status EM_PREPARACAO só pode transitar para SAIU_PARA_ENTREGA ou CANCELADO");
                }
                break;

            case STATUS_SAIU_PARA_ENTREGA:
                if (!STATUS_ENTREGUE.equals(novoStatus) && !STATUS_CANCELADO.equals(novoStatus)) {
                    throw new IllegalArgumentException("Status SAIU_PARA_ENTREGA só pode transitar para ENTREGUE ou CANCELADO");
                }
                break;

            default:
                throw new IllegalArgumentException("Transição de status desconhecida");
        }
    }

    /**
     * Confirma um pedido (transição PENDENTE -> CONFIRMADO).
     *
     * @param id ID do pedido
     * @return Pedido confirmado
     */
    public Pedido confirmar(Long id) {
        return alterarStatus(id, STATUS_CONFIRMADO);
    }

    /**
     * Marca um pedido como em preparação (transição CONFIRMADO -> EM_PREPARACAO).
     *
     * @param id ID do pedido
     * @return Pedido em preparação
     */
    public Pedido iniciarPreparacao(Long id) {
        return alterarStatus(id, STATUS_EM_PREPARACAO);
    }

    /**
     * Marca um pedido como saído para entrega (transição EM_PREPARACAO -> SAIU_PARA_ENTREGA).
     *
     * @param id ID do pedido
     * @return Pedido saído para entrega
     */
    public Pedido sairParaEntrega(Long id) {
        return alterarStatus(id, STATUS_SAIU_PARA_ENTREGA);
    }

    /**
     * Marca um pedido como entregue (transição SAIU_PARA_ENTREGA -> ENTREGUE).
     *
     * @param id ID do pedido
     * @return Pedido entregue
     */
    public Pedido entregar(Long id) {
        return alterarStatus(id, STATUS_ENTREGUE);
    }

    /**
     * Cancela um pedido.
     *
     * @param id ID do pedido
     * @return Pedido cancelado
     */
    public Pedido cancelar(Long id) {
        return alterarStatus(id, STATUS_CANCELADO);
    }

    /**
     * Retorna a quantidade total de pedidos com um status específico.
     *
     * @param status Status do pedido
     * @return Quantidade de pedidos
     */
    public long contarPorStatus(String status) {
        validarStatus(status);
        return pedidoRepository.countByStatus(status);
    }

    /**
     * Calcula o faturamento total de um período.
     *
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @return Faturamento total (apenas pedidos CONFIRMADO e ENTREGUE)
     */
    public Double obterFaturamentoTotalPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (dataInicio == null || dataFim == null) {
            throw new IllegalArgumentException("Datas de início e fim são obrigatórias");
        }

        if (dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("Data inicial não pode ser após data final");
        }

        Double faturamento = pedidoRepository.findFaturamentoTotalByPeriodo(dataInicio, dataFim);
        return faturamento != null ? faturamento : 0.0;
    }

    /**
     * Deleta permanentemente um pedido do sistema.
     *
     * @param id ID do pedido
     * @throws IllegalArgumentException Se pedido não existe
     */
    public void deletar(Long id) {
        Pedido pedido = buscarPorId(id);
        pedidoRepository.delete(pedido);
    }
}