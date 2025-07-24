package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Proveedor;
import uy.edu.ucu.inventario.service.ProveedorService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!svc.obtener(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        svc.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}