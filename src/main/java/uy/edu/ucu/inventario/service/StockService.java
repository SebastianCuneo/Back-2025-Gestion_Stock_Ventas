package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Stock;
import uy.edu.ucu.inventario.repository.StockRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing product stock in deposits.
 */
@Service
public class StockService {

    private final StockRepository repo;

    public StockService(StockRepository repo) {
        this.repo = repo;
    }

    public List<Stock> listAll() {
        return repo.findAll();
    }

    public Optional<Stock> getById(Long id) {
        return repo.findById(id);
    }

    public Stock save(Stock s) {
        return repo.save(s);
    }

    public void delete(Long id) {
        // 1) Check if stock exists
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Stock with id " + id + " not found");
        }
        // 2) Attempt deletion and catch integrity violations
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException(
                "Cannot delete stock because it is referenced by other records", ex
            );
        }
    }
}
