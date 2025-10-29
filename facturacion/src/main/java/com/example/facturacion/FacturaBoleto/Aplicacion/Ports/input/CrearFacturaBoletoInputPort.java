package com.example.facturacion.FacturaBoleto.Aplicacion.Ports.input;

import com.example.comun.DTO.FacturaBoleto.FacturaBoletoCreadoDTO;
import com.example.facturacion.FacturaBoleto.Dominio.FacturaBoleto;

public interface CrearFacturaBoletoInputPort {
    FacturaBoleto crearFacturaBoleto(FacturaBoletoCreadoDTO facturaBoletoCreadoDTO);

}
