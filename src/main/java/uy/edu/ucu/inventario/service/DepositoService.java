package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Deposito;
import uy.edu.ucu.inventario.repository.DepositoRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de depósitos.
 */
@Service
public class DepositoService {

    private final DepositoRepository repo;

    public DepositoService(DepositoRepository repo) {
        this.repo = repo;
    }

    public List<Deposito> listar() {
        return repo.findAll();
    }

    public Optional<Deposito> obtener(Long id) {
        return repo.findById(id);
    }

    public Deposito guardar(Deposito d) {
        return repo.save(d);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}