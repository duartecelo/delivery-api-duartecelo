package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service para gerenciar operações de Restaurante.
 * Implementa validações, gestão de status e regras de negócio.
 */
@Service
@Transactional
public class RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    /**
     * Cadastra um novo restaurante com validações.
     *
     * @param restaurante Restaurante a ser cadastrado
     * @return Restaurante cadastrado
     * @throws IllegalArgumentException Se validações falharem
     */
    public Restaurante cadastrar(Restaurante restaurante) {
        validarCadastro(restaurante);
        restaurante.setAtivo(true);
        return restauranteRepository.save(restaurante);
    }

    /**
     * Valida os dados para cadastro de restaurante.
     *
     * @param restaurante Restaurante a validar
     * @throws IllegalArgumentException Se dados inválidos
     */
    private void validarCadastro(Restaurante restaurante) {
        if (restaurante.getNome() == null || restaurante.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do restaurante é obrigatório");
        }

        if (restaurante.getNome().length() < 3) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres");
        }

        if (restaurante.getNome().length() > 100) {
            throw new IllegalArgumentException("Nome não pode exceder 100 caracteres");
        }

        // Verifica se nome já existe
        Optional<Restaurante> restauranteExistente = restauranteRepository.findByNome(restaurante.getNome());
        if (restauranteExistente.isPresent()) {
            throw new IllegalArgumentException("Restaurante com esse nome já existe");
        }

        validarCategoria(restaurante.getCategoria());
        validarAvaliacao(restaurante.getAvaliacao());
    }

    /**
     * Valida a categoria do restaurante.
     *
     * @param categoria Categoria a validar
     * @throws IllegalArgumentException Se categoria inválida
     */
    private void validarCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria do restaurante é obrigatória");
        }

        if (categoria.length() < 3) {
            throw new IllegalArgumentException("Categoria deve ter pelo menos 3 caracteres");
        }

        if (categoria.length() > 50) {
            throw new IllegalArgumentException("Categoria não pode exceder 50 caracteres");
        }
    }

    /**
     * Valida a avaliação do restaurante.
     *
     * @param avaliacao Avaliação a validar
     * @throws IllegalArgumentException Se avaliação inválida
     */
    private void validarAvaliacao(Double avaliacao) {
        if (avaliacao == null) {
            throw new IllegalArgumentException("Avaliação é obrigatória");
        }

        if (avaliacao < 0.0 || avaliacao > 5.0) {
            throw new IllegalArgumentException("Avaliação deve estar entre 0.0 e 5.0");
        }
    }

    /**
     * Busca um restaurante por ID.
     *
     * @param id ID do restaurante
     * @return Restaurante encontrado
     * @throws IllegalArgumentException Se restaurante não existe
     */
    public Restaurante buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID do restaurante inválido");
        }

        return restauranteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));
    }

    /**
     * Busca um restaurante por nome.
     *
     * @param nome Nome do restaurante
     * @return Restaurante encontrado
     * @throws IllegalArgumentException Se restaurante não existe
     */
    public Restaurante buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do restaurante inválido");
        }

        return restauranteRepository.findByNome(nome)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));
    }

    /**
     * Busca restaurantes por nome (busca parcial).
     *
     * @param nome Parte do nome do restaurante
     * @return Lista de restaurantes encontrados
     */
    public List<Restaurante> buscarPorNomeContendo(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do restaurante inválido");
        }

        return restauranteRepository.findByNomeContaining(nome);
    }

    /**
     * Busca restaurantes por categoria.
     *
     * @param categoria Categoria do restaurante
     * @return Lista de restaurantes da categoria (apenas ativos)
     */
    public List<Restaurante> buscarPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria inválida");
        }

        return restauranteRepository.findByCategoria(categoria);
    }

    /**
     * Lista todos os restaurantes ativos ordenados por avaliação.
     *
     * @return Lista de restaurantes ativos
     */
    public List<Restaurante> listarAtivos() {
        return restauranteRepository.findAllAtivosOrderByAvaliacao();
    }

    /**
     * Lista todos os restaurantes (ativos e inativos).
     *
     * @return Lista de todos os restaurantes
     */
    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll();
    }

    /**
     * Atualiza dados de um restaurante.
     *
     * @param id ID do restaurante
     * @param restauranteAtualizado Dados atualizados
     * @return Restaurante atualizado
     * @throws IllegalArgumentException Se restaurante não existe
     */
    public Restaurante atualizar(Long id, Restaurante restauranteAtualizado) {
        Restaurante restaurante = buscarPorId(id);

        if (restauranteAtualizado.getNome() != null && !restauranteAtualizado.getNome().equals(restaurante.getNome())) {
            validarNome(restauranteAtualizado.getNome());

            Optional<Restaurante> nomeExistente = restauranteRepository.findByNome(restauranteAtualizado.getNome());
            if (nomeExistente.isPresent() && !nomeExistente.get().getId().equals(id)) {
                throw new IllegalArgumentException("Restaurante com esse nome já existe");
            }

            restaurante.setNome(restauranteAtualizado.getNome());
        }

        if (restauranteAtualizado.getCategoria() != null) {
            validarCategoria(restauranteAtualizado.getCategoria());
            restaurante.setCategoria(restauranteAtualizado.getCategoria());
        }

        if (restauranteAtualizado.getAvaliacao() != null) {
            validarAvaliacao(restauranteAtualizado.getAvaliacao());
            restaurante.setAvaliacao(restauranteAtualizado.getAvaliacao());
        }

        return restauranteRepository.save(restaurante);
    }

    /**
     * Valida apenas o nome do restaurante.
     *
     * @param nome Nome a validar
     * @throws IllegalArgumentException Se nome inválido
     */
    private void validarNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome não pode ser vazio");
        }

        if (nome.length() < 3) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres");
        }

        if (nome.length() > 100) {
            throw new IllegalArgumentException("Nome não pode exceder 100 caracteres");
        }
    }

    /**
     * Ativa um restaurante.
     *
     * @param id ID do restaurante
     * @return Restaurante ativado
     * @throws IllegalArgumentException Se restaurante não existe
     */
    public Restaurante ativar(Long id) {
        Restaurante restaurante = buscarPorId(id);

        if (restaurante.getAtivo()) {
            throw new IllegalArgumentException("Restaurante já está ativo");
        }

        restaurante.setAtivo(true);
        return restauranteRepository.save(restaurante);
    }

    /**
     * Inativa um restaurante.
     *
     * @param id ID do restaurante
     * @return Restaurante inativado
     * @throws IllegalArgumentException Se restaurante não existe
     */
    public Restaurante inativar(Long id) {
        Restaurante restaurante = buscarPorId(id);

        if (!restaurante.getAtivo()) {
            throw new IllegalArgumentException("Restaurante já está inativo");
        }

        restaurante.setAtivo(false);
        return restauranteRepository.save(restaurante);
    }

    /**
     * Obtém o status de um restaurante.
     *
     * @param id ID do restaurante
     * @return true se ativo, false se inativo
     */
    public boolean obterStatus(Long id) {
        Restaurante restaurante = buscarPorId(id);
        return restaurante.getAtivo();
    }

    /**
     * Retorna a quantidade total de restaurantes ativos em uma categoria.
     *
     * @param categoria Categoria do restaurante
     * @return Quantidade de restaurantes ativos
     */
    public long contarPorCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria inválida");
        }

        return restauranteRepository.countByCategoriaAndAtivoTrue(categoria);
    }

    /**
     * Deleta permanentemente um restaurante do sistema.
     *
     * @param id ID do restaurante
     * @throws IllegalArgumentException Se restaurante não existe
     */
    public void deletar(Long id) {
        Restaurante restaurante = buscarPorId(id);
        restauranteRepository.delete(restaurante);
    }
}