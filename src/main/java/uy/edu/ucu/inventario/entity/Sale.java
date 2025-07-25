package uy.edu.ucu.inventario.entity;

import jakarta.persistence.*;
import uy.edu.ucu.inventario.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a sale.
 */
@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(length = 100)
    private String reseller; // Optional

    // === Constructors ===

    public Sale() {
    }

    public Sale(LocalDateTime date, BigDecimal total, PaymentMethod paymentMethod, String reseller) {
        this.date = date;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.reseller = reseller;
    }

    // === Getters and Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReseller() {
        return reseller;
    }

    public void setReseller(String reseller) {
        this.reseller = reseller;
    }
}