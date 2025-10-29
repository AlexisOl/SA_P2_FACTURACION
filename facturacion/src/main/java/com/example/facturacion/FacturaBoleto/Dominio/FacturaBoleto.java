package com.example.facturacion.FacturaBoleto.Dominio;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class FacturaBoleto {
    private UUID id;
    private UUID venta;
    private UUID cliente;
    private LocalDate fecha;
    private Double monto;
    private EstadoFacturacionBoleto estado;


    public FacturaBoleto(UUID id, UUID venta, UUID cliente, LocalDate fecha, Double monto, EstadoFacturacionBoleto estado) {
        this.id = id;
        this.venta = venta;
        this.cliente = cliente;
        this.fecha = fecha;
        this.monto = monto;
        this.estado = estado;
    }
}
