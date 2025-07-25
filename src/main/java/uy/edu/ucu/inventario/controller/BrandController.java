package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Brand;
import uy.edu.ucu.inventario.service.BrandService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

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

    /**
     * Retrieve all brands.
     */
    @GetMapping
    public List<Brand> list() {
        return svc.listAll();
    }

    /**
     * Retrieve a brand by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Brand> get(@PathVariable Long id) {
        return svc.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new brand.
     */
    @PostMapping
    public Brand create(@RequestBody Brand b) {
        return svc.save(b);
    }

    /**
     * Update an existing brand.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Brand> update(@PathVariable Long id, @RequestBody Brand b) {
        return svc.getById(id)
                .map(existing -> {
                    b.setId(id);
                    return ResponseEntity.ok(svc.save(b));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a brand by ID if not associated with products.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!svc.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            svc.delete(id);
            return ResponseEntity.noContent().build();

        } catch (EntityNotFoundException ex) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());

        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Cannot delete brand due to data integrity constraints.");

        } catch (Exception ex) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal error: " + ex.getMessage());
        }
    }
}
