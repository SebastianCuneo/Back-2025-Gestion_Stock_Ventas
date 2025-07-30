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

    private final ProductRepository repo;
    private final AuditLogService auditLogService;
    private final BrandService brandService;
    private final CategoryService categoryService;

    public ProductService(
        ProductRepository repo,
        AuditLogService auditLogService,
        BrandService brandService,
        CategoryService categoryService
    ) {
        this.repo = repo;
        this.auditLogService = auditLogService;
        this.brandService = brandService;
        this.categoryService = categoryService;
    }

    public List<Product> listAll() {
        return repo.findAll();
    }

    public Optional<Product> getById(Long id) {
        return repo.findById(id);
    }

    public Product save(Product p) {
        boolean isNew = (p.getId() == null);
        Product saved = repo.save(p);

        if (isNew) {
        	Long idBrand = saved.getBrand().getId();
        	Brand brand = brandService.getById(idBrand).get();
        	Long idCategory = saved.getCategory().getId();
        	Category category = categoryService.getById(idCategory).get();

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
        Optional<Product> productOpt = repo.findById(id);
        if (productOpt.isEmpty()) {
            throw new EntityNotFoundException("Product with id " + id + " not found");
        }

        Product product = productOpt.get();
        try {
            repo.deleteById(id);

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
        repo.save(product);
    }

    public void decrementDepositsCount(Product product) {
        int current = product.getDepositsCount();
        if (current > 0) {
            product.setDepositsCount(current - 1);
            repo.save(product);
        }
    }
}