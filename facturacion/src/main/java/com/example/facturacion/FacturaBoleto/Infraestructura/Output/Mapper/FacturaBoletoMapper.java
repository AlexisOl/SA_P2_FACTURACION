package com.example.facturacion.FacturaBoleto.Infraestructura.Output.Mapper;

import com.example.facturacion.FacturaBoleto.Dominio.FacturaBoleto;
import com.example.facturacion.FacturaBoleto.Infraestructura.Output.Entity.FacturaBoletoEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface FacturaBoletoMapper {
    FacturaBoletoEntity toEntity(FacturaBoleto facturaBoleto);
    FacturaBoleto toFacturaBoleto(FacturaBoletoEntity facturaBoletoEntity);
}
