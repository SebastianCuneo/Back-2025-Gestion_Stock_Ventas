	package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.User;
import uy.edu.ucu.inventario.repository.UserRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing users.
 */
@Service
public class UserService {

    private final UserRepository repo;
    private final AuditLogService auditLogService;

    public UserService(UserRepository repo, AuditLogService auditLogService) {
        this.repo = repo;
        this.auditLogService = auditLogService;
    }

    public List<User> listAll() {
        return repo.findAll();
    }

    public Optional<User> getById(Long id) {
        return repo.findById(id);
    }

    public User save(User user) {
        boolean isNew = (user.getId() == null);
        User saved = repo.save(user);

        auditLogService.saveLog(
            "User",
            saved.getId(),
            isNew ? "CREATE" : "UPDATE",
            null
        );

        return saved;
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }

        repo.deleteById(id);

        auditLogService.saveLog(
            "User",
            id,
            "DELETE",
            null
        );
    }
}