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

    private final DepositRepository depositRepository;
    private final AuditLogService auditLogService;

    public DepositService(DepositRepository depositRepository, AuditLogService auditLogService) {
        this.depositRepository = depositRepository;
        this.auditLogService = auditLogService;
    }

    public List<Deposit> listAll() {
        return depositRepository.findAll();
    }

    public Optional<Deposit> getById(Long id) {
        return depositRepository.findById(id);
    }

    public Deposit save(Deposit deposit) {
        boolean isNew = (deposit.getId() == null);

        if (isNew) {
            deposit.setProductCount(0); // inicializa contador de productos
            deposit.setAssociatedDate(LocalDateTime.now()); // registra la fecha de creación
        }

        Deposit saved = depositRepository.save(deposit);

        auditLogService.saveLog(
            "Deposit",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            null
        );

        return saved;
    }

    public void delete(Long id) {
        if (!depositRepository.existsById(id)) {
            throw new EntityNotFoundException("Deposit with id " + id + " not found");
        }
        depositRepository.deleteById(id);

        auditLogService.saveLog(
            "Deposit",
            id,
            "DELETE",
            null
        );
    }

    public void incrementProductCount(Deposit deposit) {
        deposit.setProductCount(deposit.getProductCount() + 1);
        depositRepository.save(deposit);
    }

    public void decrementProductCount(Deposit deposit) {
        if (deposit.getProductCount() > 0) {
            deposit.setProductCount(deposit.getProductCount() - 1);
            depositRepository.save(deposit);
        }
    }
}