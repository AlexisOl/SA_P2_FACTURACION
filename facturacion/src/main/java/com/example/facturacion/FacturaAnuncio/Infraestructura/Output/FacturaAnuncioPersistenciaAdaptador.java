package com.example.facturacion.FacturaAnuncio.Infraestructura.Output;

import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output.CambioEstadoFacturasAnuncioOutputPort;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output.CambioMonetarioFacturaAnuncioOutputPort;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output.CrearFacturaAnuncioCineOutputPort;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output.CrearFacturaAnuncioOutputPort;
import com.example.facturacion.FacturaAnuncio.Dominio.EstadoFacturacion;
import com.example.facturacion.FacturaAnuncio.Dominio.FacturaAnuncio;
import com.example.facturacion.FacturaAnuncio.Infraestructura.Output.Entity.FacturaAnuncioEntity;
import com.example.facturacion.FacturaAnuncio.Infraestructura.Output.Mapper.FacturaAnuncioMapper;
import com.example.facturacion.FacturaAnuncio.Infraestructura.Output.Repository.FacturaAnuncioRepository;
import com.example.facturacion.FacturaBoleto.Infraestructura.Output.Entity.FacturaBoletoEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Component
@Transactional
@AllArgsConstructor
public class FacturaAnuncioPersistenciaAdaptador implements CrearFacturaAnuncioOutputPort, CrearFacturaAnuncioCineOutputPort,
        CambioEstadoFacturasAnuncioOutputPort , CambioMonetarioFacturaAnuncioOutputPort {
    private FacturaAnuncioRepository facturaAnuncioRepository;
    private FacturaAnuncioMapper facturaAnuncioMapper;



    @Override
    public FacturaAnuncio crearFacturaAnuncio(FacturaAnuncio facturaAnuncio) {
        FacturaAnuncioEntity prueba = this.facturaAnuncioMapper.toEntity(facturaAnuncio);
        System.out.println("Factura a guardar: " + prueba.getAnuncio() + ", " + prueba.getUsuario());

        return   this.facturaAnuncioMapper.toFacturaAnuncio(
                this.facturaAnuncioRepository.save(
                        this.facturaAnuncioMapper.toEntity(facturaAnuncio))
        );

    }

    @Override
    public FacturaAnuncio crearFacturaCineAnuncio(FacturaAnuncio facturaAnuncio) {
        FacturaAnuncioEntity prueba = this.facturaAnuncioMapper.toEntity(facturaAnuncio);
        System.out.println("Factura a guardar: " + prueba.getAnuncio() + ", " + prueba.getUsuario());

        return   this.facturaAnuncioMapper.toFacturaAnuncio(
                this.facturaAnuncioRepository.save(
                        this.facturaAnuncioMapper.toEntity(facturaAnuncio))
        );
    }

    @Override
    public void cambiarEstadoVenta(UUID id, EstadoFacturacion estadoVenta) {
        FacturaAnuncioEntity entidad = this.facturaAnuncioRepository.findById(id).orElse(null);
        if (entidad == null) return;
        entidad.setEstado(estadoVenta);
        this.facturaAnuncioRepository.save(entidad);
    }

    @Override
    public void cambiarCantidad(UUID id, Double cantidad) {
        FacturaAnuncioEntity entidad = this.facturaAnuncioRepository.findById(id).orElse(null);
        if (entidad == null) return;
        entidad.setMonto(cantidad);
        this.facturaAnuncioRepository.save(entidad);

    }
}
