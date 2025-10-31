package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ApiResponse;
import com.deliverytech.delivery_api.dto.ErrorResponse;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller REST para gerenciamento de Clientes.
 *
 * Endpoints disponíveis:
 * - POST /api/v1/clientes - Criar novo cliente
 * - GET /api/v1/clientes - Listar todos os clientes ativos
 * - GET /api/v1/clientes/{id} - Buscar cliente por ID
 * - PUT /api/v1/clientes/{id} - Atualizar cliente
 * - DELETE /api/v1/clientes/{id} - Deletar cliente
 * - GET /api/v1/clientes/email/{email} - Buscar cliente por e-mail
 */
@RestController
@RequestMapping("/api/v1/clientes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * POST - Criar um novo cliente
     *
     * @param cliente Dados do cliente a ser criado
     * @return Cliente criado com status 201 CREATED
     * @throws IllegalArgumentException Se dados inválidos
     */
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Cliente cliente) {
        try {
            if (cliente == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Cliente não pode ser nulo", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Cliente clienteCriado = clienteService.cadastrar(cliente);
            ApiResponse<Cliente> response = new ApiResponse<>("Cliente criado com sucesso", clienteCriado, true);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao criar cliente: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Listar todos os clientes ativos
     *
     * @return Lista de clientes ativos com status 200 OK
     */
    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<Cliente> clientes = clienteService.listarAtivos();
            ApiResponse<List<Cliente>> response = new ApiResponse<>(
                    "Total de " + clientes.size() + " clientes encontrados",
                    clientes,
                    true
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao listar clientes: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Buscar cliente por ID
     *
     * @param id ID do cliente
     * @return Cliente encontrado com status 200 OK
     * @throws IllegalArgumentException Se cliente não existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Cliente cliente = clienteService.buscarPorId(id);
            ApiResponse<Cliente> response = new ApiResponse<>("Cliente encontrado", cliente, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao buscar cliente: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Buscar cliente por e-mail
     *
     * @param email E-mail do cliente
     * @return Cliente encontrado com status 200 OK
     * @throws IllegalArgumentException Se cliente não existe ou está inativo
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Email inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Cliente cliente = clienteService.buscarPorEmail(email);
            ApiResponse<Cliente> response = new ApiResponse<>("Cliente encontrado", cliente, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao buscar cliente: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT - Atualizar dados do cliente
     *
     * @param id ID do cliente
     * @param clienteAtualizado Dados atualizados
     * @return Cliente atualizado com status 200 OK
     * @throws IllegalArgumentException Se cliente não existe ou dados inválidos
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            if (clienteAtualizado == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Dados do cliente não podem ser nulos", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Cliente cliente = clienteService.atualizar(id, clienteAtualizado);
            ApiResponse<Cliente> response = new ApiResponse<>("Cliente atualizado com sucesso", cliente, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao atualizar cliente: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * DELETE - Deletar um cliente
     *
     * @param id ID do cliente
     * @return Status 204 NO CONTENT se sucesso
     * @throws IllegalArgumentException Se cliente não existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            clienteService.deletar(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao deletar cliente: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT - Inativar um cliente
     *
     * @param id ID do cliente
     * @return Cliente inativado com status 200 OK
     */
    @PutMapping("/{id}/inativar")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Cliente cliente = clienteService.inativar(id);
            ApiResponse<Cliente> response = new ApiResponse<>("Cliente inativado com sucesso", cliente, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao inativar cliente: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT - Ativar um cliente inativo
     *
     * @param id ID do cliente
     * @return Cliente ativado com status 200 OK
     */
    @PutMapping("/{id}/ativar")
    public ResponseEntity<?> ativar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Cliente cliente = clienteService.ativar(id);
            ApiResponse<Cliente> response = new ApiResponse<>("Cliente ativado com sucesso", cliente, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao ativar cliente: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Contar clientes ativos
     *
     * @return Total de clientes ativos com status 200 OK
     */
    @GetMapping("/info/total-ativos")
    public ResponseEntity<?> contarAtivos() {
        try {
            long total = clienteService.contarAtivos();
            ApiResponse<Long> response = new ApiResponse<>("Total de clientes ativos", total, true);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao contar clientes: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}