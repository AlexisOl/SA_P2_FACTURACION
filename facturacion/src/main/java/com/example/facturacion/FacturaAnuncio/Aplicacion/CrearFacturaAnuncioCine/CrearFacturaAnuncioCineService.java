package com.example.facturacion.FacturaAnuncio.Aplicacion.CrearFacturaAnuncioCine;


import com.example.comun.DTO.FacturaAnuncio.AnuncioCreadoDTO;
import com.example.comun.DTO.FacturaAnuncio.RespuestaAnuncioCreadoCineDTO;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Input.CrearFacturaAnuncioCineInputPort;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Input.CrearFacturaAnuncioInputPort;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output.CrearFacturaAnuncioCineOutputPort;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output.CrearFacturaAnuncioOutputPort;
import com.example.facturacion.FacturaAnuncio.Dominio.EstadoFacturacion;
import com.example.facturacion.FacturaAnuncio.Dominio.FacturaAnuncio;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class CrearFacturaAnuncioCineService implements CrearFacturaAnuncioCineInputPort {

    private final CrearFacturaAnuncioCineOutputPort crearFacturaAnuncioOutputPort;



    public CrearFacturaAnuncioCineService(CrearFacturaAnuncioCineOutputPort crearFacturaAnuncioOutputPort){
        this.crearFacturaAnuncioOutputPort = crearFacturaAnuncioOutputPort;
    }



    @Override
    public FacturaAnuncio crearFacturaCineAnuncio(RespuestaAnuncioCreadoCineDTO facturaAnuncio) {


        if (facturaAnuncio.getAnuncioId() == null) {
            throw  new IllegalArgumentException("El id del anuncio no puede ser null");
        }

        System.out.println(facturaAnuncio.getAnuncioId()+ "aca id");

        //
        // aca factura solo al usuario
       return this.crearFacturaAnuncioOutputPort.crearFacturaCineAnuncio(
                new FacturaAnuncio(
                        UUID.randomUUID(),
                        facturaAnuncio.getAnuncioId(),
                     //   facturaAnuncio.getUsuarioId(),
                        facturaAnuncio.getCosto(),
                        LocalDate.now(),
                        EstadoFacturacion.PENDIENTE,
                        "Ingreso",
                        facturaAnuncio.getCineId()

                ));
    }
}
