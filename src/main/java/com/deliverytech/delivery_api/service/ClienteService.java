package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Service para gerenciar operações de Cliente.
 * Implementa validações, CRUD e regras de negócio.
 */
@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    /**
     * Cadastra um novo cliente com validações.
     *
     * @param cliente Cliente a ser cadastrado
     * @return Cliente cadastrado
     * @throws IllegalArgumentException Se validações falharem
     */
    public Cliente cadastrar(Cliente cliente) {
        validarCadastro(cliente);
        cliente.setAtivo(true);
        return clienteRepository.save(cliente);
    }

    /**
     * Valida os dados para cadastro de cliente.
     *
     * @param cliente Cliente a validar
     * @throws IllegalArgumentException Se dados inválidos
     */
    private void validarCadastro(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do cliente é obrigatório");
        }

        if (cliente.getNome().length() < 3) {
            throw new IllegalArgumentException("Nome deve ter pelo menos 3 caracteres");
        }

        if (cliente.getNome().length() > 100) {
            throw new IllegalArgumentException("Nome não pode exceder 100 caracteres");
        }

        validarEmail(cliente.getEmail());

        // Verifica se email já existe
        Optional<Cliente> clienteExistente = clienteRepository.findByEmail(cliente.getEmail());
        if (clienteExistente.isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado no sistema");
        }
    }

    /**
     * Valida o formato do email.
     *
     * @param email Email a validar
     * @throws IllegalArgumentException Se email inválido
     */
    public void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Email em formato inválido");
        }

        if (email.length() > 100) {
            throw new IllegalArgumentException("Email não pode exceder 100 caracteres");
        }
    }

    /**
     * Busca um cliente por ID.
     *
     * @param id ID do cliente
     * @return Cliente encontrado
     * @throws IllegalArgumentException Se cliente não existe
     */
    public Cliente buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        return clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
    }

    /**
     * Busca um cliente ativo por email.
     *
     * @param email Email do cliente
     * @return Cliente encontrado
     * @throws IllegalArgumentException Se cliente não existe ou está inativo
     */
    public Cliente buscarPorEmail(String email) {
        validarEmail(email);

        return clienteRepository.findByEmailAndAtivoTrue(email)
                .orElseThrow(() -> new IllegalArgumentException("Cliente ativo não encontrado com esse email"));
    }

    /**
     * Lista todos os clientes ativos.
     *
     * @return Lista de clientes ativos
     */
    public List<Cliente> listarAtivos() {
        return clienteRepository.findAllAtivos();
    }

    /**
     * Retorna a quantidade total de clientes ativos.
     *
     * @return Quantidade de clientes ativos
     */
    public long contarAtivos() {
        return clienteRepository.countByAtivoTrue();
    }

    /**
     * Atualiza dados de um cliente.
     *
     * @param id ID do cliente
     * @param clienteAtualizado Dados atualizados
     * @return Cliente atualizado
     * @throws IllegalArgumentException Se cliente não existe
     */
    public Cliente atualizar(Long id, Cliente clienteAtualizado) {
        Cliente cliente = buscarPorId(id);

        if (clienteAtualizado.getNome() != null && !clienteAtualizado.getNome().trim().isEmpty()) {
            validarNome(clienteAtualizado.getNome());
            cliente.setNome(clienteAtualizado.getNome());
        }

        if (clienteAtualizado.getEmail() != null && !clienteAtualizado.getEmail().equals(cliente.getEmail())) {
            validarEmail(clienteAtualizado.getEmail());

            Optional<Cliente> emailExistente = clienteRepository.findByEmail(clienteAtualizado.getEmail());
            if (emailExistente.isPresent() && !emailExistente.get().getId().equals(id)) {
                throw new IllegalArgumentException("Email já cadastrado para outro cliente");
            }

            cliente.setEmail(clienteAtualizado.getEmail());
        }

        return clienteRepository.save(cliente);
    }

    /**
     * Valida apenas o nome do cliente.
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
     * Inativa um cliente.
     *
     * @param id ID do cliente
     * @return Cliente inativado
     * @throws IllegalArgumentException Se cliente não existe
     */
    public Cliente inativar(Long id) {
        Cliente cliente = buscarPorId(id);

        if (!cliente.getAtivo()) {
            throw new IllegalArgumentException("Cliente já está inativo");
        }

        cliente.setAtivo(false);
        return clienteRepository.save(cliente);
    }

    /**
     * Ativa um cliente inativo.
     *
     * @param id ID do cliente
     * @return Cliente ativado
     * @throws IllegalArgumentException Se cliente não existe
     */
    public Cliente ativar(Long id) {
        Cliente cliente = buscarPorId(id);

        if (cliente.getAtivo()) {
            throw new IllegalArgumentException("Cliente já está ativo");
        }

        cliente.setAtivo(true);
        return clienteRepository.save(cliente);
    }

    /**
     * Deleta permanentemente um cliente do sistema.
     *
     * @param id ID do cliente
     * @throws IllegalArgumentException Se cliente não existe
     */
    public void deletar(Long id) {
        Cliente cliente = buscarPorId(id);
        clienteRepository.delete(cliente);
    }
}