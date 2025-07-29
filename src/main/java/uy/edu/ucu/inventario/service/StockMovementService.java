package uy.edu.ucu.inventario.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import uy.edu.ucu.inventario.entity.Product;
import uy.edu.ucu.inventario.entity.Stock;
import uy.edu.ucu.inventario.entity.StockMovement;
import uy.edu.ucu.inventario.enums.MovementType;
import uy.edu.ucu.inventario.repository.StockMovementRepository;
import uy.edu.ucu.inventario.repository.StockRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StockMovementService {

    private final StockMovementRepository repo;
    private final AuditLogService auditLogService;
    private final StockRepository stockRepo;
    private final ProductService productService;

    public StockMovementService(
            StockMovementRepository repo,
            AuditLogService auditLogService,
            StockRepository stockRepo,
            ProductService productService) {
        this.repo = repo;
        this.auditLogService = auditLogService;
        this.stockRepo = stockRepo;
        this.productService = productService;
    }

    public List<StockMovement> listAll() {
        return repo.findAll();
    }

    public Optional<StockMovement> getById(Long id) {
        return repo.findById(id);
    }

    public StockMovement save(StockMovement movement) {
        boolean isNew = (movement.getId() == null);
        StockMovement saved = repo.save(movement);

        Product product = movement.getProduct();

        switch (movement.getType()) {
            case ENTRY -> {
                boolean exists = stockRepo.existsByProductIdAndDepositId(
                        product.getId(), movement.getDestinationDeposit().getId());
                if (!exists) {
                    productService.incrementDepositsCount(product);
                }
            }
            case EXIT -> {
                Long depositId = movement.getOriginDeposit().getId();
                Optional<Stock> stockOpt = stockRepo.findByProductIdAndDepositId(product.getId(), depositId);
                if (stockOpt.isPresent() && stockOpt.get().getQuantity() == 0) {
                    productService.decrementDepositsCount(product);
                    stockRepo.deleteById(stockOpt.get().getId());
                }
            }
            case TRANSFER -> {
                Long originId = movement.getOriginDeposit().getId();
                Long destId = movement.getDestinationDeposit().getId();

                Optional<Stock> originStock = stockRepo.findByProductIdAndDepositId(product.getId(), originId);
                if (originStock.isPresent() && originStock.get().getQuantity() == 0) {
                    productService.decrementDepositsCount(product);
                    stockRepo.deleteById(originStock.get().getId());
                }

                boolean alreadyInDest = stockRepo.existsByProductIdAndDepositId(product.getId(), destId);
                if (!alreadyInDest) {
                    productService.incrementDepositsCount(product);
                }
            }
        }

        auditLogService.saveLog(
                "StockMovement",
                saved.getId(),
                isNew ? "CREATE" : "UPDATE",
                null
        );

        return saved;
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Stock movement with id " + id + " not found.");
        }

        repo.deleteById(id);

        auditLogService.saveLog(
                "StockMovement",
                id,
                "DELETE",
                null
        );
    }

    public List<StockMovement> findByType(MovementType type) {
        return repo.findByType(type);
    }

    public List<StockMovement> findByOriginDeposit(Long depositId) {
        return repo.findByOriginDepositId(depositId);
    }

    public List<StockMovement> findByDestinationDeposit(Long depositId) {
        return repo.findByDestinationDepositId(depositId);
    }

    public List<StockMovement> findTransfersBetweenDeposits(Long originId, Long destinationId) {
        return repo.findByOriginDepositIdAndDestinationDepositId(originId, destinationId);
    }
}