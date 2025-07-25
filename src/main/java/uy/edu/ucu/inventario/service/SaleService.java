package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Sale;
import uy.edu.ucu.inventario.repository.SaleRepository;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing sales.
 */
@Service
public class SaleService {

    private final SaleRepository repo;

    public SaleService(SaleRepository repo) {
        this.repo = repo;
    }

    public List<Sale> listAll() {
        return repo.findAll();
    }

    public Optional<Sale> getById(Long id) {
        return repo.findById(id);
    }

    public Sale save(Sale sale) {
        return repo.save(sale);
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Sale with id " + id + " not found");
        }
        repo.deleteById(id);
    }
}
