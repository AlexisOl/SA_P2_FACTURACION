package com.example.facturacion.FacturaBoleto.Infraestructura.Output.Entity;

import com.example.facturacion.FacturaAnuncio.Dominio.EstadoFacturacion;
import com.example.facturacion.FacturaBoleto.Dominio.EstadoFacturacionBoleto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "factura_boleto")
public class FacturaBoletoEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column( nullable = false)
    private UUID venta;
    @Column( nullable = false)
    private UUID cliente;
    @Column(nullable = false)
    private LocalDate fecha;
    @Column( nullable = false)
    private Double monto;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFacturacionBoleto estado;
}
