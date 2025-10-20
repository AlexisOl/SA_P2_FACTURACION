package com.example.facturacion.FacturaAnuncio.Aplicacion.CasosUso.CrearFacturaAnuncio;


import com.example.comun.DTO.FacturaAnuncio.AnuncioCreadoDTO;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Input.CrearFacturaAnuncioInputPort;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output.CrearFacturaAnuncioOutputPort;
import com.example.facturacion.FacturaAnuncio.Dominio.EstadoFacturacion;
import com.example.facturacion.FacturaAnuncio.Dominio.FacturaAnuncio;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class CrearFacturaAnuncioService implements CrearFacturaAnuncioInputPort {

    private CrearFacturaAnuncioOutputPort crearFacturaAnuncioOutputPort;



    public  CrearFacturaAnuncioService(CrearFacturaAnuncioOutputPort crearFacturaAnuncioOutputPort){
        this.crearFacturaAnuncioOutputPort = crearFacturaAnuncioOutputPort;
    }



    @Override
    public FacturaAnuncio crearFacturaAnuncio(AnuncioCreadoDTO facturaAnuncio) {


        if (facturaAnuncio.getAnuncioId() == null) {
            throw  new IllegalArgumentException("El id del anuncio no puede ser null");
        }

        System.out.println(facturaAnuncio.getAnuncioId()+ "aca id");

        // el admin verifica correctamente el anuncio
       return this.crearFacturaAnuncioOutputPort.crearFacturaAnuncio(
                new FacturaAnuncio(
                        UUID.randomUUID(),
                        facturaAnuncio.getAnuncioId(),
                        facturaAnuncio.getUsuarioId(),
                        facturaAnuncio.getCosto(),
                        LocalDate.now(),
                        EstadoFacturacion.PENDIENTE
                ));
    }
}
