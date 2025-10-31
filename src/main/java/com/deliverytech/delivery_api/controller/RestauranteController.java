package com.deliverytech.delivery_api.controller;

import com.deliverytech.delivery_api.dto.ApiResponse;
import com.deliverytech.delivery_api.dto.ErrorResponse;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller REST para gerenciamento de Restaurantes.
 *
 * Endpoints disponíveis:
 * - POST /api/v1/restaurantes - Criar novo restaurante
 * - GET /api/v1/restaurantes - Listar todos os restaurantes
 * - GET /api/v1/restaurantes/{id} - Buscar restaurante por ID
 * - PUT /api/v1/restaurantes/{id} - Atualizar restaurante
 * - DELETE /api/v1/restaurantes/{id} - Deletar restaurante
 * - GET /api/v1/restaurantes/categoria/{categoria} - Buscar por categoria
 */
@RestController
@RequestMapping("/api/v1/restaurantes")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    /**
     * POST - Criar um novo restaurante
     *
     * @param restaurante Dados do restaurante a ser criado
     * @return Restaurante criado com status 201 CREATED
     */
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Restaurante restaurante) {
        try {
            if (restaurante == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Restaurante não pode ser nulo", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Restaurante restauranteCriado = restauranteService.cadastrar(restaurante);
            ApiResponse<Restaurante> response = new ApiResponse<>("Restaurante criado com sucesso", restauranteCriado, true);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao criar restaurante: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Listar todos os restaurantes
     *
     * @return Lista de restaurantes com status 200 OK
     */
    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<Restaurante> restaurantes = restauranteService.listarTodos();
            ApiResponse<List<Restaurante>> response = new ApiResponse<>(
                    "Total de " + restaurantes.size() + " restaurantes encontrados",
                    restaurantes,
                    true
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao listar restaurantes: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Listar apenas restaurantes ativos ordenados por avaliação
     *
     * @return Lista de restaurantes ativos com status 200 OK
     */
    @GetMapping("/ativos")
    public ResponseEntity<?> listarAtivos() {
        try {
            List<Restaurante> restaurantes = restauranteService.listarAtivos();
            ApiResponse<List<Restaurante>> response = new ApiResponse<>(
                    "Total de " + restaurantes.size() + " restaurantes ativos encontrados",
                    restaurantes,
                    true
            );
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao listar restaurantes ativos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Buscar restaurante por ID
     *
     * @param id ID do restaurante
     * @return Restaurante encontrado com status 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Restaurante restaurante = restauranteService.buscarPorId(id);
            ApiResponse<Restaurante> response = new ApiResponse<>("Restaurante encontrado", restaurante, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao buscar restaurante: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Buscar restaurantes por categoria
     *
     * @param categoria Categoria do restaurante
     * @return Lista de restaurantes da categoria com status 200 OK
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<?> buscarPorCategoria(@PathVariable String categoria) {
        try {
            if (categoria == null || categoria.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Categoria inválida", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            List<Restaurante> restaurantes = restauranteService.buscarPorCategoria(categoria);
            ApiResponse<List<Restaurante>> response = new ApiResponse<>(
                    "Total de " + restaurantes.size() + " restaurantes na categoria '" + categoria + "' encontrados",
                    restaurantes,
                    true
            );
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao buscar por categoria: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Buscar restaurante por nome (busca parcial)
     *
     * @param nome Nome ou parte do nome do restaurante
     * @return Lista de restaurantes encontrados com status 200 OK
     */
    @GetMapping("/buscar/nome")
    public ResponseEntity<?> buscarPorNome(@RequestParam String nome) {
        try {
            if (nome == null || nome.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Nome inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            List<Restaurante> restaurantes = restauranteService.buscarPorNomeContendo(nome);
            ApiResponse<List<Restaurante>> response = new ApiResponse<>(
                    "Total de " + restaurantes.size() + " restaurantes encontrados",
                    restaurantes,
                    true
            );
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao buscar restaurante: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT - Atualizar restaurante
     *
     * @param id ID do restaurante
     * @param restauranteAtualizado Dados atualizados
     * @return Restaurante atualizado com status 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Restaurante restauranteAtualizado) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            if (restauranteAtualizado == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Dados do restaurante não podem ser nulos", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Restaurante restaurante = restauranteService.atualizar(id, restauranteAtualizado);
            ApiResponse<Restaurante> response = new ApiResponse<>("Restaurante atualizado com sucesso", restaurante, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao atualizar restaurante: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT - Ativar restaurante
     *
     * @param id ID do restaurante
     * @return Restaurante ativado com status 200 OK
     */
    @PutMapping("/{id}/ativar")
    public ResponseEntity<?> ativar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Restaurante restaurante = restauranteService.ativar(id);
            ApiResponse<Restaurante> response = new ApiResponse<>("Restaurante ativado com sucesso", restaurante, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao ativar restaurante: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * PUT - Inativar restaurante
     *
     * @param id ID do restaurante
     * @return Restaurante inativado com status 200 OK
     */
    @PutMapping("/{id}/inativar")
    public ResponseEntity<?> inativar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            Restaurante restaurante = restauranteService.inativar(id);
            ApiResponse<Restaurante> response = new ApiResponse<>("Restaurante inativado com sucesso", restaurante, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao inativar restaurante: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * DELETE - Deletar restaurante
     *
     * @param id ID do restaurante
     * @return Status 204 NO CONTENT se sucesso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("ID inválido", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            restauranteService.deletar(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao deletar restaurante: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * GET - Contar restaurantes ativos em uma categoria
     *
     * @param categoria Categoria do restaurante
     * @return Total de restaurantes ativos
     */
    @GetMapping("/info/contar-categoria/{categoria}")
    public ResponseEntity<?> contarPorCategoria(@PathVariable String categoria) {
        try {
            if (categoria == null || categoria.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse("Categoria inválida", HttpStatus.BAD_REQUEST.value(), LocalDateTime.now()));
            }

            long total = restauranteService.contarPorCategoria(categoria);
            ApiResponse<Long> response = new ApiResponse<>("Total de restaurantes ativos na categoria '" + categoria + "'", total, true);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ErrorResponse error = new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("Erro ao contar: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}