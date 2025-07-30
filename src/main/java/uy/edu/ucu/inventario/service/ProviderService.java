package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Provider;
import uy.edu.ucu.inventario.repository.ProviderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing providers.
 */
@Service
public class ProviderService {

    private final ProviderRepository repo;
    private final AuditLogService auditLogService;

    public ProviderService(ProviderRepository repo, AuditLogService auditLogService) {
        this.repo = repo;
        this.auditLogService = auditLogService;
    }

    public List<Provider> listAll() {
        return repo.findAll();
    }

    public Optional<Provider> getById(Long id) {
        return repo.findById(id);
    }

    public Provider save(Provider p) {
        boolean isNew = (p.getId() == null);

        if (isNew) {
            p.setAssociatedDate(LocalDateTime.now());
        }

        Provider saved = repo.save(p);

        auditLogService.saveLog(
            "Provider",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            null
        );

        return saved;
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Provider with id " + id + " not found");
        }

        repo.deleteById(id);

        auditLogService.saveLog(
            "Provider",
            id,
            "DELETE",
            null
        );
    }
}