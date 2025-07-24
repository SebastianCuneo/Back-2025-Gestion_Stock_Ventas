package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Producto;
import uy.edu.ucu.inventario.service.ProductoService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la entidad Producto.
 * Proporciona operaciones CRUD sobre los productos del sistema.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService svc;

    public ProductoController(ProductoService svc) {
        this.svc = svc;
    }

    /**
     * Obtener la lista de todos los productos.
     */
    @GetMapping
    public List<Producto> listar() {
        return svc.listar();
    }

    /**
     * Obtener un producto por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> get(@PathVariable Long id) {
        return svc.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crear un nuevo producto.
     */
    @PostMapping
    public Producto crear(@RequestBody Producto p) {
        return svc.guardar(p);
    }

    /**
     * Actualizar un producto existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> update(@PathVariable Long id, @RequestBody Producto p) {
        return svc.obtener(id)
                .map(productoExistente -> {
                    p.setId(id);
                    return ResponseEntity.ok(svc.guardar(p));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Eliminar un producto por su ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        // 1) Validación previa: existe el producto?
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
        } catch (Exception ex) {
            // 500 para cualquier otro error
            return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Error interno: " + ex.getMessage());
        }
    }
}
