package com.deliverytech.delivery_api.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private Boolean disponivel;

    @ManyToOne
    @JoinColumn(name = "restaurante_id", nullable = false)
    private Restaurante restaurante;

    // Construtores
    public Produto() {
    }

    public Produto(Long id, String nome, String categoria, Boolean disponivel, Restaurante restaurante) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.disponivel = disponivel;
        this.restaurante = restaurante;
    }

    public Produto(String nome, String categoria, Boolean disponivel, Restaurante restaurante) {
        this.nome = nome;
        this.categoria = categoria;
        this.disponivel = disponivel;
        this.restaurante = restaurante;
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

    public Boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Restaurante getRestaurante() {
        return restaurante;
    }

    public void setRestaurante(Restaurante restaurante) {
        this.restaurante = restaurante;
    }

    // equals e hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id) &&
                Objects.equals(nome, produto.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome);
    }

    // toString
    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", categoria='" + categoria + '\'' +
                ", disponivel=" + disponivel +
                ", restaurante=" + restaurante.getNome() +
                '}';
    }
}
