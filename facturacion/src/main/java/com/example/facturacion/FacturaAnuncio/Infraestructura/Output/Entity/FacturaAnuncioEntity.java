package com.example.facturacion.FacturaAnuncio.Infraestructura.Output.Entity;

import com.example.facturacion.FacturaAnuncio.Dominio.EstadoFacturacion;
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
@Table(name = "factura_anuncio")
public class FacturaAnuncioEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column( nullable = false)
    private UUID anuncio;
    @Column( nullable = true)
    private UUID usuario;
    @Column( nullable = false)
    private Double monto;
    @Column(nullable = false)
    private LocalDate fecha;
    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private EstadoFacturacion estado;
    @Column(nullable = true)
    private String detalle;
    @Column( nullable=true)
    private UUID cine;

}
