package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Stock;
import uy.edu.ucu.inventario.service.StockService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

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
    public List<Stock> list() {
        return svc.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> get(@PathVariable Long id) {
        return svc.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Stock create(@RequestBody Stock s) {
        return svc.save(s);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> update(@PathVariable Long id, @RequestBody Stock s) {
        return svc.getById(id)
                .map(existing -> {
                    s.setId(id);
                    return ResponseEntity.ok(svc.save(s));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!svc.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            svc.delete(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalStateException ex) {
            return ResponseEntity
                   .badRequest()
                   .body(ex.getMessage());

        } catch (EntityNotFoundException ex) {
            return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .body(ex.getMessage());

        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity
                   .status(HttpStatus.CONFLICT)
                   .body("Cannot delete stock due to data integrity constraints.");

        } catch (Exception ex) {
            return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Internal error: " + ex.getMessage());
        }
    }
}