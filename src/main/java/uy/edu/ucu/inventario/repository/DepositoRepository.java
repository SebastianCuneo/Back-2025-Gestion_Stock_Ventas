package uy.edu.ucu.inventario.repository;

import uy.edu.ucu.inventario.entity.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositoRepository extends JpaRepository<Deposito, Long> {}