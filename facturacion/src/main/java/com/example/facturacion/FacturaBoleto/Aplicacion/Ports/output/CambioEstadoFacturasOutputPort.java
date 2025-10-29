package com.example.facturacion.FacturaBoleto.Aplicacion.Ports.output;

import com.example.facturacion.FacturaBoleto.Dominio.EstadoFacturacionBoleto;

import java.util.UUID;

public interface CambioEstadoFacturasOutputPort {
    void cambiarEstadoVenta(UUID id, EstadoFacturacionBoleto estadoVenta);

}
