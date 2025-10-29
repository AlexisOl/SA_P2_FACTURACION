package com.example.facturacion.FacturaBoleto.Aplicacion.Ports.output;

import com.example.comun.DTO.FacturaBoleto.FacturaBoletoCreadoDTO;
import com.example.facturacion.FacturaBoleto.Dominio.FacturaBoleto;

public interface CrearFacturaBoletoOutputPort {
    FacturaBoleto crearFacturaBoleto(FacturaBoleto facturaBoleto);

}
