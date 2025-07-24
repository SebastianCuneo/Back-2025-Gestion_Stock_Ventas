package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Marca;
import uy.edu.ucu.inventario.service.MarcaService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * Controlador REST para la entidad Marca.
 * Proporciona operaciones CRUD sobre las marcas del sistema.
 */
@RestController
@RequestMapping("/api/marcas")
public class MarcaController {

    private final MarcaService svc;

    public MarcaController(MarcaService svc) {
        this.svc = svc;
    }

    /**
     * Obtener la lista de todas las marcas.
     */
    @GetMapping
    public List<Marca> listar() {
        return svc.listar();
    }

    /**
     * Obtener una marca por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Marca> get(@PathVariable Long id) {
        return svc.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crear una nueva marca.
     */
    @PostMapping
    public Marca crear(@RequestBody Marca m) {
        return svc.guardar(m);
    }

    /**
     * Actualizar una marca existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Marca> update(@PathVariable Long id, @RequestBody Marca m) {
        return svc.obtener(id)
                .map(marcaExistente -> {
                    m.setId(id);
                    return ResponseEntity.ok(svc.guardar(m));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Eliminar una marca por ID si no está asociada a productos.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        // Validación previa: existe la marca?
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
            // 409 si hay restricciones (p.ej. productos referenciando esta marca)
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("No se puede eliminar la marca por integridad de datos.");

        } catch (Exception ex) {
            // 500 para cualquier otro error inesperado
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno: " + ex.getMessage());
        }
    }
}