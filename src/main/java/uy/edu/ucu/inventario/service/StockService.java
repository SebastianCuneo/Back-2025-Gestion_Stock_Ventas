package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Product;
import uy.edu.ucu.inventario.entity.Stock;
import uy.edu.ucu.inventario.repository.StockRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing product stock in deposits.
 */
@Service
public class StockService {

    private final StockRepository repo;
    private final AuditLogService auditLogService;
    private final ProductService productService;

    public StockService(StockRepository repo, AuditLogService auditLogService, ProductService productService) {
        this.repo = repo;
        this.auditLogService = auditLogService;
        this.productService = productService;
    }

    public List<Stock> listAll() {
        return repo.findAll();
    }

    public Optional<Stock> getById(Long id) {
        return repo.findById(id);
    }

    public Stock save(Stock s) {
        boolean isNew = (s.getId() == null);

        if (isNew) {
            boolean existsInDeposit = repo.existsByProductIdAndDepositId(
                s.getProduct().getId(), s.getDeposit().getId()
            );

            if (!existsInDeposit) {
                productService.incrementDepositsCount(s.getProduct());
            }
        }

        Stock saved = repo.save(s);

        auditLogService.saveLog(
            "Stock",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            null
        );

        return saved;
    }

    public void delete(Long id) {
        Optional<Stock> stockOpt = repo.findById(id);

        if (stockOpt.isEmpty()) {
            throw new EntityNotFoundException("Stock with id " + id + " not found");
        }

        Stock stock = stockOpt.get();

        try {
            repo.deleteById(id);

            productService.decrementDepositsCount(stock.getProduct());

            auditLogService.saveLog(
                "Stock",
                id,
                "DELETE",
                null
            );

        } catch (DataIntegrityViolationException ex) {
            throw new IllegalStateException(
                "Cannot delete stock because it is referenced by other records", ex
            );
        }
    }
}