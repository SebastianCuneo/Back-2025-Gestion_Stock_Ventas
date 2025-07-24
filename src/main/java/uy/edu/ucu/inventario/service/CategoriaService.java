package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Categoria;
import uy.edu.ucu.inventario.repository.CategoriaRepository;
import uy.edu.ucu.inventario.repository.ProductoRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de categorías de productos.
 */
@Service
public class CategoriaService {

    private final CategoriaRepository repo;
    private final ProductoRepository productoRepo;

    public CategoriaService(CategoriaRepository repo, ProductoRepository productoRepo) {
        this.repo = repo;
        this.productoRepo = productoRepo;
    }

    public List<Categoria> listar() {
        return repo.findAll();
    }

    public Optional<Categoria> obtener(Long id) {
        return repo.findById(id);
    }

    public Categoria guardar(Categoria c) {
        return repo.save(c);
    }

    public void eliminar(Long id) {
        if (productoRepo.existsByCategoriaId(id)) {
            throw new IllegalStateException("No se puede eliminar la categoría porque está en uso por productos.");
        }
        repo.deleteById(id);
    }
}