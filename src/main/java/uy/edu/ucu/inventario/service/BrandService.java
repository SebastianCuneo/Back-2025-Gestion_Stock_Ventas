package uy.edu.ucu.inventario.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import uy.edu.ucu.inventario.entity.Brand;
import uy.edu.ucu.inventario.repository.BrandRepository;
import uy.edu.ucu.inventario.repository.ProductRepository;

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
        boolean isCreate = (brand.getId() == null);
        if (isCreate) {
            brand.setAssociatedProductCount(0); // Inicializamos contador si es nuevo
        }
        Brand saved = brandRepository.save(brand);

        auditLogService.saveLog(
                "Brand",
                saved.getId(),
                isCreate ? "CREATE" : "UPDATE",
                "Brand name: " + saved.getName()
        );

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

        auditLogService.saveLog(
                "Brand",
                id,
                "DELETE",
                "Brand deleted with id: " + id
        );
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