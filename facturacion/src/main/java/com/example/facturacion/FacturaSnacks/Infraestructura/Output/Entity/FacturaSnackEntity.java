package com.example.facturacion.FacturaSnacks.Infraestructura.Output.Entity;

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
@Table(name = "factura_snack")
public class FacturaSnackEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    // puede estar o no en asociada a una venta
    @Column( nullable = true)
    private UUID venta;
    @Column( nullable = false)
    private UUID usuarioId;
    @Column( nullable = false)
    private UUID idCine;
    @Column( nullable = true)
    private UUID ventaSnackId;
    @Column(nullable = false)
    private LocalDate fecha;
    @Column( nullable = false)
    private Double monto;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoFacturacionBoleto estado;

}
