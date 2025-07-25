package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Category;
import uy.edu.ucu.inventario.repository.CategoryRepository;
import uy.edu.ucu.inventario.repository.ProductRepository;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing product categories.
 */
@Service
public class CategoryService {

    private final CategoryRepository repo;
    private final ProductRepository productRepo;

    public CategoryService(CategoryRepository repo, ProductRepository productRepo) {
        this.repo = repo;
        this.productRepo = productRepo;
    }

    public List<Category> listAll() {
        return repo.findAll();
    }

    public Optional<Category> getById(Long id) {
        return repo.findById(id);
    }

    public Category save(Category c) {
        return repo.save(c);
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
    }
}
