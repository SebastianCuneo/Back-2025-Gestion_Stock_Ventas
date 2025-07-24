package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Categoria;
import uy.edu.ucu.inventario.service.CategoriaService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * Controlador REST para la entidad Categoria.
 * Permite realizar operaciones CRUD sobre las categorías de productos.
 */
@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService svc;

    public CategoriaController(CategoriaService svc) {
        this.svc = svc;
    }

    /**
     * Obtener todas las categorías.
     */
    @GetMapping
    public List<Categoria> listar() {
        return svc.listar();
    }

    /**
     * Obtener una categoría por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> get(@PathVariable Long id) {
        return svc.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crear una nueva categoría.
     */
    @PostMapping
    public Categoria crear(@RequestBody Categoria c) {
        return svc.guardar(c);
    }

    /**
     * Actualizar una categoría existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> update(@PathVariable Long id, @RequestBody Categoria c) {
        return svc.obtener(id)
                .map(catExistente -> {
                    c.setId(id);
                    return ResponseEntity.ok(svc.guardar(c));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Eliminar una categoría por ID, si no está en uso por productos.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        // Validación previa: existe la categoría?
        if (!svc.obtener(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        try {
            svc.eliminar(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalStateException ex) {
            // La categoría está en uso por productos
            return ResponseEntity
                    .badRequest()
                    .body(ex.getMessage());

        } catch (EntityNotFoundException ex) {
            // Se lanzó tras el delete si no existía (doble chequeo)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Categoría no encontrada: " + ex.getMessage());

        } catch (DataIntegrityViolationException ex) {
            // En caso de violaciones de integridad referencial
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("No se puede eliminar por integridad de datos.");

        } catch (Exception ex) {
            // Cualquier otro error
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno: " + ex.getMessage());
        }
    }}