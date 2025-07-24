package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Stock;
import uy.edu.ucu.inventario.service.StockService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!svc.obtener(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        svc.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}