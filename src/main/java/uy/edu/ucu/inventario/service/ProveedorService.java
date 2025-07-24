package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Proveedor;
import uy.edu.ucu.inventario.repository.ProveedorRepository;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de proveedores.
 */
@Service
public class ProveedorService {

    private final ProveedorRepository repo;

    public ProveedorService(ProveedorRepository repo) {
        this.repo = repo;
    }

    public List<Proveedor> listar() {
        return repo.findAll();
    }

    public Optional<Proveedor> obtener(Long id) {
        return repo.findById(id);
    }

    public Proveedor guardar(Proveedor p) {
        return repo.save(p);
    }

    public void eliminar(Long id){
        //Verificar que el proveedor existe
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Proveedor con id " + id + " no encontrado");
        }
    }
}