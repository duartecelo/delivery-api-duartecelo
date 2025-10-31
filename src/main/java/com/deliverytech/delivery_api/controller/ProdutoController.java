package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ApiResponse;
import com.deliverytech.delivery_api.dto.ErrorResponse;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller REST para gerenciamento de Produtos.
 *
 * Endpoints disponíveis:
 * - POST /api/v1/produtos - Criar novo produto
 * - GET /api/v1/produtos - Listar todos os produtos
 * - GET /api/v1/produtos/{id} - Buscar produto por ID
 * - PUT /api/v1/produtos/{id} - Atualizar produto
 * - DELETE /api/v1/produtos/{id} - Deletar produto
 * - GET /api/v1/produtos/restaurante/{restauranteId} - Buscar por restaurante
 */
@RestController
@RequestMapping("/api/v1/produtos")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    /**
     * POST - Criar um novo produto
     *
     * @param produto Dados do produto a ser criado
     * @return Produto criado com status 201 CREATED
     */
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Produto produto) {
        try {
            if (produto == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Produto não pode ser nulo", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Produto produtoCriado = produtoService.cadastrar(produto);
            ApiResponse<Produto> response = new ApiResponse<>("Produto criado com sucesso", produtoCriado, true);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao criar produto: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Buscar produto por ID
     *
     * @param id ID do produto
     * @return Produto encontrado com status 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Produto produto = produtoService.buscarPorId(id);
            ApiResponse<Produto> response = new ApiResponse<>("Produto encontrado", produto, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao buscar produto: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Listar todos os produtos de um restaurante
     *
     * @param restauranteId ID do restaurante
     * @return Lista de produtos do restaurante com status 200 OK
     */
    @GetMapping("/restaurante/{restauranteId}")
    public ResponseEntity<?> listarPorRestaurante(@PathVariable Long restauranteId) {
        try {
            if (restauranteId == null || restauranteId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID do restaurante inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            List<Produto> produtos = produtoService.listarPorRestaurante(restauranteId);
            ApiResponse<List<Produto>> response = new ApiResponse<>(
                    "Total de " + produtos.size() + " produtos encontrados",
                    produtos,
                    true
            );
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao listar produtos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Listar produtos disponíveis de um restaurante
     *
     * @param restauranteId ID do restaurante
     * @return Lista de produtos disponíveis com status 200 OK
     */
    @GetMapping("/restaurante/{restauranteId}/disponiveis")
    public ResponseEntity<?> listarDisponiveisPorRestaurante(@PathVariable Long restauranteId) {
        try {
            if (restauranteId == null || restauranteId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID do restaurante inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            List<Produto> produtos = produtoService.listarDisponiveisPorRestaurante(restauranteId);
            ApiResponse<List<Produto>> response = new ApiResponse<>(
                    "Total de " + produtos.size() + " produtos disponíveis encontrados",
                    produtos,
                    true
            );
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao listar produtos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Listar produtos indisponíveis de um restaurante
     *
     * @param restauranteId ID do restaurante
     * @return Lista de produtos indisponíveis com status 200 OK
     */
    @GetMapping("/restaurante/{restauranteId}/indisponíveis")
    public ResponseEntity<?> listarIndisponiveisPorRestaurante(@PathVariable Long restauranteId) {
        try {
            if (restauranteId == null || restauranteId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID do restaurante inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            List<Produto> produtos = produtoService.listarIndisponiveisPorRestaurante(restauranteId);
            ApiResponse<List<Produto>> response = new ApiResponse<>(
                    "Total de " + produtos.size() + " produtos indisponíveis encontrados",
                    produtos,
                    true
            );
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao listar produtos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Listar produtos por categoria e restaurante
     *
     * @param restauranteId ID do restaurante
     * @param categoria Categoria do produto
     * @return Lista de produtos da categoria com status 200 OK
     */
    @GetMapping("/restaurante/{restauranteId}/categoria/{categoria}")
    public ResponseEntity<?> listarPorCategoriaERestaurante(@PathVariable Long restauranteId, @PathVariable String categoria) {
        try {
            if (restauranteId == null || restauranteId <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID do restaurante inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            if (categoria == null || categoria.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Categoria inválida", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            List<Produto> produtos = produtoService.listarPorCategoriaERestaurante(restauranteId, categoria);
            ApiResponse<List<Produto>> response = new ApiResponse<>(
                    "Total de " + produtos.size() + " produtos encontrados na categoria '" + categoria + "'",
                    produtos,
                    true
            );
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao listar produtos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT - Atualizar produto
     *
     * @param id ID do produto
     * @param produtoAtualizado Dados atualizados
     * @return Produto atualizado com status 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Produto produtoAtualizado) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            if (produtoAtualizado == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Dados do produto não podem ser nulos", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Produto produto = produtoService.atualizar(id, produtoAtualizado);
            ApiResponse<Produto> response = new ApiResponse<>("Produto atualizado com sucesso", produto, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao atualizar produto: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT - Ativar disponibilidade do produto
     *
     * @param id ID do produto
     * @return Produto com disponibilidade ativada com status 200 OK
     */
    @PutMapping("/{id}/ativar")
    public ResponseEntity<?> ativar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Produto produto = produtoService.ativarDisponibilidade(id);
            ApiResponse<Produto> response = new ApiResponse<>("Produto ativado com sucesso", produto, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao ativar produto: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT - Desativar disponibilidade do produto
     *
     * @param id ID do produto
     * @return Produto com disponibilidade desativada com status 200 OK
     */
    @PutMapping("/{id}/desativar")
    public ResponseEntity<?> desativar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Produto produto = produtoService.desativarDisponibilidade(id);
            ApiResponse<Produto> response = new ApiResponse<>("Produto desativado com sucesso", produto, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao desativar produto: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * DELETE - Deletar produto
     *
     * @param id ID do produto
     * @return Status 204 NO CONTENT se sucesso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            produtoService.deletar(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao deletar produto: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}