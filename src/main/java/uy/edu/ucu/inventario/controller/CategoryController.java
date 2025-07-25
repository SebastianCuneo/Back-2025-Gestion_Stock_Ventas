package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Category;
import uy.edu.ucu.inventario.service.CategoryService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * REST Controller for the Category entity.
 * Allows CRUD operations on product categories.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService svc;

    public CategoryController(CategoryService svc) {
        this.svc = svc;
    }

    /**
     * Get all categories.
     */
    @GetMapping
    public List<Category> list() {
        return svc.listAll();
    }

    /**
     * Get a category by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> get(@PathVariable Long id) {
        return svc.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new category.
     */
    @PostMapping
    public Category create(@RequestBody Category c) {
        return svc.save(c);
    }

    /**
     * Update an existing category.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id, @RequestBody Category c) {
        return svc.getById(id)
                .map(existing -> {
                    c.setId(id);
                    return ResponseEntity.ok(svc.save(c));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a category by ID, only if it's not used by products.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        // Pre-validation: does the category exist?
        if (!svc.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        try {
            svc.delete(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalStateException ex) {
            // The category is in use by products
            return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());

        } catch (EntityNotFoundException ex) {
            // Triggered after delete if not found (double check)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Category not found: " + ex.getMessage());

        } catch (DataIntegrityViolationException ex) {
            // Referential integrity violation
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Cannot delete due to data integrity.");

        } catch (Exception ex) {
            // Any other error
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal error: " + ex.getMessage());
        }
    }
}