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
 * Controlador REST para la entidad Stock.
 * Permite gestionar la cantidad de productos en depósitos.
 */
@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService svc;

    public StockController(StockService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<Stock> listar() {
        return svc.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> get(@PathVariable Long id) {
        return svc.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Stock crear(@RequestBody Stock s) {
        return svc.guardar(s);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Stock> update(@PathVariable Long id, @RequestBody Stock s) {
        return svc.obtener(id)
                .map(stockExistente -> {
                    s.setId(id);
                    return ResponseEntity.ok(svc.guardar(s));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        // Validación previa: existe el stock?
        if (!svc.obtener(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        try {
            svc.eliminar(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalStateException ex) {
            // 400 si hay dependencia en uso
            return ResponseEntity
                   .badRequest()
                   .body(ex.getMessage());

        } catch (EntityNotFoundException ex) {
            // 404 si no existía justo al eliminar
            return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .body(ex.getMessage());

        } catch (DataIntegrityViolationException ex) {
            // 409 si restricción de integridad
            return ResponseEntity
                   .status(HttpStatus.CONFLICT)
                   .body("No se puede eliminar el stock por integridad de datos.");

        } catch (Exception ex) {
            // 500 para cualquier otro error
            return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error interno: " + ex.getMessage());
        }
    }
}