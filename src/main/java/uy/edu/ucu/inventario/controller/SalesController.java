package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Sale;
import uy.edu.ucu.inventario.service.SaleService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * REST Controller for the Sale entity.
 */
@RestController
@RequestMapping("/api/sales")
public class SalesController {

    private final SaleService svc;

    public SalesController(SaleService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<Sale> list() {
        return svc.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> get(@PathVariable Long id) {
        return svc.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Sale create(@RequestBody Sale s) {
        return svc.save(s);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sale> update(@PathVariable Long id, @RequestBody Sale s) {
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

        } catch (Exception ex) {
            return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Internal error: " + ex.getMessage());
        }
    }
}