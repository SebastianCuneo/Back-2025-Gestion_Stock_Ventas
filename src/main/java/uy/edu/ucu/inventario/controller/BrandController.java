package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Brand;
import uy.edu.ucu.inventario.service.BrandService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.*;

/**
 * REST Controller for the Brand entity.
 * Provides CRUD operations for system brands.
 */
@RestController
@RequestMapping("/api/brands")
public class BrandController {

    private final BrandService svc;

    public BrandController(BrandService svc) {
        this.svc = svc;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list() {
        List<Brand> brands = svc.listAll();
        List<Map<String, Object>> transformed = new ArrayList<>();

        for (Brand b : brands) {
            transformed.add(transformBrand(b));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformed);
        response.put("message", "Brands list retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
        return svc.getById(id)
                .map(brand -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformBrand(brand));
                    response.put("message", "Brand found.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Brand not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Brand b) {
        Brand saved = svc.save(b);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformBrand(saved));
        response.put("message", "Brand created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Brand b) {
        return svc.getById(id)
                .map(existing -> {
                    b.setId(id);
                    Brand updated = svc.save(b);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformBrand(updated));
                    response.put("message", "Brand updated successfully.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Brand not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        if (!svc.getById(id).isPresent()) {
            response.put("success", false);
            response.put("error", "Brand not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            svc.delete(id);
            response.put("success", true);
            response.put("message", "Brand deleted successfully.");
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException ex) {
            response.put("success", false);
            response.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (DataIntegrityViolationException ex) {
            response.put("success", false);
            response.put("error", "Cannot delete brand due to data integrity constraints.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } catch (Exception ex) {
            response.put("success", false);
            response.put("error", "Internal error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private Map<String, Object> transformBrand(Brand b) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", b.getId());
        map.put("name", b.getName());
        map.put("description", b.getDescription());
        map.put("country", b.getCountryOfOrigin());
        map.put("createdAt", b.getCreatedAt());
        map.put("associatedProductCount", b.getAssociatedProductCount());
        return map;
    }
}