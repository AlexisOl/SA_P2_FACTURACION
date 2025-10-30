package com.example.facturacion.FacturaSnacks.Infraestructura.Output.Mapper;

import com.example.facturacion.FacturaSnacks.Dominio.FacturaSnacks;
import com.example.facturacion.FacturaSnacks.Infraestructura.Output.Entity.FacturaSnackEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface FacturaSnacksMapper {

    FacturaSnacks toFacturaSnacks(FacturaSnackEntity facturaSnackEntity);
    FacturaSnackEntity  toFacturaSnackEntity(FacturaSnacks facturaSnacks);

    

}
