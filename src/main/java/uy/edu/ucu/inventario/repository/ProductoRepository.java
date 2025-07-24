package uy.edu.ucu.inventario.repository;

import uy.edu.ucu.inventario.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Este método permite verificar si hay productos con una marca específica
	boolean existsByMarcaId(Long id);
	// Este método permite verificar si hay productos con una categoría específica
	boolean existsByCategoriaId(Long categoriaId);
}
