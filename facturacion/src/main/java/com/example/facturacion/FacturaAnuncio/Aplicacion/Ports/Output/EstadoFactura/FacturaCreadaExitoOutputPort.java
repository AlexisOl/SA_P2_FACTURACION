package com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output.EstadoFactura;


import com.example.comun.DTO.FacturaAnuncio.AnuncioCreadoDTO;

public interface FacturaCreadaExitoOutputPort {
    void crearFacturaAnuncio(AnuncioCreadoDTO facturaAnuncio);

}
