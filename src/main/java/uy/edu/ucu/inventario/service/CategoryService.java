package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.AuditLog;
import uy.edu.ucu.inventario.entity.Category;
import uy.edu.ucu.inventario.repository.CategoryRepository;
import uy.edu.ucu.inventario.repository.ProductRepository;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository repo;
    private final ProductRepository productRepo;
    private final AuditLogService auditLogService;

    public CategoryService(CategoryRepository repo, ProductRepository productRepo, AuditLogService auditLogService) {
        this.repo = repo;
        this.productRepo = productRepo;
        this.auditLogService = auditLogService;
    }

    public List<Category> listAll() {
        return repo.findAll();
    }

    public Optional<Category> getById(Long id) {
        return repo.findById(id);
    }

    public Category save(Category c) {

        boolean isCreate = (c.getId() == null);

        Category saved = repo.save(c);

        auditLogService.saveLog(
            "Category",
            saved.getId(),
            isCreate ? "CREATE" : "UPDATE",
            null
        );

        return saved;
    }

    public void delete(Long id) {
        Optional<Category> catOpt = repo.findById(id);
        if (catOpt.isEmpty()) {
            throw new EntityNotFoundException("Category with id " + id + " not found.");
        }

        if (productRepo.existsByCategoryId(id)) {
            throw new IllegalStateException("Cannot delete category because it is used by products.");
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
