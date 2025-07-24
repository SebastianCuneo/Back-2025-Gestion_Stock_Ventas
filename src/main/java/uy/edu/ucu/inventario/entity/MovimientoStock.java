package uy.edu.ucu.inventario.entity;

import jakarta.persistence.*;
import uy.edu.ucu.inventario.enums.TipoMovimiento;

import java.time.LocalDateTime;

/**
 * Entidad que representa un movimiento de stock (entrada o salida).
 */
@Entity
@Table(name = "movimientos_stock")
public class MovimientoStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipo;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "deposito_id", nullable = false)
    private Deposito deposito;

    @Column(nullable = false)
    private int cantidad;

    @Column(nullable = false)
    private LocalDateTime fecha;

    // === Usuario ===
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public MovimientoStock() {
    }

    public MovimientoStock(TipoMovimiento tipo, Producto producto, Deposito deposito, int cantidad, LocalDateTime fecha) {
        this.tipo = tipo;
        this.producto = producto;
        this.deposito = deposito;
        this.cantidad = cantidad;
        this.fecha = fecha;
    }

    // === Getters y Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
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

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
    	return usuario; 
    }
    
    public void setUsuario(Usuario usuario) {
    	this.usuario = usuario; 
    }
}