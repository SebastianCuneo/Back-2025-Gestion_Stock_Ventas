package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Deposit;
import uy.edu.ucu.inventario.repository.DepositRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing deposits.
 */
@Service
public class DepositService {

    private final DepositRepository repo;
    private final AuditLogService auditLogService;

    public DepositService(DepositRepository repo, AuditLogService auditLogService) {
        this.repo = repo;
        this.auditLogService = auditLogService;
    }

    public List<Deposit> listAll() {
        return repo.findAll();
    }

    public Optional<Deposit> getById(Long id) {
        return repo.findById(id);
    }

    public Deposit save(Deposit d) {
        boolean isNew = (d.getId() == null);

        if (isNew) {
            d.setProductCount(0); // inicializa contador de productos
            d.setAssociatedDate(LocalDateTime.now()); // registra la fecha de creación
        }

        Deposit saved = repo.save(d);

        auditLogService.saveLog(
            "Deposit",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            null
        );

        return saved;
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Deposit with id " + id + " not found");
        }
        repo.deleteById(id);

        auditLogService.saveLog(
            "Deposit",
            id,
            "DELETE",
            null
        );
    }

    public void incrementProductCount(Deposit deposit) {
        deposit.setProductCount(deposit.getProductCount() + 1);
        repo.save(deposit);
    }

    public void decrementProductCount(Deposit deposit) {
        if (deposit.getProductCount() > 0) {
            deposit.setProductCount(deposit.getProductCount() - 1);
            repo.save(deposit);
        }
    }
}