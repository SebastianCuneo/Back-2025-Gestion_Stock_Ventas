package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Marca;
import uy.edu.ucu.inventario.repository.MarcaRepository;
import uy.edu.ucu.inventario.repository.ProductoRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MarcaService {

    private final MarcaRepository repo;
    private final ProductoRepository productoRepo;

    public MarcaService(MarcaRepository repo, ProductoRepository productoRepo) {
        this.repo = repo;
        this.productoRepo = productoRepo;
    }

    public List<Marca> listar() {
        return repo.findAll();
    }

    public Optional<Marca> obtener(Long id) {
        return repo.findById(id);
    }

    public Marca guardar(Marca m) {
        return repo.save(m);
    }

    public void eliminar(Long id) {
        if (productoRepo.existsByMarcaId(id)) {
            throw new IllegalStateException("No se puede eliminar la marca porque tiene productos asociados.");
        }
        repo.deleteById(id);
    }
}