package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Usuario;
import uy.edu.ucu.inventario.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de usuarios.
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService svc;

    public UsuarioController(UsuarioService svc) {
        this.svc = svc;
    }

    // Obtener todos los usuarios
    @GetMapping
    public List<Usuario> listar() {
        return svc.listar();
    }

    // Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> get(@PathVariable Long id) {
        return svc.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear nuevo usuario
    @PostMapping
    public Usuario crear(@RequestBody Usuario usuario) {
        return svc.guardar(usuario);
    }

    // Actualizar usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Long id, @RequestBody Usuario usuario) {
        return svc.obtener(id)
                .map(u -> {
                    usuario.setId(id);
                    return ResponseEntity.ok(svc.guardar(usuario));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!svc.obtener(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        svc.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}