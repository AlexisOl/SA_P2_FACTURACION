package com.example.facturacion.FacturaBoleto.Infraestructura.Output.Repository;

import com.example.facturacion.FacturaBoleto.Infraestructura.Output.Entity.FacturaBoletoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FacturaBoletoRepository extends JpaRepository<FacturaBoletoEntity, UUID> {
}
