package uy.edu.ucu.inventario.entity;

import jakarta.persistence.*;

/**
 * Entidad que representa el stock de un producto en un depósito específico.
 */
@Entity
@Table(name = "stock")
public class Stock {

    // === Atributos ===

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "deposito_id", nullable = false)
    private Deposito deposito;

    @Column(nullable = false)
    private int cantidad;

    // === Constructores ===

    public Stock() {
    }

    public Stock(Producto producto, Deposito deposito, int cantidad) {
        this.producto = producto;
        this.deposito = deposito;
        this.cantidad = cantidad;
    }

    // === Getters & Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Deposito getDeposito() {
        return deposito;
    }

    public void setDeposito(Deposito deposito) {
        this.deposito = deposito;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}