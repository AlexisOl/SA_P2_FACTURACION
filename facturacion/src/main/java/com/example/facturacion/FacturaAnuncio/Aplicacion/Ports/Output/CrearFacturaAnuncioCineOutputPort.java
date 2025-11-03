package com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output;

import com.example.comun.DTO.FacturaAnuncio.AnuncioCreadoDTO;
import com.example.facturacion.FacturaAnuncio.Dominio.FacturaAnuncio;

public interface CrearFacturaAnuncioCineOutputPort {
    FacturaAnuncio crearFacturaCineAnuncio(FacturaAnuncio facturaAnuncio);

}
