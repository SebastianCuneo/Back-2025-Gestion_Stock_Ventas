package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.StockMovement;
import uy.edu.ucu.inventario.service.StockMovementService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * REST Controller for stock movements.
 */
@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    private final StockMovementService svc;

    public StockMovementController(StockMovementService svc) {
        this.svc = svc;
    }

    /**
     * Retrieve all stock movements.
     */
    @GetMapping
    public List<StockMovement> list() {
        return svc.listAll();
    }

    /**
     * Retrieve a stock movement by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StockMovement> get(@PathVariable Long id) {
        return svc.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new stock movement.
     */
    @PostMapping
    public StockMovement create(@RequestBody StockMovement m) {
        return svc.save(m);
    }

    /**
     * Update an existing stock movement.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StockMovement> update(@PathVariable Long id, @RequestBody StockMovement m) {
        return svc.getById(id)
                .map(existing -> {
                    m.setId(id);
                    return ResponseEntity.ok(svc.save(m));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a stock movement by ID.
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
                   .body("Cannot delete stock movement due to data integrity constraints.");

        } catch (Exception ex) {
            return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Internal error: " + ex.getMessage());
        }
    }
}