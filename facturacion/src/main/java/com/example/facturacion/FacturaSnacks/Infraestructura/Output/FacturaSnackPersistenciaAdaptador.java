package com.example.facturacion.FacturaSnacks.Infraestructura.Output;


import com.example.facturacion.FacturaBoleto.Dominio.EstadoFacturacionBoleto;
import com.example.facturacion.FacturaBoleto.Infraestructura.Output.Entity.FacturaBoletoEntity;
import com.example.facturacion.FacturaSnacks.Aplicacion.Ports.Output.CambioEstadoFacturasSnacksOutputPort;
import com.example.facturacion.FacturaSnacks.Aplicacion.Ports.Output.CrearFacturaSnacksOutputPort;
import com.example.facturacion.FacturaSnacks.Dominio.FacturaSnacks;
import com.example.facturacion.FacturaSnacks.Infraestructura.Output.Entity.FacturaSnackEntity;
import com.example.facturacion.FacturaSnacks.Infraestructura.Output.Mapper.FacturaSnacksMapper;
import com.example.facturacion.FacturaSnacks.Infraestructura.Output.Repository.FacturaSnacksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@AllArgsConstructor
public class FacturaSnackPersistenciaAdaptador implements CrearFacturaSnacksOutputPort, CambioEstadoFacturasSnacksOutputPort {
    private final FacturaSnacksMapper  facturaSnacksMapper;
    private final FacturaSnacksRepository facturaSnacksRepository;

    @Override
    @Transactional
    public FacturaSnacks crearFacturaSnacks(FacturaSnacks facturaSnacks) {
        return this.facturaSnacksMapper.toFacturaSnacks(
                this.facturaSnacksRepository.save(
                        this.facturaSnacksMapper.toFacturaSnackEntity(facturaSnacks)
                )
        );
    }

    @Override
    @Transactional
    public void cambiarEstadoVenta(UUID id, EstadoFacturacionBoleto estadoVenta) {
        FacturaSnackEntity entidad = this.facturaSnacksRepository.findById(id).orElse(null);
        if (entidad == null) return;
        entidad.setEstado(estadoVenta);
        this.facturaSnacksRepository.save(entidad);
    }
}
