package com.example.facturacion.FacturaSnacks.Aplicacion.Ports.Input;

import com.example.comun.DTO.FacturaBoleto.FacturaSnackCreadaDTO;
import com.example.facturacion.FacturaSnacks.Dominio.FacturaSnacks;

public interface CrearFacturaSnacksInputPort {

    FacturaSnacks crearFacturaSnacks(FacturaSnackCreadaDTO facturaSnacks);
}
