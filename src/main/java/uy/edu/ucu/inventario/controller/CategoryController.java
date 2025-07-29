package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Category;
import uy.edu.ucu.inventario.service.CategoryService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping
    public ResponseEntity<Map<String, Object>> list() {
        List<Category> categories = svc.listAll();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", categories);
        response.put("message", "Category list retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        return svc.getById(id)
                .map(category -> {
                    response.put("success", true);
                    response.put("data", category);
                    response.put("message", "Category found.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.put("success", false);
                    response.put("error", "Category not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Category c) {
        Category saved = svc.save(c);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", saved);
        response.put("message", "Category created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Category c) {
        Map<String, Object> response = new HashMap<>();

        return svc.getById(id)
                .map(existing -> {
                    c.setId(id);
                    Category updated = svc.save(c);
                    response.put("success", true);
                    response.put("data", updated);
                    response.put("message", "Category updated successfully.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.put("success", false);
                    response.put("error", "Category not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        if (!svc.getById(id).isPresent()) {
            response.put("success", false);
            response.put("error", "Category not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            svc.delete(id);
            response.put("success", true);
            response.put("message", "Category deleted successfully.");
            return ResponseEntity.ok(response);

        } catch (IllegalStateException ex) {
            response.put("success", false);
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (EntityNotFoundException ex) {
            response.put("success", false);
            response.put("error", "Category not found: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (DataIntegrityViolationException ex) {
            response.put("success", false);
            response.put("error", "Cannot delete due to data integrity.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } catch (Exception ex) {
            response.put("success", false);
            response.put("error", "Internal error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}