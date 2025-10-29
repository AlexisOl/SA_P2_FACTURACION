package com.example.facturacion.FacturaAnuncio.Infraestructura.Output.Mapper;

import com.example.facturacion.FacturaAnuncio.Dominio.FacturaAnuncio;
import com.example.facturacion.FacturaAnuncio.Infraestructura.Output.Entity.FacturaAnuncioEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FacturaAnuncioMapper {

    FacturaAnuncioEntity toEntity(FacturaAnuncio facturaAnuncio);
    FacturaAnuncio toFacturaAnuncio(FacturaAnuncioEntity facturaAnuncioEntity);

    List<FacturaAnuncioEntity> toEntityList(List<FacturaAnuncio> facturaAnuncios);
    List<FacturaAnuncio>  toFacturaAnuncioList(List<FacturaAnuncioEntity> facturaAnuncios);
}
