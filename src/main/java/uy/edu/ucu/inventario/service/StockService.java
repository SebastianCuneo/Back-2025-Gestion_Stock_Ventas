package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Stock;
import uy.edu.ucu.inventario.repository.StockRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión del stock de productos en depósitos.
 */
@Service
public class StockService {

    private final StockRepository repo;

    public StockService(StockRepository repo) {
        this.repo = repo;
    }

    public List<Stock> listar() {
        return repo.findAll();
    }

    public Optional<Stock> obtener(Long id) {
        return repo.findById(id);
    }

    public Stock guardar(Stock s) {
        return repo.save(s);
    }

    public void eliminar(Long id) {
        // 1) Verificar que el stock existe
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Stock con id " + id + " no encontrado");
        }
        // 2) Intentar el borrado y capturar dependencias
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException(
                "No se puede eliminar el stock porque está en uso por movimientos o ventas", ex
            );
        }
    }
}