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
 * REST Controller for managing deposits.
 */
@RestController
@RequestMapping("/api/deposit")
public class DepositController {

    private final DepositService depositService;

    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    /**
     * Get all deposits.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> list() {
        List<Deposit> deposits = depositService.listAll();
        List<Map<String, Object>> transformed = new ArrayList<>();

        for (Deposit deposit : deposits) {
            transformed.add(transformDeposit(deposit));
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
        return depositService.getById(id)
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
    public ResponseEntity<Map<String, Object>> create(@RequestBody Deposit deposit) {
        Deposit saved = depositService.save(deposit);
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
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Deposit deposit) {
        return depositService.getById(id)
                .map(existing -> {
                    deposit.setId(id);
                    Deposit updated = depositService.save(deposit);
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

        if (!depositService.getById(id).isPresent()) {
            response.put("success", false);
            response.put("error", "Deposit not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            depositService.delete(id);
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

    private Map<String, Object> transformDeposit(Deposit deposit) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", deposit.getId());
        map.put("name", deposit.getName());
        map.put("location", deposit.getLocation());
        map.put("description", deposit.getDescription());
        map.put("productCount", deposit.getProductCount());
        map.put("associatedDate", deposit.getAssociatedDate());
        return map;
    }
}