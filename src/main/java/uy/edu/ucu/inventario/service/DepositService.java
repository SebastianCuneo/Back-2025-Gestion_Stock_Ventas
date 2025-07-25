package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Deposit;
import uy.edu.ucu.inventario.repository.DepositRepository;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing deposits.
 */
@Service
public class DepositService {

    private final DepositRepository repo;

    public DepositService(DepositRepository repo) {
        this.repo = repo;
    }

    public List<Deposit> listAll() {
        return repo.findAll();
    }

    public Optional<Deposit> getById(Long id) {
        return repo.findById(id);
    }

    public Deposit save(Deposit d) {
        return repo.save(d);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Deposit with id " + id + " not found");
        }
        repo.deleteById(id);
    }
}
