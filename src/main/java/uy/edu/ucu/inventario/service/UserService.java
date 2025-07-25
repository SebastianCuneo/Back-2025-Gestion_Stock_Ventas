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

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> listAll() {
        return repo.findAll();
    }

    public Optional<User> getById(Long id) {
        return repo.findById(id);
    }

    public User save(User user) {
        return repo.save(user);
    }

    public void delete(Long id) {
        // Check if the user exists
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        repo.deleteById(id);
    }
}
