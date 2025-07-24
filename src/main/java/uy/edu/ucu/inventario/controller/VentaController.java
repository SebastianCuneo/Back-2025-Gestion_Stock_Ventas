package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Venta;
import uy.edu.ucu.inventario.service.VentaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * Controlador REST para la entidad Venta.
 */
@RestController
@RequestMapping("/api/venta")
public class VentaController {

    private final VentaService svc;

    public VentaController(VentaService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<Venta> listar() {
        return svc.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venta> get(@PathVariable Long id) {
        return svc.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Venta crear(@RequestBody Venta v) {
        return svc.guardar(v);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Venta> update(@PathVariable Long id, @RequestBody Venta v) {
        return svc.obtener(id)
                .map(ventaExistente -> {
                    v.setId(id);
                    return ResponseEntity.ok(svc.guardar(v));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        // Pre-check: existe la venta?
        if (!svc.obtener(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        try {
            svc.eliminar(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalStateException ex) {
            // 400 si dependencias impiden el borrado
            return ResponseEntity
                   .badRequest()
                   .body(ex.getMessage());

        } catch (EntityNotFoundException ex) {
            // 404 si no existía justo al eliminar
            return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .body(ex.getMessage());

        } catch (Exception ex) {
            // 500 para cualquier otro error
            return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error interno: " + ex.getMessage());
        }
    }
}