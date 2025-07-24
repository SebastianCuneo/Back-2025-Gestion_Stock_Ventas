package uy.edu.ucu.inventario.repository;

import uy.edu.ucu.inventario.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {}