package com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output;

import com.example.facturacion.FacturaAnuncio.Dominio.FacturaAnuncio;

import java.util.UUID;

public interface ObtenerFacturaEspecificaOutputPort {
    FacturaAnuncio getFacturaAnuncioEspecifica(UUID idFactura);
}
