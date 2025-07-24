package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.MovimientoStock;
import uy.edu.ucu.inventario.service.MovimientoStockService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para los movimientos de stock.
 */
@RestController
@RequestMapping("/api/movimientos")
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!svc.obtener(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        svc.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}