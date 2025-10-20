package com.example.facturacion.FacturaAnuncio.Infraestructura.Output;

import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output.CrearFacturaAnuncioOutputPort;
import com.example.facturacion.FacturaAnuncio.Dominio.FacturaAnuncio;
import com.example.facturacion.FacturaAnuncio.Infraestructura.Output.Entity.FacturaAnuncioEntity;
import com.example.facturacion.FacturaAnuncio.Infraestructura.Output.Mapper.FacturaAnuncioMapper;
import com.example.facturacion.FacturaAnuncio.Infraestructura.Output.Repository.FacturaAnuncioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
@AllArgsConstructor
public class FacturaAnuncioPersistenciaAdaptador implements CrearFacturaAnuncioOutputPort {
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
}
