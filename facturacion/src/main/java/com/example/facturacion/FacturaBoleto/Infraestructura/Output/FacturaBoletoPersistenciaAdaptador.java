package com.example.facturacion.FacturaBoleto.Infraestructura.Output;


import com.example.facturacion.FacturaBoleto.Aplicacion.Ports.output.CrearFacturaBoletoOutputPort;
import com.example.facturacion.FacturaBoleto.Dominio.FacturaBoleto;
import com.example.facturacion.FacturaBoleto.Infraestructura.Output.Mapper.FacturaBoletoMapper;
import com.example.facturacion.FacturaBoleto.Infraestructura.Output.Repository.FacturaBoletoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FacturaBoletoPersistenciaAdaptador implements CrearFacturaBoletoOutputPort {


    private final FacturaBoletoRepository facturaBoletoRepository;
    private final FacturaBoletoMapper facturaBoletoMapper;


    @Override
    public FacturaBoleto crearFacturaBoleto(FacturaBoleto facturaBoleto) {
        return this.facturaBoletoMapper.toFacturaBoleto(
                this.facturaBoletoRepository.save(this.facturaBoletoMapper.toEntity(facturaBoleto))
        );
    }
}
