package uy.edu.ucu.inventario.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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

    private final StockMovementRepository stockMovementRepository;
    private final AuditLogService auditLogService;
    private final StockRepository stockRepository;
    private final ProductService productService;

    public StockMovementService(
            StockMovementRepository stockMovementRepository,
            AuditLogService auditLogService,
            StockRepository stockRepository,
            ProductService productService) {
        this.stockMovementRepository = stockMovementRepository;
        this.auditLogService = auditLogService;
        this.stockRepository = stockRepository;
        this.productService = productService;
    }

    public List<StockMovement> listAll() {
        return stockMovementRepository.findAll();
    }

    public Optional<StockMovement> getById(Long id) {
        return stockMovementRepository.findById(id);
    }

    public StockMovement save(StockMovement movement) {
        boolean isNew = (movement.getId() == null);
        StockMovement saved = stockMovementRepository.save(movement);

        Product product = movement.getProduct();

        switch (movement.getType()) {
            case ENTRY -> {
                boolean exists = stockRepository.existsByProductIdAndDepositId(
                        product.getId(), movement.getDestinationDeposit().getId());
                if (!exists) {
                    productService.incrementDepositsCount(product);
                }
            }
            case EXIT -> {
                Long depositId = movement.getOriginDeposit().getId();
                Optional<Stock> stockOpt = stockRepository.findByProductIdAndDepositId(product.getId(), depositId);
                if (stockOpt.isPresent() && stockOpt.get().getQuantity() == 0) {
                    productService.decrementDepositsCount(product);
                    stockRepository.deleteById(stockOpt.get().getId());
                }
            }
            case TRANSFER -> {
                Long originId = movement.getOriginDeposit().getId();
                Long destinationId = movement.getDestinationDeposit().getId();

                Optional<Stock> originStock = stockRepository.findByProductIdAndDepositId(product.getId(), originId);
                if (originStock.isPresent() && originStock.get().getQuantity() == 0) {
                    productService.decrementDepositsCount(product);
                    stockRepository.deleteById(originStock.get().getId());
                }

                boolean alreadyInDestination = stockRepository.existsByProductIdAndDepositId(product.getId(), destinationId);
                if (!alreadyInDestination) {
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
        if (!stockMovementRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Stock movement with id " + id + " not found.");
        }

        stockMovementRepository.deleteById(id);

        auditLogService.saveLog(
                "StockMovement",
                id,
                "DELETE",
                null
        );
    }

    public List<StockMovement> findByType(MovementType type) {
        return stockMovementRepository.findByType(type);
    }

    public List<StockMovement> findByOriginDeposit(Long depositId) {
        return stockMovementRepository.findByOriginDepositId(depositId);
    }

    public List<StockMovement> findByDestinationDeposit(Long depositId) {
        return stockMovementRepository.findByDestinationDepositId(depositId);
    }

    public List<StockMovement> findTransfersBetweenDeposits(Long originId, Long destinationId) {
        return stockMovementRepository.findByOriginDepositIdAndDestinationDepositId(originId, destinationId);
    }
}
