package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Product;
import uy.edu.ucu.inventario.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService svc;

    public ProductController(ProductService svc) {
        this.svc = svc;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list() {
        List<Product> products = svc.listAll();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", products);
        response.put("message", "Product list retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        return svc.getById(id)
                .map(product -> {
                    response.put("success", true);
                    response.put("data", product);
                    response.put("message", "Product found.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.put("success", false);
                    response.put("error", "Product not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Product p) {
        Product saved = svc.save(p);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true); 
        response.put("data", saved);
        response.put("message", "Product created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Product p) {
        Map<String, Object> response = new HashMap<>();

        return svc.getById(id)
                .map(existing -> {
                    p.setId(id);
                    Product updated = svc.save(p);
                    response.put("success", true);
                    response.put("data", updated);
                    response.put("message", "Product updated successfully.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.put("success", false);
                    response.put("error", "Product not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        if (!svc.getById(id).isPresent()) {
            response.put("success", false);
            response.put("error", "Product not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            svc.delete(id);
            response.put("success", true);
            response.put("message", "Product deleted successfully.");
            return ResponseEntity.ok(response);

        } catch (IllegalStateException ex) {
            response.put("success", false);
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception ex) {
            response.put("success", false);
            response.put("error", "Internal error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}