package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Product;
import uy.edu.ucu.inventario.repository.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing products.
 * Contains business logic related to the Product entity.
 */
@Service
public class ProductService {

    private final ProductRepository repo;
    private final AuditLogService auditLogService;

    public ProductService(ProductRepository repo, AuditLogService auditLogService) {
        this.repo = repo;
        this.auditLogService = auditLogService;
    }

    /**
     * List all products.
     */
    public List<Product> listAll() {
        return repo.findAll();
    }

    /**
     * Get a product by its ID.
     */
    public Optional<Product> getById(Long id) {
        return repo.findById(id);
    }

    /**
     * Save a new product or update an existing one.
     */
    public Product save(Product p) {
        boolean isNew = (p.getId() == null);
        Product saved = repo.save(p);

        auditLogService.saveLog(
            "Product",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            null
        );

        return saved;
    }

    /**
     * Delete a product by its ID.
     */
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Product with id " + id + " not found");
        }

        try {
            repo.deleteById(id);
            auditLogService.saveLog(
                "Product",
                id,
                "DELETE",
                null
            );
        } catch (DataIntegrityViolationException ex) {
            throw ex;
        }
    }
}
