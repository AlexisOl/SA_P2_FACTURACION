package com.example.facturacion.FacturaSnacks.Aplicacion.CasoUso.CrearFacturaSnack;

import com.example.comun.DTO.FacturaBoleto.DetalleSnackDTO;
import com.example.comun.DTO.FacturaBoleto.FacturaSnackCreadaDTO;
import com.example.facturacion.FacturaBoleto.Dominio.EstadoFacturacionBoleto;
import com.example.facturacion.FacturaSnacks.Aplicacion.Ports.Input.CrearFacturaSnacksInputPort;
import com.example.facturacion.FacturaSnacks.Aplicacion.Ports.Output.CrearFacturaSnacksOutputPort;
import com.example.facturacion.FacturaSnacks.Dominio.FacturaSnacks;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CrearFacturaSnacksServicio implements CrearFacturaSnacksInputPort  {

    private final CrearFacturaSnacksOutputPort crearFacturaSnacksOutputPort;


    public CrearFacturaSnacksServicio(CrearFacturaSnacksOutputPort crearFacturaSnacksOutputPort){
        this.crearFacturaSnacksOutputPort = crearFacturaSnacksOutputPort;
    }
    @Override
    public FacturaSnacks crearFacturaSnacks(FacturaSnackCreadaDTO facturaSnacks) {

        if (facturaSnacks.getVentaSnackId() == null) {
            throw  new IllegalArgumentException("El venta no puede ser nulo");
        }

        Double monto = facturaSnacks.getDetalleSnacks().stream()
                .mapToDouble(DetalleSnackDTO::getSubtotal)
                .sum();


       return this.crearFacturaSnacksOutputPort.crearFacturaSnacks(
                new FacturaSnacks(
                        UUID.randomUUID(),
                        facturaSnacks.getVentaId(),
                        facturaSnacks.getUsuarioId(),
                        facturaSnacks.getIdCine(),
                        facturaSnacks.getVentaSnackId(),
                        facturaSnacks.getDetalleSnacks().get(0).getFechaVenta(),
                        monto,
                        EstadoFacturacionBoleto.valueOf("PENDIENTE")
                )
        );
    }
}
