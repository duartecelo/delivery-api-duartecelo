package com.deliverytech.delivery_api.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "restaurantes")
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private Double avaliacao;

    @Column(nullable = false)
    private Boolean ativo;

    // Construtores
    public Restaurante() {
    }

    public Restaurante(Long id, String nome, String categoria, Double avaliacao, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.avaliacao = avaliacao;
        this.ativo = ativo;
    }

    public Restaurante(String nome, String categoria, Double avaliacao, Boolean ativo) {
        this.nome = nome;
        this.categoria = categoria;
        this.avaliacao = avaliacao;
        this.ativo = ativo;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurante that = (Restaurante) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    // toString
    @Override
    public String toString() {
        return "Restaurante{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", categoria='" + categoria + '\'' +
                ", avaliacao=" + avaliacao +
                ", ativo=" + ativo +
                '}';
    }
}
