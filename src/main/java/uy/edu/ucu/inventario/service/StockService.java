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
    private final AuditLogService auditLogService;

    public StockService(StockRepository repo, AuditLogService auditLogService) {
        this.repo = repo;
        this.auditLogService = auditLogService;
    }

    public List<Stock> listAll() {
        return repo.findAll();
    }

    public Optional<Stock> getById(Long id) {
        return repo.findById(id);
    }

    public Stock save(Stock s) {
        boolean isNew = (s.getId() == null);
        Stock saved = repo.save(s);

        auditLogService.saveLog(
            "Stock",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            null
        );

        return saved;
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Stock with id " + id + " not found");
        }

        try {
            repo.deleteById(id);

            auditLogService.saveLog(
                "Stock",
                id,
                "DELETE",
                null
            );

        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException(
                "Cannot delete stock because it is referenced by other records", ex
            );
        }
    }
}