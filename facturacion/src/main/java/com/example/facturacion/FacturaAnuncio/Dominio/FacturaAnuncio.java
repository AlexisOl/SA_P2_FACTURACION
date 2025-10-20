package com.example.facturacion.FacturaAnuncio.Dominio;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class FacturaAnuncio {
    private UUID id;
    private UUID anuncio;
    private UUID usuario;
    private Double monto;
    private LocalDate fecha;
    // estado
    private EstadoFacturacion estado;
    private String detalle;


    public FacturaAnuncio(UUID id, UUID anuncio, UUID usuario, Double monto, LocalDate fecha, EstadoFacturacion estado) {
        this.id = id;
        this.anuncio = anuncio;
        this.usuario = usuario;
        this.monto = monto;
        this.fecha = fecha;
        this.estado = estado;
    }
}
