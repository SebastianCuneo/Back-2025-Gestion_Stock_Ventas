package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Provider;
import uy.edu.ucu.inventario.service.ProviderService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * Controlador REST para la entidad Provider.
 */
@RestController
@RequestMapping("/api/providers")
public class ProvidersController {

    private final ProviderService svc;

    public ProvidersController(ProviderService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<Provider> list() {
        return svc.listAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Provider> get(@PathVariable Long id) {
        return svc.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Provider crear(@RequestBody Provider p) {
        return svc.save(p);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Provider> update(@PathVariable Long id, @RequestBody Provider p) {
        return svc.getById(id)
                .map(provExistente -> {
                    p.setId(id);
                    return ResponseEntity.ok(svc.save(p));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        // Validación previa: existe el proveedor?
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