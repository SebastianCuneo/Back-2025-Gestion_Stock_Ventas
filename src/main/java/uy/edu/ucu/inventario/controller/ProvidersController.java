package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.Provider;
import uy.edu.ucu.inventario.service.ProviderService;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.*;

/**
 * REST Controller for the Provider entity.
 */
@RestController
@RequestMapping("/api/providers")
public class ProvidersController {

    private final ProviderService svc;

    public ProvidersController(ProviderService svc) {
        this.svc = svc;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> list() {
        List<Provider> providers = svc.listAll();
        List<Map<String, Object>> transformed = new ArrayList<>();

        for (Provider p : providers) {
            transformed.add(transformProvider(p));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformed);
        response.put("message", "Providers list retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
        return svc.getById(id)
                .map(provider -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformProvider(provider));
                    response.put("message", "Provider found.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Provider not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody Provider p) {
        Provider saved = svc.save(p);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", transformProvider(saved));
        response.put("message", "Provider created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody Provider p) {
        return svc.getById(id)
                .map(existing -> {
                    p.setId(id);
                    Provider updated = svc.save(p);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", transformProvider(updated));
                    response.put("message", "Provider updated successfully.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", false);
                    response.put("error", "Provider not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        if (!svc.getById(id).isPresent()) {
            response.put("success", false);
            response.put("error", "Provider not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            svc.delete(id);
            response.put("success", true);
            response.put("message", "Provider deleted successfully.");
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException ex) {
            response.put("success", false);
            response.put("error", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (DataIntegrityViolationException ex) {
            response.put("success", false);
            response.put("error", "Cannot delete provider due to data integrity constraints.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);

        } catch (Exception ex) {
            response.put("success", false);
            response.put("error", "Internal error: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private Map<String, Object> transformProvider(Provider p) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", p.getId());
        map.put("name", p.getName());
        map.put("email", p.getEmail());
        map.put("phone", p.getPhone());
        map.put("address", p.getAddress());
        map.put("associatedDate", p.getAssociatedDate());
        return map;
    }
}