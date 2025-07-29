package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Stock;
import uy.edu.ucu.inventario.service.StockService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for the Stock entity.
 * Manages the quantity of products in deposits.
 */
@RestController
@RequestMapping("/api/stocks") // se pluraliza en inglés para ser consistente
public class StocksController {

    private final StockService svc;

    public StocksController(StockService svc) {
        this.svc = svc;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list() {
        List<Stock> stocks = svc.listAll();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", stocks);
        response.put("message", "Stocks list retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        return svc.getById(id)
                .map(stock -> {
                    response.put("success", true);
                    response.put("data", stock);
                    response.put("message", "Stock found.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.put("success", false);
                    response.put("error", "Stock not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Stock s) {
        Stock saved = svc.save(s);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", saved);
        response.put("message", "Stock created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Stock s) {
        Map<String, Object> response = new HashMap<>();
        return svc.getById(id)
                .map(existing -> {
                    s.setId(id);
                    Stock updated = svc.save(s);
                    response.put("success", true);
                    response.put("data", updated);
                    response.put("message", "Stock updated successfully.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    response.put("success", false);
                    response.put("error", "Stock not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        if (!svc.getById(id).isPresent()) {
            response.put("success", false);
            response.put("error", "Stock not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            svc.delete(id);
            response.put("success", true);
            response.put("message", "Stock deleted successfully.");
            return ResponseEntity.ok(response);

        } catch (IllegalStateException ex) {
            response.put("success", false);
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);

        } catch (EntityNotFoundException ex) {
            response.put("success", false);
            response.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (DataIntegrityViolationException ex) {
            response.put("success", false);
            response.put("error", "Cannot delete stock due to data integrity constraints.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } catch (Exception ex) {
            response.put("success", false);
            response.put("error", "Internal error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}