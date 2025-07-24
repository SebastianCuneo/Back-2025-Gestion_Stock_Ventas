package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Proveedor;
import uy.edu.ucu.inventario.service.ProveedorService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * Controlador REST para la entidad Proveedor.
 */
@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService svc;

    public ProveedorController(ProveedorService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<Proveedor> listar() {
        return svc.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> get(@PathVariable Long id) {
        return svc.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Proveedor crear(@RequestBody Proveedor p) {
        return svc.guardar(p);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> update(@PathVariable Long id, @RequestBody Proveedor p) {
        return svc.obtener(id)
                .map(provExistente -> {
                    p.setId(id);
                    return ResponseEntity.ok(svc.guardar(p));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        // Validación previa: existe el proveedor?
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

        } catch (Exception ex) {
            // 500 para cualquier otro error
            return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error interno: " + ex.getMessage());
        }
    }
}