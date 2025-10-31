package com.deliverytech.delivery_api.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO para resposta padronizada de erro.
 * Utilizado em todas as respostas de erro da API.
 */
public class ErrorResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mensagem;
    private Integer codigo;
    private LocalDateTime timestamp;

    /**
     * Construtor padrão
     */
    public ErrorResponse() {
    }

    /**
     * Construtor com parâmetros
     *
     * @param mensagem Mensagem de erro descritiva
     * @param codigo Código HTTP da resposta
     * @param timestamp Data/hora do erro
     */
    public ErrorResponse(String mensagem, Integer codigo, LocalDateTime timestamp) {
        this.mensagem = mensagem;
        this.codigo = codigo;
        this.timestamp = timestamp;
    }

    // Getters e Setters

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "mensagem='" + mensagem + '\'' +
                ", codigo=" + codigo +
                ", timestamp=" + timestamp +
                '}';
    }
}