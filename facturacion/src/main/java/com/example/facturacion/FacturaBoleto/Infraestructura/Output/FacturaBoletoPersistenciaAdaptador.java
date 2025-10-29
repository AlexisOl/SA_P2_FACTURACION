package com.example.facturacion.FacturaBoleto.Infraestructura.Output;


import com.example.facturacion.FacturaBoleto.Aplicacion.Ports.output.CambioEstadoFacturasOutputPort;
import com.example.facturacion.FacturaBoleto.Aplicacion.Ports.output.CrearFacturaBoletoOutputPort;
import com.example.facturacion.FacturaBoleto.Dominio.EstadoFacturacionBoleto;
import com.example.facturacion.FacturaBoleto.Dominio.FacturaBoleto;
import com.example.facturacion.FacturaBoleto.Infraestructura.Output.Entity.FacturaBoletoEntity;
import com.example.facturacion.FacturaBoleto.Infraestructura.Output.Mapper.FacturaBoletoMapper;
import com.example.facturacion.FacturaBoleto.Infraestructura.Output.Repository.FacturaBoletoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class FacturaBoletoPersistenciaAdaptador implements CrearFacturaBoletoOutputPort,
        CambioEstadoFacturasOutputPort {


    private final FacturaBoletoRepository facturaBoletoRepository;
    private final FacturaBoletoMapper facturaBoletoMapper;


    @Override
    public FacturaBoleto crearFacturaBoleto(FacturaBoleto facturaBoleto) {
        return this.facturaBoletoMapper.toFacturaBoleto(
                this.facturaBoletoRepository.save(this.facturaBoletoMapper.toEntity(facturaBoleto))
        );
    }

    @Override
    public void cambiarEstadoVenta(UUID id, EstadoFacturacionBoleto estadoVenta) {
        FacturaBoletoEntity entidad = this.facturaBoletoRepository.findById(id).orElse(null);
        if (entidad == null) return;
        entidad.setEstado(estadoVenta);
        this.facturaBoletoRepository.save(entidad);
    }
}
