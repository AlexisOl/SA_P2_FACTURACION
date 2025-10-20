package com.example.facturacion.FacturaAnuncio.Infraestructura.Output.Repository;

import com.example.facturacion.FacturaAnuncio.Infraestructura.Output.Entity.FacturaAnuncioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FacturaAnuncioRepository extends JpaRepository<FacturaAnuncioEntity, UUID> {
}
