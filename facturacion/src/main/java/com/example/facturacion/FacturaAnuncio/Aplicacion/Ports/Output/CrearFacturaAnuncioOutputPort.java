package com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output;

import com.example.facturacion.FacturaAnuncio.Dominio.FacturaAnuncio;

public interface CrearFacturaAnuncioOutputPort {
    FacturaAnuncio crearFacturaAnuncio(FacturaAnuncio facturaAnuncio);

}
