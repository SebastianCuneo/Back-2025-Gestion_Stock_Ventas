package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Deposit;
import uy.edu.ucu.inventario.service.DepositService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.*;

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
    public ResponseEntity<Map<String, Object>> list() {
        List<Deposit> deposits = svc.listAll();
        List<Map<String, Object>> transformed = new ArrayList<>();

        for (Deposit d : deposits) {
            transformed.add(transformDeposit(d));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformed);
        response.put("message", "Deposit list retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    /**
     * Get a deposit by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
        return svc.getById(id)
                .map(deposit -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformDeposit(deposit));
                    response.put("message", "Deposit found.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Deposit not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    /**
     * Create a new deposit.
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Deposit d) {
        Deposit saved = svc.save(d);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformDeposit(saved));
        response.put("message", "Deposit created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Update an existing deposit.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Deposit d) {
        return svc.getById(id)
                .map(existing -> {
                    d.setId(id);
                    Deposit updated = svc.save(d);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformDeposit(updated));
                    response.put("message", "Deposit updated successfully.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Deposit not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    /**
     * Delete a deposit by ID, handling integrity exceptions.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        if (!svc.getById(id).isPresent()) {
            response.put("success", false);
            response.put("error", "Deposit not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            svc.delete(id);
            response.put("success", true);
            response.put("message", "Deposit deleted successfully.");
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException ex) {
            response.put("success", false);
            response.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (DataIntegrityViolationException ex) {
            response.put("success", false);
            response.put("error", "Cannot delete deposit due to data integrity constraints.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } catch (Exception ex) {
            response.put("success", false);
            response.put("error", "Internal error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private Map<String, Object> transformDeposit(Deposit d) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", d.getId());
        map.put("name", d.getName());
        map.put("location", d.getLocation());
        map.put("description", d.getDescription());
        map.put("productCount", d.getProductCount());
        map.put("associatedDate", d.getAssociatedDate());
        return map;
    }
}