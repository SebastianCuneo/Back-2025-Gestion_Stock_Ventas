package uy.edu.ucu.inventario.entity;

import jakarta.persistence.*;

/**
 * Entidad que representa una categoría de productos.
 * Cada categoría puede estar asociada a múltiples productos.
 */
@Entity
@Table(name = "categorias")
public class Categoria {

    // === Atributos ===

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    // === Constructores ===

    public Categoria() {
    }

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    // === Getters & Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
