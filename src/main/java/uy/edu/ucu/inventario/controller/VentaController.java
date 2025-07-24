package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Venta;
import uy.edu.ucu.inventario.service.VentaService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la entidad Venta.
 */
@RestController
@RequestMapping("/api/ventas")
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!svc.obtener(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        svc.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}