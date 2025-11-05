package com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output;

import com.example.facturacion.FacturaAnuncio.Dominio.EstadoFacturacion;

import java.util.UUID;

public interface CambioMonetarioFacturaAnuncioOutputPort {
    void cambiarCantidad(UUID id, Double cantidad);

}
