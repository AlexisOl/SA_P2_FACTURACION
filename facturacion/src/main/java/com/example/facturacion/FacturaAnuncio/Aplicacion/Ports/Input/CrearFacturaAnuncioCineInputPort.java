package com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Input;

import com.example.comun.DTO.FacturaAnuncio.AnuncioCreadoDTO;
import com.example.comun.DTO.FacturaAnuncio.RespuestaAnuncioCreadoCineDTO;
import com.example.facturacion.FacturaAnuncio.Dominio.FacturaAnuncio;

public interface CrearFacturaAnuncioCineInputPort {
    FacturaAnuncio crearFacturaCineAnuncio(RespuestaAnuncioCreadoCineDTO facturaAnuncio);
}
