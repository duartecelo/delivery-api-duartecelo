package com.deliverytech.delivery_api.dto;

import java.io.Serializable;

/**
 * DTO para resposta padronizada de sucesso.
 * Utilizado em todas as respostas bem-sucedidas da API.
 *
 * @param <T> Tipo do dado contido na resposta
 */
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mensagem;
    private T dados;
    private Boolean sucesso;

    /**
     * Construtor padrão
     */
    public ApiResponse() {
    }

    /**
     * Construtor com parâmetros
     *
     * @param mensagem Mensagem descritiva da resposta
     * @param dados Dados da resposta
     * @param sucesso Indicador de sucesso (true/false)
     */
    public ApiResponse(String mensagem, T dados, Boolean sucesso) {
        this.mensagem = mensagem;
        this.dados = dados;
        this.sucesso = sucesso;
    }

    // Getters e Setters

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public T getDados() {
        return dados;
    }

    public void setDados(T dados) {
        this.dados = dados;
    }

    public Boolean getSucesso() {
        return sucesso;
    }

    public void setSucesso(Boolean sucesso) {
        this.sucesso = sucesso;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "mensagem='" + mensagem + '\'' +
                ", dados=" + dados +
                ", sucesso=" + sucesso +
                '}';
    }
}