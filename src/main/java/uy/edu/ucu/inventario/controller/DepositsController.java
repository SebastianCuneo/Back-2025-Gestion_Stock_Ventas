package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Deposit;
import uy.edu.ucu.inventario.service.DepositService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * REST Controller for managing Deposits.
 */
@RestController
@RequestMapping("/api/deposits")
public class DepositsController {

    private final DepositService svc;

    public DepositsController(DepositService svc) {
        this.svc = svc;
    }

    /**
     * Get all deposits.
     */
    @GetMapping
    public List<Deposit> list() {
        return svc.listAll();
    }

    /**
     * Get a deposit by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Deposit> get(@PathVariable Long id) {
        return svc.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new deposit.
     */
    @PostMapping
    public Deposit create(@RequestBody Deposit d) {
        return svc.save(d);
    }

    /**
     * Update an existing deposit.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Deposit> update(@PathVariable Long id, @RequestBody Deposit d) {
        return svc.getById(id)
                .map(existing -> {
                    d.setId(id);
                    return ResponseEntity.ok(svc.save(d));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a deposit by ID, handling integrity exceptions.
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
                    .body("Cannot delete deposit due to data integrity constraints.");

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal error: " + ex.getMessage());
        }
    }
}
