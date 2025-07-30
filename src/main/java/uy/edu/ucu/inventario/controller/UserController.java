package uy.edu.ucu.inventario.controller;

import uy.edu.ucu.inventario.entity.User;
import uy.edu.ucu.inventario.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for user management.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Get all users
    @GetMapping
    public ResponseEntity<Map<String, Object>> list() {
        List<User> users = userService.listAll();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", users);
        response.put("message", "User list retrieved successfully.");
        return ResponseEntity.ok(response);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
        return userService.getById(id)
                .map(user -> {
                    Map<String, Object> response = new HashMap<>();
                    Map<String, Object> userData = new HashMap<>();
                    userData.put("id", user.getId());
                    userData.put("firstName", user.getFirstName());
                    userData.put("lastName", user.getLastName());
                    userData.put("email", user.getEmail());
                    userData.put("phone", user.getPhone());

                    response.put("success", true);
                    response.put("data", userData);
                    response.put("message", "User found.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("success", false);
                    error.put("error", "User not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    // Create new user
    @PostMapping
    public ResponseEntity<Map<String, Object>> create(@RequestBody User user) {
        User saved = userService.save(user);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", saved);
        response.put("message", "User created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Update existing user
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody User user) {
        return userService.getById(id)
                .map(existing -> {
                    user.setId(id);
                    User updated = userService.save(user);
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("data", updated);
                    response.put("message", "User updated successfully.");
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> {
                    Map<String, Object> error = new HashMap<>();
                    error.put("success", false);
                    error.put("error", "User not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                });
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!userService.getById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();

        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error: " + ex.getMessage());
        }
    }
}