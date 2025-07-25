package uy.edu.ucu.inventario.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uy.edu.ucu.inventario.entity.StockMovement;
import uy.edu.ucu.inventario.enums.MovementType;
import uy.edu.ucu.inventario.service.StockMovementService;

import java.util.List;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    private final StockMovementService service;

    public StockMovementController(StockMovementService service) {
        this.service = service;
    }

    // === GET: All movements ===
    @GetMapping
    public ResponseEntity<List<StockMovement>> getAll() {
        return ResponseEntity.ok(service.listAll());
    }

    // === GET: By ID ===
    @GetMapping("/{id}")
    public ResponseEntity<StockMovement> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // === GET: By type ===
    @GetMapping("/type/{type}")
    public ResponseEntity<List<StockMovement>> getByType(@PathVariable MovementType type) {
        return ResponseEntity.ok(service.findByType(type));
    }

    // === GET: By origin deposit ===
    @GetMapping("/origin/{depositId}")
    public ResponseEntity<List<StockMovement>> getByOriginDeposit(@PathVariable Long depositId) {
        return ResponseEntity.ok(service.findByOriginDeposit(depositId));
    }

    // === GET: By destination deposit ===
    @GetMapping("/destination/{depositId}")
    public ResponseEntity<List<StockMovement>> getByDestinationDeposit(@PathVariable Long depositId) {
        return ResponseEntity.ok(service.findByDestinationDeposit(depositId));
    }

    // === GET: By origin and destination ===
    @GetMapping("/transfer/{originId}/{destinationId}")
    public ResponseEntity<List<StockMovement>> getTransfersBetween(
            @PathVariable Long originId,
            @PathVariable Long destinationId
    ) {
        return ResponseEntity.ok(service.findTransfersBetweenDeposits(originId, destinationId));
    }

    // === POST: Create ===
    @PostMapping
    public ResponseEntity<StockMovement> create(@RequestBody StockMovement movement) {
        return ResponseEntity.ok(service.save(movement));
    }

    // === PUT: Update ===
    @PutMapping("/{id}")
    public ResponseEntity<StockMovement> update(@PathVariable Long id, @RequestBody StockMovement updated) {
        return service.getById(id)
                .map(existing -> {
                    updated.setId(id);
                    return ResponseEntity.ok(service.save(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // === DELETE: Delete ===
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
