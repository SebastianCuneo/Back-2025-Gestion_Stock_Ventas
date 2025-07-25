package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Brand;
import uy.edu.ucu.inventario.repository.BrandRepository;
import uy.edu.ucu.inventario.repository.ProductRepository;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing brands.
 */
@Service
public class BrandService {

    private final BrandRepository repo;
    private final ProductRepository productRepo;

    public BrandService(BrandRepository repo, ProductRepository productRepo) {
        this.repo = repo;
        this.productRepo = productRepo;
    }

    public List<Brand> listAll() {
        return repo.findAll();
    }

    public Optional<Brand> getById(Long id) {
        return repo.findById(id);
    }

    public Brand save(Brand brand) {
        return repo.save(brand);
    }

    public void delete(Long id) {
        if (productRepo.existsByBrandId(id)) {
            throw new IllegalStateException("Cannot delete brand because it has associated products.");
        }
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Brand with id " + id + " not found.");
        }
        repo.deleteById(id);
    }
}
