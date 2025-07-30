package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Product;
import uy.edu.ucu.inventario.entity.Brand;
import uy.edu.ucu.inventario.entity.Category;
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

    private final ProductRepository productRepository;
    private final AuditLogService auditLogService;
    private final BrandService brandService;
    private final CategoryService categoryService;

    public ProductService(
        ProductRepository productRepository,
        AuditLogService auditLogService,
        BrandService brandService,
        CategoryService categoryService
    ) {
        this.productRepository = productRepository;
        this.auditLogService = auditLogService;
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    public List<Product> listAll() {
        return productRepository.findAll();
    }

    public Optional<Product> getById(Long id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        boolean isNew = (product.getId() == null);
        Product saved = productRepository.save(product);

        if (isNew) {
            Brand brand = saved.getBrand();
            Category category = saved.getCategory();
            brandService.incrementProductCount(brand);
            categoryService.incrementProductCount(category);
        }

        auditLogService.saveLog(
            "Product",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            null
        );

        return saved;
    }

    public void delete(Long id) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isEmpty()) {
            throw new EntityNotFoundException("Product with id " + id + " not found");
        }

        Product product = productOpt.get();
        try {
            productRepository.deleteById(id);

            brandService.decrementProductCount(product.getBrand());
            categoryService.decrementProductCount(product.getCategory());

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

    public void incrementDepositsCount(Product product) {
        product.setDepositsCount(product.getDepositsCount() + 1);
        productRepository.save(product);
    }

    public void decrementDepositsCount(Product product) {
        int current = product.getDepositsCount();
        if (current > 0) {
            product.setDepositsCount(current - 1);
            productRepository.save(product);
        }
    }
}