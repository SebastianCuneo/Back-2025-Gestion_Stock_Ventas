package uy.edu.ucu.inventario.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uy.edu.ucu.inventario.entity.StockMovement;
import uy.edu.ucu.inventario.enums.MovementType;
import uy.edu.ucu.inventario.service.StockMovementService;

import java.util.*;

@RestController
@RequestMapping("/api/stock-movements")
public class StockMovementController {

    private final StockMovementService service;

    public StockMovementController(StockMovementService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAll() {
        List<StockMovement> movements = service.listAll();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformList(movements));
        response.put("message", "Stock movements retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(movement -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformMovement(movement));
                    response.put("message", "Stock movement found.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Stock movement not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Map<String, Object>> getByType(@PathVariable MovementType type) {
        List<StockMovement> movements = service.findByType(type);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformList(movements));
        response.put("message", "Stock movements by type retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/origin/{depositId}")
    public ResponseEntity<Map<String, Object>> getByOriginDeposit(@PathVariable Long depositId) {
        List<StockMovement> movements = service.findByOriginDeposit(depositId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformList(movements));
        response.put("message", "Stock movements by origin deposit retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/destination/{depositId}")
    public ResponseEntity<Map<String, Object>> getByDestinationDeposit(@PathVariable Long depositId) {
        List<StockMovement> movements = service.findByDestinationDeposit(depositId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformList(movements));
        response.put("message", "Stock movements by destination deposit retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/transfer/{originId}/{destinationId}")
    public ResponseEntity<Map<String, Object>> getTransfersBetween(
            @PathVariable Long originId,
            @PathVariable Long destinationId
    ) {
        List<StockMovement> movements = service.findTransfersBetweenDeposits(originId, destinationId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformList(movements));
        response.put("message", "Transfers between deposits retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody StockMovement movement) {
        StockMovement saved = service.save(movement);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformMovement(saved));
        response.put("message", "Stock movement created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody StockMovement updated) {
        return service.getById(id)
                .map(existing -> {
                    updated.setId(id);
                    StockMovement saved = service.save(updated);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformMovement(saved));
                    response.put("message", "Stock movement updated successfully.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Stock movement not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        service.delete(id);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Stock movement deleted successfully.");
        return ResponseEntity.ok(response);
    }

    // === Helpers ===

    private List<Map<String, Object>> transformList(List<StockMovement> list) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (StockMovement m : list) {
            result.add(transformMovement(m));
        }
        return result;
    }

    private Map<String, Object> transformMovement(StockMovement m) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", m.getId());
        map.put("productId", m.getProduct().getId());
        map.put("quantity", m.getQuantity());
        map.put("type", m.getType());
        map.put("createdAt", m.getDate());

        if (m.getOriginDeposit() != null) {
            Map<String, Object> origin = new HashMap<>();
            origin.put("id", m.getOriginDeposit().getId());
            origin.put("name", m.getOriginDeposit().getName());
            map.put("originDeposit", origin);
        }

        if (m.getDestinationDeposit() != null) {
            Map<String, Object> destination = new HashMap<>();
            destination.put("id", m.getDestinationDeposit().getId());
            destination.put("name", m.getDestinationDeposit().getName());
            map.put("destinationDeposit", destination);
        }

        return map;
    }
}