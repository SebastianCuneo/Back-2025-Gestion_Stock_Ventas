package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Producto;
import uy.edu.ucu.inventario.repository.ProductoRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de productos.
 * Contiene la lógica de negocio relacionada a la entidad Producto.
 */
@Service
public class ProductoService {

    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    /**
     * Listar todos los productos.
     */
    public List<Producto> listar() {
        return repo.findAll();
    }

    /**
     * Obtener un producto por su ID.
     */
    public Optional<Producto> obtener(Long id) {
        return repo.findById(id);
    }

    /**
     * Guardar un nuevo producto o actualizar uno existente.
     */
    public Producto guardar(Producto p) {
        return repo.save(p);
    }

    /**
     * Eliminar un producto por su ID.
     */
    public void eliminar(Long id) {
        // 1) Validación: ¿existe el producto?
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Producto con id " + id + " no encontrado");
        }
        // 2) Intento de borrado
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            // Re-lanzar para que lo capture el controlador
            throw ex;
        }
    }
}