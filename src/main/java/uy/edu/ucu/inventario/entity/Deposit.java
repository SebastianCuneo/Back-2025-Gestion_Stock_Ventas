package uy.edu.ucu.inventario.entity;

import jakarta.persistence.*;

/**
 * Entity representing a deposit (warehouse).
 * A deposit can store multiple products in stock.
 */
@Entity
@Table(name = "deposits")
public class Deposit {

    // === Attributes ===

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String location;

    @Column(length = 255)
    private String description;

    // === Constructors ===

    public Deposit() {
    }

    public Deposit(String name, String location, String description) {
        this.name = name;
        this.location = location;
        this.description = description;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
