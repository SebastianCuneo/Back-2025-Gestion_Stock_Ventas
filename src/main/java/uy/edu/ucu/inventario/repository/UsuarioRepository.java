package uy.edu.ucu.inventario.repository;

import uy.edu.ucu.inventario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Permite buscar un usuario por su email (útil para login o validaciones futuras)
    boolean existsByEmail(String email);
}