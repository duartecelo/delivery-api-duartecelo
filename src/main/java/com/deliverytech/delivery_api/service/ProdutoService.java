package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service para gerenciar operações de Produto.
 * Implementa cadastro por restaurante, disponibilidade e validação de preço.
 */
@Service
@Transactional
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    private static final Double PRECO_MINIMO = 0.01;
    private static final Double PRECO_MAXIMO = 999999.99;

    /**
     * Cadastra um novo produto para um restaurante com validações.
     *
     * @param produto Produto a ser cadastrado
     * @return Produto cadastrado
     * @throws IllegalArgumentException Se validações falharem
     */
    public Produto cadastrar(Produto produto) {
        validarCadastro(produto);
        produto.setDisponivel(true);
        return produtoRepository.save(produto);
    }

    /**
     * Valida os dados para cadastro de produto.
     *
     * @param produto Produto a validar
     * @throws IllegalArgumentException Se dados inválidos
     */
    private void validarCadastro(Produto produto) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }

        if (produto.getNome().length() < 3) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres");
        }

        if (produto.getNome().length() > 100) {
            throw new IllegalArgumentException("Nome não pode exceder 100 caracteres");
        }

        validarCategoria(produto.getCategoria());

        if (produto.getRestaurante() == null || produto.getRestaurante().getId() == null) {
            throw new IllegalArgumentException("Restaurante é obrigatório");
        }

        // Verifica se restaurante existe e está ativo
        Restaurante restaurante = restauranteRepository.findById(produto.getRestaurante().getId())
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        if (!restaurante.getAtivo()) {
            throw new IllegalArgumentException("Não é possível cadastrar produtos em restaurante inativo");
        }

        produto.setRestaurante(restaurante);
    }

    /**
     * Valida a categoria do produto.
     *
     * @param categoria Categoria a validar
     * @throws IllegalArgumentException Se categoria inválida
     */
    private void validarCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria do produto é obrigatória");
        }

        if (categoria.length() < 3) {
            throw new IllegalArgumentException("Categoria deve ter pelo menos 3 caracteres");
        }

        if (categoria.length() > 50) {
            throw new IllegalArgumentException("Categoria não pode exceder 50 caracteres");
        }
    }

    /**
     * Valida o preço do produto.
     *
     * @param preco Preço a validar
     * @throws IllegalArgumentException Se preço inválido
     */
    public void validarPreco(Double preco) {
        if (preco == null) {
            throw new IllegalArgumentException("Preço é obrigatório");
        }

        if (preco < PRECO_MINIMO) {
            throw new IllegalArgumentException("Preço deve ser maior ou igual a R$ 0,01");
        }

        if (preco > PRECO_MAXIMO) {
            throw new IllegalArgumentException("Preço não pode exceder R$ 999.999,99");
        }
    }

    /**
     * Busca um produto por ID.
     *
     * @param id ID do produto
     * @return Produto encontrado
     * @throws IllegalArgumentException Se produto não existe
     */
    public Produto buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID do produto inválido");
        }

        return produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
    }

    /**
     * Lista todos os produtos de um restaurante.
     *
     * @param restauranteId ID do restaurante
     * @return Lista de produtos
     */
    public List<Produto> listarPorRestaurante(Long restauranteId) {
        if (restauranteId == null || restauranteId <= 0) {
            throw new IllegalArgumentException("ID do restaurante inválido");
        }

        restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        return produtoRepository.findByRestauranteId(restauranteId);
    }

    /**
     * Lista todos os produtos disponíveis de um restaurante.
     *
     * @param restauranteId ID do restaurante
     * @return Lista de produtos disponíveis
     */
    public List<Produto> listarDisponiveisPorRestaurante(Long restauranteId) {
        if (restauranteId == null || restauranteId <= 0) {
            throw new IllegalArgumentException("ID do restaurante inválido");
        }

        restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        return produtoRepository.findByRestauranteIdAndDisponivel(restauranteId);
    }

    /**
     * Lista produtos de um restaurante por categoria.
     *
     * @param restauranteId ID do restaurante
     * @param categoria Categoria do produto
     * @return Lista de produtos disponíveis na categoria
     */
    public List<Produto> listarPorCategoriaERestaurante(Long restauranteId, String categoria) {
        if (restauranteId == null || restauranteId <= 0) {
            throw new IllegalArgumentException("ID do restaurante inválido");
        }

        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria inválida");
        }

        restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        return produtoRepository.findByRestauranteIdAndCategoria(restauranteId, categoria);
    }

    /**
     * Lista produtos indisponíveis de um restaurante.
     *
     * @param restauranteId ID do restaurante
     * @return Lista de produtos indisponíveis
     */
    public List<Produto> listarIndisponiveisPorRestaurante(Long restauranteId) {
        if (restauranteId == null || restauranteId <= 0) {
            throw new IllegalArgumentException("ID do restaurante inválido");
        }

        restauranteRepository.findById(restauranteId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));

        return produtoRepository.findByRestauranteIdAndIndisponivel(restauranteId);
    }

    /**
     * Ativa a disponibilidade de um produto.
     *
     * @param id ID do produto
     * @return Produto com disponibilidade ativada
     * @throws IllegalArgumentException Se produto não existe
     */
    public Produto ativarDisponibilidade(Long id) {
        Produto produto = buscarPorId(id);

        if (produto.isDisponivel()) {
            throw new IllegalArgumentException("Produto já está disponível");
        }

        produto.setDisponivel(true);
        return produtoRepository.save(produto);
    }

    /**
     * Desativa a disponibilidade de um produto.
     *
     * @param id ID do produto
     * @return Produto com disponibilidade desativada
     * @throws IllegalArgumentException Se produto não existe
     */
    public Produto desativarDisponibilidade(Long id) {
        Produto produto = buscarPorId(id);

        if (!produto.isDisponivel()) {
            throw new IllegalArgumentException("Produto já está indisponível");
        }

        produto.setDisponivel(false);
        return produtoRepository.save(produto);
    }

    /**
     * Obtém o status de disponibilidade de um produto.
     *
     * @param id ID do produto
     * @return true se disponível, false se indisponível
     */
    public boolean obterStatusDisponibilidade(Long id) {
        Produto produto = buscarPorId(id);
        return produto.isDisponivel();
    }

    /**
     * Atualiza dados de um produto.
     *
     * @param id ID do produto
     * @param produtoAtualizado Dados atualizados
     * @return Produto atualizado
     * @throws IllegalArgumentException Se produto não existe
     */
    public Produto atualizar(Long id, Produto produtoAtualizado) {
        Produto produto = buscarPorId(id);

        if (produtoAtualizado.getNome() != null && !produtoAtualizado.getNome().trim().isEmpty()) {
            validarNome(produtoAtualizado.getNome());
            produto.setNome(produtoAtualizado.getNome());
        }

        if (produtoAtualizado.getCategoria() != null && !produtoAtualizado.getCategoria().trim().isEmpty()) {
            validarCategoria(produtoAtualizado.getCategoria());
            produto.setCategoria(produtoAtualizado.getCategoria());
        }

        return produtoRepository.save(produto);
    }

    /**
     * Valida apenas o nome do produto.
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
     * Retorna a quantidade de produtos disponíveis de um restaurante.
     *
     * @param restauranteId ID do restaurante
     * @return Quantidade de produtos disponíveis
     */
    public long contarDisponiveisPorRestaurante(Long restauranteId) {
        if (restauranteId == null || restauranteId <= 0) {
            throw new IllegalArgumentException("ID do restaurante inválido");
        }

        return produtoRepository.countByRestauranteIdAndDisponivel(restauranteId, true);
    }

    /**
     * Retorna a quantidade de produtos indisponíveis de um restaurante.
     *
     * @param restauranteId ID do restaurante
     * @return Quantidade de produtos indisponíveis
     */
    public long contarIndisponiveisPorRestaurante(Long restauranteId) {
        if (restauranteId == null || restauranteId <= 0) {
            throw new IllegalArgumentException("ID do restaurante inválido");
        }

        return produtoRepository.countByRestauranteIdAndDisponivel(restauranteId, false);
    }

    /**
     * Deleta permanentemente um produto do sistema.
     *
     * @param id ID do produto
     * @throws IllegalArgumentException Se produto não existe
     */
    public void deletar(Long id) {
        Produto produto = buscarPorId(id);
        produtoRepository.delete(produto);
    }
}