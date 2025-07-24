package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.MovimientoStock;
import uy.edu.ucu.inventario.repository.MovimientoStockRepository;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de movimientos de stock (entradas y salidas).
 */
@Service
public class MovimientoStockService {

    private final MovimientoStockRepository repo;

    public MovimientoStockService(MovimientoStockRepository repo) {
        this.repo = repo;
    }

    public List<MovimientoStock> listar() {
        return repo.findAll();
    }

    public Optional<MovimientoStock> obtener(Long id) {
        return repo.findById(id);
    }

    public MovimientoStock guardar(MovimientoStock m) {
        return repo.save(m);
    }

    public void eliminar(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("MovimientoStock con id " + id + " no encontrado");
        }
        repo.deleteById(id);
    }
}