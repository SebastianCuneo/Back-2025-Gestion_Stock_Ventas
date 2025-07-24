package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Deposito;
import uy.edu.ucu.inventario.service.DepositoService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la entidad Depósito.
 */
@RestController
@RequestMapping("/api/depositos")
public class DepositoController {

    private final DepositoService svc;

    public DepositoController(DepositoService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<Deposito> listar() {
        return svc.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Deposito> get(@PathVariable Long id) {
        return svc.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Deposito crear(@RequestBody Deposito d) {
        return svc.guardar(d);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Deposito> update(@PathVariable Long id, @RequestBody Deposito d) {
        return svc.obtener(id)
                .map(depExistente -> {
                    d.setId(id);
                    return ResponseEntity.ok(svc.guardar(d));
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