package com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output;

import com.example.facturacion.FacturaAnuncio.Dominio.EstadoFacturacion;
import com.example.facturacion.FacturaBoleto.Dominio.EstadoFacturacionBoleto;

import java.util.UUID;

public interface CambioEstadoFacturasAnuncioOutputPort {
    void cambiarEstadoVenta(UUID id, EstadoFacturacion estadoVenta);

}
