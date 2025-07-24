package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Usuario;
import uy.edu.ucu.inventario.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de usuarios.
 */
@Service
public class UsuarioService {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    public List<Usuario> listar() {
        return repo.findAll();
    }

    public Optional<Usuario> obtener(Long id) {
        return repo.findById(id);
    }

    public Usuario guardar(Usuario usuario) {
        return repo.save(usuario);
    }

    public void eliminar(Long id) {
        // 1) Verificar que el usuario existe
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Usuario con id " + id + " no encontrado");
        }
            repo.deleteById(id);
    }
}