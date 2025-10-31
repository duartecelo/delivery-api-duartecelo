package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ApiResponse;
import com.deliverytech.delivery_api.dto.ErrorResponse;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller REST para gerenciamento de Pedidos.
 *
 * Endpoints disponíveis:
 * - POST /api/v1/pedidos - Criar novo pedido
 * - GET /api/v1/pedidos/{id} - Buscar pedido por ID
 * - GET /api/v1/pedidos/cliente/{clienteId} - Listar pedidos do cliente
 * - GET /api/v1/pedidos/status/{status} - Listar por status
 * - PUT /api/v1/pedidos/{id}/status - Atualizar status
 * - PUT /api/v1/pedidos/{id}/confirmar - Confirmar pedido
 * - PUT /api/v1/pedidos/{id}/cancelar - Cancelar pedido
 */
@RestController
@RequestMapping("/api/v1/pedidos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /**
     * POST - Criar um novo pedido
     *
     * @param pedido Dados do pedido a ser criado
     * @return Pedido criado com status 201 CREATED
     */
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Pedido pedido) {
        try {
            if (pedido == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Pedido não pode ser nulo", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Pedido pedidoCriado = pedidoService.criar(pedido);
            ApiResponse<Pedido> response = new ApiResponse<>("Pedido criado com sucesso", pedidoCriado, true);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao criar pedido: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Buscar pedido por ID
     *
     * @param id ID do pedido
     * @return Pedido encontrado com status 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Pedido pedido = pedidoService.buscarPorId(id);
            ApiResponse<Pedido> response = new ApiResponse<>("Pedido encontrado", pedido, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao buscar pedido: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Listar todos os pedidos de um cliente
     *
     * @param clienteId ID do cliente
     * @return Lista de pedidos do cliente com status 200 OK
     */
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> listarPorCliente(@PathVariable Long clienteId) {
        try {
            if (clienteId == null || clienteId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID do cliente inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            List<Pedido> pedidos = pedidoService.listarPorCliente(clienteId);
            ApiResponse<List<Pedido>> response = new ApiResponse<>(
                    "Total de " + pedidos.size() + " pedidos encontrados",
                    pedidos,
                    true
            );
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao listar pedidos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Listar pedidos por status
     *
     * @param status Status do pedido (PENDENTE, CONFIRMADO, EM_PREPARACAO, SAIU_PARA_ENTREGA, ENTREGUE, CANCELADO)
     * @return Lista de pedidos com o status especificado com status 200 OK
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<?> listarPorStatus(@PathVariable String status) {
        try {
            if (status == null || status.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Status inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            List<Pedido> pedidos = pedidoService.listarPorStatus(status);
            ApiResponse<List<Pedido>> response = new ApiResponse<>(
                    "Total de " + pedidos.size() + " pedidos com status '" + status + "' encontrados",
                    pedidos,
                    true
            );
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao listar pedidos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Listar pedidos de um cliente com status específico
     *
     * @param clienteId ID do cliente
     * @param status Status do pedido
     * @return Lista de pedidos com filtro combinado com status 200 OK
     */
    @GetMapping("/cliente/{clienteId}/status/{status}")
    public ResponseEntity<?> listarPorClienteEStatus(@PathVariable Long clienteId, @PathVariable String status) {
        try {
            if (clienteId == null || clienteId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID do cliente inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            if (status == null || status.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Status inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            List<Pedido> pedidos = pedidoService.listarPorClienteEStatus(clienteId, status);
            ApiResponse<List<Pedido>> response = new ApiResponse<>(
                    "Total de " + pedidos.size() + " pedidos encontrados",
                    pedidos,
                    true
            );
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao listar pedidos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Listar todos os pedidos
     *
     * @return Lista de todos os pedidos com status 200 OK
     */
    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<Pedido> pedidos = pedidoService.listarTodos();
            ApiResponse<List<Pedido>> response = new ApiResponse<>(
                    "Total de " + pedidos.size() + " pedidos encontrados",
                    pedidos,
                    true
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao listar pedidos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT - Atualizar status do pedido
     *
     * @param id ID do pedido
     * @param novoStatus Novo status do pedido
     * @return Pedido com status atualizado com status 200 OK
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<?> alterarStatus(@PathVariable Long id, @RequestParam String novoStatus) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            if (novoStatus == null || novoStatus.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Status inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Pedido pedido = pedidoService.alterarStatus(id, novoStatus);
            ApiResponse<Pedido> response = new ApiResponse<>("Status do pedido atualizado com sucesso", pedido, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao alterar status: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT - Confirmar pedido (PENDENTE -> CONFIRMADO)
     *
     * @param id ID do pedido
     * @return Pedido confirmado com status 200 OK
     */
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Pedido pedido = pedidoService.confirmar(id);
            ApiResponse<Pedido> response = new ApiResponse<>("Pedido confirmado com sucesso", pedido, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao confirmar pedido: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT - Iniciar preparação (CONFIRMADO -> EM_PREPARACAO)
     *
     * @param id ID do pedido
     * @return Pedido em preparação com status 200 OK
     */
    @PutMapping("/{id}/iniciar-preparacao")
    public ResponseEntity<?> iniciarPreparacao(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Pedido pedido = pedidoService.iniciarPreparacao(id);
            ApiResponse<Pedido> response = new ApiResponse<>("Preparação do pedido iniciada", pedido, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao iniciar preparação: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT - Sair para entrega (EM_PREPARACAO -> SAIU_PARA_ENTREGA)
     *
     * @param id ID do pedido
     * @return Pedido saído para entrega com status 200 OK
     */
    @PutMapping("/{id}/sair-para-entrega")
    public ResponseEntity<?> sairParaEntrega(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Pedido pedido = pedidoService.sairParaEntrega(id);
            ApiResponse<Pedido> response = new ApiResponse<>("Pedido saiu para entrega", pedido, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao sair para entrega: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT - Entregar pedido (SAIU_PARA_ENTREGA -> ENTREGUE)
     *
     * @param id ID do pedido
     * @return Pedido entregue com status 200 OK
     */
    @PutMapping("/{id}/entregar")
    public ResponseEntity<?> entregar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Pedido pedido = pedidoService.entregar(id);
            ApiResponse<Pedido> response = new ApiResponse<>("Pedido entregue com sucesso", pedido, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao entregar pedido: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT - Cancelar pedido
     *
     * @param id ID do pedido
     * @return Pedido cancelado com status 200 OK
     */
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Pedido pedido = pedidoService.cancelar(id);
            ApiResponse<Pedido> response = new ApiResponse<>("Pedido cancelado com sucesso", pedido, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao cancelar pedido: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Contar pedidos por status
     *
     * @param status Status do pedido
     * @return Total de pedidos com o status especificado
     */
    @GetMapping("/info/contar-status/{status}")
    public ResponseEntity<?> contarPorStatus(@PathVariable String status) {
        try {
            if (status == null || status.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Status inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            long total = pedidoService.contarPorStatus(status);
            ApiResponse<Long> response = new ApiResponse<>("Total de pedidos com status '" + status + "'", total, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao contar: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Obter faturamento total em um período
     *
     * @param dataInicio Data inicial (ISO 8601: YYYY-MM-DDTHH:MM:SS)
     * @param dataFim Data final (ISO 8601: YYYY-MM-DDTHH:MM:SS)
     * @return Faturamento total (apenas pedidos CONFIRMADO e ENTREGUE)
     */
    @GetMapping("/relatorio/faturamento")
    public ResponseEntity<?> obterFaturamento(@RequestParam LocalDateTime dataInicio, @RequestParam LocalDateTime dataFim) {
        try {
            if (dataInicio == null || dataFim == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Datas são obrigatórias", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Double faturamento = pedidoService.obterFaturamentoTotalPeriodo(dataInicio, dataFim);
            ApiResponse<Double> response = new ApiResponse<>("Faturamento total do período", faturamento, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao obter faturamento: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * DELETE - Deletar pedido
     *
     * @param id ID do pedido
     * @return Status 204 NO CONTENT se sucesso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            pedidoService.deletar(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao deletar pedido: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}