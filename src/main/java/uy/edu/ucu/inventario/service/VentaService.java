package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Venta;
import uy.edu.ucu.inventario.repository.VentaRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de ventas.
 */
@Service
public class VentaService {

    private final VentaRepository repo;

    public VentaService(VentaRepository repo) {
        this.repo = repo;
    }

    public List<Venta> listar() {
        return repo.findAll();
    }

    public Optional<Venta> obtener(Long id) {
        return repo.findById(id);
    }

    public Venta guardar(Venta v) {
        return repo.save(v);
    }

    public void eliminar(Long id) {
        repo.deleteById(id);
    }
}