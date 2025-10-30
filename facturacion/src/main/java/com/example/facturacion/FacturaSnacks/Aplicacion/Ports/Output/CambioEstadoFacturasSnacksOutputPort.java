package com.example.facturacion.FacturaSnacks.Aplicacion.Ports.Output;

import com.example.facturacion.FacturaBoleto.Dominio.EstadoFacturacionBoleto;
import com.example.facturacion.FacturaSnacks.Dominio.FacturaSnacks;

import java.util.UUID;

public interface CambioEstadoFacturasSnacksOutputPort {
    void cambiarEstadoVenta(UUID id, EstadoFacturacionBoleto estadoVenta);

}
