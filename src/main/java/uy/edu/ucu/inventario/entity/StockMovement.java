package uy.edu.ucu.inventario.entity;

import jakarta.persistence.*;
import uy.edu.ucu.inventario.enums.MovementType;

import java.time.LocalDateTime;

/**
 * Entity representing a stock movement (entry or exit).
 */
@Entity
@Table(name = "stock_movements")
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MovementType type;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "deposit_id", nullable = false)
    private Deposit deposit;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private LocalDateTime date;

    // === User ===
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public StockMovement() {
    }

    public StockMovement(MovementType type, Product product, Deposit deposit, int quantity, LocalDateTime date) {
        this.type = type;
        this.product = product;
        this.deposit = deposit;
        this.quantity = quantity;
        this.date = date;
    }

    // === Getters & Setters ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Deposit getDeposit() {
        return deposit;
    }

    public void setDeposit(Deposit deposit) {
        this.deposit = deposit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}