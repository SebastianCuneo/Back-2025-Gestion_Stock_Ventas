package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Provider;
import uy.edu.ucu.inventario.repository.ProviderRepository;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Service for managing providers.
 */
@Service
public class ProviderService {

    private final ProviderRepository repo;

    public ProviderService(ProviderRepository repo) {
        this.repo = repo;
    }

    public List<Provider> listAll() {
        return repo.findAll();
    }

    public Optional<Provider> getById(Long id) {
        return repo.findById(id);
    }

    public Provider save(Provider p) {
        return repo.save(p);
    }

    public void delete(Long id){
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Provider with id " + id + " not found");
        }
        repo.deleteById(id);
    }
}
