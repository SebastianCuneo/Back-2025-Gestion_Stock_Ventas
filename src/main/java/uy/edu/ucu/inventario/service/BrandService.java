package uy.edu.ucu.inventario.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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

    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final AuditLogService auditLogService;

    public BrandService(BrandRepository brandRepository, ProductRepository productRepository, AuditLogService auditLogService) {
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
        this.auditLogService = auditLogService;
    }

    public List<Brand> listAll() {
        return brandRepository.findAll();
    }

    public Optional<Brand> getById(Long id) {
        return brandRepository.findById(id);
    }

    public Brand save(Brand brand) {
        boolean isNew = (brand.getId() == null);
        if (isNew) {
            brand.setAssociatedProductCount(0); // inicializar
        }
        Brand saved = brandRepository.save(brand);

        AuditLog log = new AuditLog();
        log.setOperation(isNew ? "CREATE_BRAND" : "UPDATE_BRAND");
        log.setTimestamp(LocalDateTime.now());
        log.setUsername(null); // Spring Security más adelante
        log.setDetails("Brand " + (isNew ? "created: " : "updated: ") + brand.getName());

        auditLogService.save(log);
        return saved;
    }

    public void delete(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Brand with id " + id + " not found.");
        }

        if (productRepository.existsByBrandId(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete brand because it has associated products.");
        }

        brandRepository.deleteById(id);

        AuditLog log = new AuditLog();
        log.setOperation("DELETE_BRAND");
        log.setTimestamp(LocalDateTime.now());
        log.setUsername(null);
        log.setDetails("Brand deleted with id: " + id);

        auditLogService.save(log);
    }

    public void incrementProductCount(Brand brand) {
        brand.incrementAssociatedProductCount();
        brandRepository.save(brand);
    }

    public void decrementProductCount(Brand brand) {
        brand.decrementAssociatedProductCount();
        brandRepository.save(brand);
    }
}