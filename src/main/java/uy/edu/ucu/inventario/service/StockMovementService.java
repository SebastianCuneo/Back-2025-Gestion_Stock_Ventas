package uy.edu.ucu.inventario.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import uy.edu.ucu.inventario.entity.StockMovement;
import uy.edu.ucu.inventario.enums.MovementType;
import uy.edu.ucu.inventario.repository.StockMovementRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing stock movements (entries, exits, transfers).
 */
@Service
public class StockMovementService {

    private final StockMovementRepository repo;
    private final AuditLogService auditLogService;

    public StockMovementService(StockMovementRepository repo, AuditLogService auditLogService) {
        this.repo = repo;
        this.auditLogService = auditLogService;
    }

    public List<StockMovement> listAll() {
        return repo.findAll();
    }

    public Optional<StockMovement> getById(Long id) {
        return repo.findById(id);
    }

    public StockMovement save(StockMovement movement) {
        boolean isNew = (movement.getId() == null);
        StockMovement saved = repo.save(movement);

        auditLogService.saveLog(
            "StockMovement",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            null
        );

        return saved;
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Stock movement with id " + id + " not found.");
        }

        repo.deleteById(id);

        auditLogService.saveLog(
            "StockMovement",
            id,
            "DELETE",
            null
        );
    }

    // === Métodos adicionales previstos ===

    public List<StockMovement> findByType(MovementType type) {
        return repo.findByType(type);
    }

    public List<StockMovement> findByOriginDeposit(Long depositId) {
        return repo.findByOriginDepositId(depositId);
    }

    public List<StockMovement> findByDestinationDeposit(Long depositId) {
        return repo.findByDestinationDepositId(depositId);
    }

    public List<StockMovement> findTransfersBetweenDeposits(Long originId, Long destinationId) {
        return repo.findByOriginDepositIdAndDestinationDepositId(originId, destinationId);
    }
}