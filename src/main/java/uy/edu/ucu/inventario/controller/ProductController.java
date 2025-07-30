package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Product;
import uy.edu.ucu.inventario.entity.Product.MonetaryValue;
import uy.edu.ucu.inventario.service.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list() {
        List<Product> products = productService.listAll();
        List<Map<String, Object>> transformed = new ArrayList<>();

        for (Product product : products) {
            transformed.add(transformProduct(product));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformed);
        response.put("message", "Product list retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
        return productService.getById(id)
                .map(product -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformProduct(product));
                    response.put("message", "Product found.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Product not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Product product) {
        Product saved = productService.save(product);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformProduct(saved));
        response.put("message", "Product created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Product product) {
        return productService.getById(id)
                .map(existing -> {
                    product.setId(id);
                    Product updated = productService.save(product);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformProduct(updated));
                    response.put("message", "Product updated successfully.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Product not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        if (!productService.getById(id).isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Product not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            productService.delete(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Product deleted successfully.");
            return ResponseEntity.ok(response);

        } catch (IllegalStateException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (Exception ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Internal error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private Map<String, Object> transformProduct(Product product) {
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("id", product.getId());
        productMap.put("name", product.getName());
        productMap.put("description", product.getDescription());
        productMap.put("brand", product.getBrand());
        productMap.put("category", product.getCategory());
        productMap.put("depositsCount", product.getDepositsCount());

        MonetaryValue purchase = product.getPurchasePrice();
        MonetaryValue sale = product.getSalePrice();

        productMap.put("purchasePrices", List.of(Map.of(
            "currency", purchase != null ? purchase.getCurrency() : "USD",
            "value", purchase != null ? purchase.getValue() : null
        )));

        productMap.put("salePrices", List.of(Map.of(
            "currency", sale != null ? sale.getCurrency() : "USD",
            "value", sale != null ? sale.getValue() : null
        )));

        return productMap;
    }
}