package uy.edu.ucu.inventario.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import uy.edu.ucu.inventario.entity.AuditLog;
import uy.edu.ucu.inventario.entity.Brand;
import uy.edu.ucu.inventario.entity.Category;
import uy.edu.ucu.inventario.entity.User;
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

    public Brand save(Brand b) {
        boolean isCreate = (b.getId() == null);
        Brand saved = repo.save(b);
        
            auditLogService.saveLog(
                "Brand", 
                saved.getId(), 
                isCreate ? "CREATE" : "UPDATE", 
                "Brand name: " + saved.getName()
            );
        return saved;
    }
    public void delete(Long id) {
    	if (productRepo.existsByBrandId(id)) {
            throw new IllegalStateException("Cannot delete brand because it has associated products.");
        }
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Brand with id " + id + " not found");
        }
        repo.deleteById(id);
        
        auditLogService.saveLog(
                "Category",
                id,
                "DELETE",
                null
            );
    }
    
}
