package com.example.facturacion.FacturaSnacks.Dominio;


import com.example.facturacion.FacturaBoleto.Dominio.EstadoFacturacionBoleto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class FacturaSnacks {
    private UUID id;
    private UUID venta;
    private UUID usuarioId;
    private UUID idCine;
    private UUID ventaSnackId;
    private LocalDate fecha;
    private Double monto;
    private EstadoFacturacionBoleto estado;


    public FacturaSnacks(UUID id, UUID venta, UUID usuarioId, UUID idCine, UUID ventaSnackId,  LocalDate fecha, Double monto, EstadoFacturacionBoleto estado) {
        this.id = id;
        this.venta = venta;
        this.usuarioId = usuarioId;
        this.idCine = idCine;
        this.ventaSnackId = ventaSnackId;
        this.fecha = fecha;
        this.monto = monto;
        this.estado = estado;
    }
}
