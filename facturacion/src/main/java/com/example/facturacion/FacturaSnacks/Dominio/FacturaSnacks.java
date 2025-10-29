package com.example.facturacion.FacturaSnacks.Dominio;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class FacturaSnacks {
    private UUID id;
    private UUID venta;
    private UUID facturaBoleto;
    private LocalDate fecha;
    private Double monto;


    public FacturaSnacks(UUID id, UUID venta, UUID facturaBoleto, LocalDate fecha, Double monto) {
        this.id = id;
        this.venta = venta;
        this.facturaBoleto = facturaBoleto;
        this.fecha = fecha;
        this.monto = monto;
    }
}
