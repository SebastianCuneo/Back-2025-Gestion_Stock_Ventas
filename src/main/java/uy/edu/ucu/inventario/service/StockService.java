package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Stock;
import uy.edu.ucu.inventario.repository.StockRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión del stock de productos en depósitos.
 */
@Service
public class StockService {

    private final StockRepository repo;

    public StockService(StockRepository repo) {
        this.repo = repo;
    }

    public List<Stock> listar() {
        return repo.findAll();
    }

    public Optional<Stock> obtener(Long id) {
        return repo.findById(id);
    }

    public Stock guardar(Stock s) {
        return repo.save(s);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}