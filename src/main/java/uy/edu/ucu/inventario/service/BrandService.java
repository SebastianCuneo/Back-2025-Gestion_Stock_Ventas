package uy.edu.ucu.inventario.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import uy.edu.ucu.inventario.entity.AuditLog;
import uy.edu.ucu.inventario.entity.Brand;
import uy.edu.ucu.inventario.repository.BrandRepository;
import uy.edu.ucu.inventario.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for managing brands.
 */
@Service
public class BrandService {

    private final BrandRepository repo;
    private final ProductRepository productRepo;
    private final AuditLogService auditLogService;

    public BrandService(BrandRepository repo, ProductRepository productRepo, AuditLogService auditLogService) {
        this.repo = repo;
        this.productRepo = productRepo;
        this.auditLogService = auditLogService;
    }

    public List<Brand> listAll() {
        return repo.findAll();
    }

    public Optional<Brand> getById(Long id) {
        return repo.findById(id);
    }

    public Brand save(Brand brand) {
        boolean isNew = (brand.getId() == null);
        if (isNew) {
            brand.setAssociatedProductCount(0); // inicializar
        }
        Brand saved = repo.save(brand);

        AuditLog log = new AuditLog();
        log.setOperation(isNew ? "CREATE_BRAND" : "UPDATE_BRAND");
        log.setTimestamp(LocalDateTime.now());
        log.setUsername(null); // Spring Security más adelante
        log.setDetails("Brand " + (isNew ? "created: " : "updated: ") + brand.getName());

        auditLogService.save(log);
        return saved;
    }

    public void delete(Long id) {
        if (productRepo.existsByBrandId(id)) {
            throw new IllegalStateException("Cannot delete brand because it has associated products.");
        }
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Brand with id " + id + " not found.");
        }
        repo.deleteById(id);

        AuditLog log = new AuditLog();
        log.setOperation("DELETE_BRAND");
        log.setTimestamp(LocalDateTime.now());
        log.setUsername(null);
        log.setDetails("Brand deleted with id: " + id);

        auditLogService.save(log);
    }

    public void incrementProductCount(Brand brand) {
        brand.incrementAssociatedProductCount();
        repo.save(brand);
    }

    public void decrementProductCount(Brand brand) {
        brand.decrementAssociatedProductCount();
        repo.save(brand);
    }
}