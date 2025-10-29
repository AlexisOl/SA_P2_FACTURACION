package com.example.facturacion.FacturaBoleto.Aplicacion.CasoUso.CrearFactura;


import com.example.comun.DTO.FacturaBoleto.FacturaBoletoCreadoDTO;
import com.example.facturacion.FacturaBoleto.Aplicacion.Ports.input.CrearFacturaBoletoInputPort;
import com.example.facturacion.FacturaBoleto.Aplicacion.Ports.output.CrearFacturaBoletoOutputPort;
import com.example.facturacion.FacturaBoleto.Dominio.EstadoFacturacionBoleto;
import com.example.facturacion.FacturaBoleto.Dominio.FacturaBoleto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CrearFacturaBoletoService implements CrearFacturaBoletoInputPort {

    private final CrearFacturaBoletoOutputPort crearFacturaBoletoOutputPort;

    public CrearFacturaBoletoService(CrearFacturaBoletoOutputPort crearFacturaBoletoOutputPort) {
        this.crearFacturaBoletoOutputPort = crearFacturaBoletoOutputPort;
    }


    @Override
    public FacturaBoleto crearFacturaBoleto(FacturaBoletoCreadoDTO facturaBoletoCreadoDTO) {
        if (facturaBoletoCreadoDTO.getFechaVenta() == null) {
            throw  new IllegalArgumentException("El id de la venta no puede ser nula");
        }

        return this.crearFacturaBoletoOutputPort.crearFacturaBoleto
                (new FacturaBoleto(
                UUID.randomUUID(),
                facturaBoletoCreadoDTO.getVentaId(),
                facturaBoletoCreadoDTO.getUsuarioId(),
                facturaBoletoCreadoDTO.getFechaVenta(),
                facturaBoletoCreadoDTO.getMontoTotal(),
                EstadoFacturacionBoleto.valueOf("PENDIENTE")

                ));
    }
}
