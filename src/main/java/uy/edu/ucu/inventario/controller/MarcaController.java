package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Marca;
import uy.edu.ucu.inventario.service.MarcaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/marcas")
public class MarcaController {
    private final MarcaService svc;
    public MarcaController(MarcaService svc) { this.svc = svc; }
    @GetMapping public List<Marca> listar() { return svc.listar(); }
    @GetMapping("/{id}") public ResponseEntity<Marca> get(@PathVariable Long id) {
        return svc.obtener(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping public Marca crear(@RequestBody Marca m) { return svc.guardar(m); }
    @PutMapping("/{id}") public ResponseEntity<Marca> update(@PathVariable Long id, @RequestBody Marca m) {
        return svc.obtener(id).map(e->{ m.setId(id); return ResponseEntity.ok(svc.guardar(m));})
                  .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) {
        if(!svc.obtener(id).isPresent()) return ResponseEntity.notFound().build();
        svc.eliminar(id); return ResponseEntity.noContent().build();
    }
}