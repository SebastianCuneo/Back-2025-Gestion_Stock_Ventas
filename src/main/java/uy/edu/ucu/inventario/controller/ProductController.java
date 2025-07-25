package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Product;
import uy.edu.ucu.inventario.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for the Product entity.
 * Provides CRUD operations for products in the system.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService svc;

    public ProductController(ProductService svc) {
        this.svc = svc;
    }

    /**
     * Get the list of all products.
     */
    @GetMapping
    public List<Product> list() {
        return svc.listAll();
    }

    /**
     * Get a product by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> get(@PathVariable Long id) {
        return svc.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new product.
     */
    @PostMapping
    public Product create(@RequestBody Product p) {
        return svc.save(p);
    }

    /**
     * Update an existing product.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product p) {
        return svc.getById(id)
                .map(existingProduct -> {
                    p.setId(id);
                    return ResponseEntity.ok(svc.save(p));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a product by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!svc.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            svc.delete(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalStateException ex) {
            return ResponseEntity
                   .badRequest()
                   .body(ex.getMessage());

        } catch (Exception ex) {
            return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Internal error: " + ex.getMessage());
        }
    }
}