package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.MovimientoStock;
import uy.edu.ucu.inventario.service.MovimientoStockService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * Controlador REST para los movimientos de stock.
 */
@RestController
@RequestMapping("/api/movimiento")
public class MovimientoStockController {

    private final MovimientoStockService svc;

    public MovimientoStockController(MovimientoStockService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<MovimientoStock> listar() {
        return svc.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoStock> get(@PathVariable Long id) {
        return svc.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MovimientoStock crear(@RequestBody MovimientoStock m) {
        return svc.guardar(m);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MovimientoStock> update(@PathVariable Long id, @RequestBody MovimientoStock m) {
        return svc.obtener(id)
                .map(mov -> {
                    m.setId(id);
                    return ResponseEntity.ok(svc.guardar(m));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        // 1) Validación previa: existe el movimiento?
        if (!svc.obtener(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        try {
            svc.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            // 404 si no existía justo al eliminar
            return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .body(ex.getMessage());
        } catch (DataIntegrityViolationException ex) {
            // 409 si hay integridad referencial u otros constraints
            return ResponseEntity
                   .status(HttpStatus.CONFLICT)
                   .body("No se puede eliminar el movimiento de stock por integridad de datos.");
        } catch (Exception ex) {
            // 500 para cualquier otro error
            return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error interno: " + ex.getMessage());
        }
    }
}