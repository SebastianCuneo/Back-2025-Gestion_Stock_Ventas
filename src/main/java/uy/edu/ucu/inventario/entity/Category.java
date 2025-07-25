package uy.edu.ucu.inventario.entity;

import jakarta.persistence.*;

/**
 * Entity representing a product category.
 * Each category can be associated with multiple products.
 */
@Entity
@Table(name = "categories")
public class Category {

    // === Attributes ===

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    // === Constructors ===

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    // === Getters & Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}