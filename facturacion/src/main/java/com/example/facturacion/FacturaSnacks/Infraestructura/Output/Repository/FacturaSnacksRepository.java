package com.example.facturacion.FacturaSnacks.Infraestructura.Output.Repository;

import com.example.facturacion.FacturaBoleto.Infraestructura.Output.Entity.FacturaBoletoEntity;
import com.example.facturacion.FacturaSnacks.Infraestructura.Output.Entity.FacturaSnackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FacturaSnacksRepository extends JpaRepository<FacturaSnackEntity, UUID> {
}
