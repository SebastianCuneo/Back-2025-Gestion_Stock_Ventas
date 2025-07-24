package uy.edu.ucu.inventario.repository;

import uy.edu.ucu.inventario.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {}
