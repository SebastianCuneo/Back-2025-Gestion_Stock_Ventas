package uy.edu.ucu.inventario.service;

import uy.edu.ucu.inventario.entity.Marca;
import uy.edu.ucu.inventario.repository.MarcaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MarcaService {
    private final MarcaRepository repo;
    public MarcaService(MarcaRepository repo) { this.repo = repo; }
    public List<Marca> listar() { return repo.findAll(); }
    public Optional<Marca> obtener(Long id) { return repo.findById(id); }
    public Marca guardar(Marca m) { return repo.save(m); }
    public void eliminar(Long id) { repo.deleteById(id); }
}