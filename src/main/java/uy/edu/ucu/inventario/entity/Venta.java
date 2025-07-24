package uy.edu.ucu.inventario.entity;

import jakarta.persistence.*;
import uy.edu.ucu.inventario.enums.MetodoPago;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa una venta.
 */
@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    @Column(length = 100)
    private String revendedor; // Opcional

    // === Constructores ===

    public Venta() {
    }

    public Venta(LocalDateTime fecha, BigDecimal total, MetodoPago metodoPago, String revendedor) {
        this.fecha = fecha;
        this.total = total;
        this.metodoPago = metodoPago;
        this.revendedor = revendedor;
    }

    // === Getters y Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getRevendedor() {
        return revendedor;
    }

    public void setRevendedor(String revendedor) {
        this.revendedor = revendedor;
    }
}