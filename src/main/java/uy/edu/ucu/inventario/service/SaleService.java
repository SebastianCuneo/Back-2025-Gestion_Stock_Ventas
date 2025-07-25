package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Sale;
import uy.edu.ucu.inventario.repository.SaleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing sales.
 */
@Service
public class SaleService {

    private final SaleRepository repo;
    private final AuditLogService auditLogService;

    public SaleService(SaleRepository repo, AuditLogService auditLogService) {
        this.repo = repo;
        this.auditLogService = auditLogService;
    }

    public List<Sale> listAll() {
        return repo.findAll();
    }

    public Optional<Sale> getById(Long id) {
        return repo.findById(id);
    }

    public Sale save(Sale sale) {
        boolean isNew = (sale.getId() == null);
        Sale saved = repo.save(sale);

        auditLogService.saveLog(
            "Sale",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            null
        );

        return saved;
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Sale with id " + id + " not found");
        }

        repo.deleteById(id);

        auditLogService.saveLog(
            "Sale",
            id,
            "DELETE",
            null
        );
    }
}