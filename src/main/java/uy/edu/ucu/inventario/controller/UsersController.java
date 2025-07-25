package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.User;
import uy.edu.ucu.inventario.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;

/**
 * REST Controller for user management.
 */
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UserService svc;

    public UsersController(UserService svc) {
        this.svc = svc;
    }

    // Get all users
    @GetMapping
    public List<User> list() {
        return svc.listAll();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        return svc.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create new user
    @PostMapping
    public User create(@RequestBody User user) {
        return svc.save(user);
    }

    // Update existing user
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
        return svc.getById(id)
                .map(existing -> {
                    user.setId(id);
                    return ResponseEntity.ok(svc.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!svc.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            svc.delete(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalStateException ex) {
            return ResponseEntity
                   .badRequest()
                   .body(ex.getMessage());

        } catch (EntityNotFoundException ex) {
            return ResponseEntity
                   .status(HttpStatus.NOT_FOUND)
                   .body(ex.getMessage());

        } catch (Exception ex) {
            return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Internal error: " + ex.getMessage());
        }
    }
}