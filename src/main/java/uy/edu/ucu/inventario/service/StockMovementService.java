package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.StockMovement;
import uy.edu.ucu.inventario.repository.StockMovementRepository;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing stock movements (entries and exits).
 */
@Service
public class StockMovementService {

    private final StockMovementRepository repo;

    public StockMovementService(StockMovementRepository repo) {
        this.repo = repo;
    }

    public List<StockMovement> listAll() {
        return repo.findAll();
    }

    public Optional<StockMovement> getById(Long id) {
        return repo.findById(id);
    }

    public StockMovement save(StockMovement movement) {
        return repo.save(movement);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Stock movement with id " + id + " not found.");
        }
        repo.deleteById(id);
    }
}
