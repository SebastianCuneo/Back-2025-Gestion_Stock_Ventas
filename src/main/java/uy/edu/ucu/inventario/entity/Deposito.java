package uy.edu.ucu.inventario.entity;

import jakarta.persistence.*;

/**
 * Entidad que representa un depósito.
 * Un depósito puede almacenar múltiples productos en el stock.
 */
@Entity
@Table(name = "depositos")
public class Deposito {

    // === Atributos ===

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String ubicacion;

    @Column(length = 255)
    private String descripcion;

    // === Constructores ===

    public Deposito() {
    }

    public Deposito(String nombre, String ubicacion, String descripcion) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
