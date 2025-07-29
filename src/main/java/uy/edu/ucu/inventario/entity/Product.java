package uy.edu.ucu.inventario.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;

/**
 * Entity representing a Product.
 * Each product has an associated brand and category.
 */
@Entity
@Table(name = "products")
public class Product {

    // === Attributes ===

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private int depositsCount = 0;

    // === Constructors ===

    public Product() {
    }

    public Product(String name, String description, BigDecimal price, Brand brand, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.brand = brand;
        this.category = category;
        this.depositsCount = 0;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getDepositsCount() {
        return depositsCount;
    }

    public void setDepositsCount(int depositsCount) {
        this.depositsCount = depositsCount;
    }

    public void incrementDepositsCount() {
        this.depositsCount++;
    }

    public void decrementDepositsCount() {
        if (this.depositsCount > 0) {
            this.depositsCount--;
        }
    }
}