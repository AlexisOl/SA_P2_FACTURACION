package com.example.facturacion.FacturaAnuncio.Infraestructura.Kafka;


import com.example.comun.DTO.DW.ListadoFacturadoAnuncio;
import com.example.comun.DTO.DW.ReplicacionFacturaAnuncioDTO;
import com.example.comun.DTO.FacturaAnuncio.*;
import com.example.comun.DTO.FacturaBoleto.DebitoUsuario;
import com.example.comun.DTO.FacturaBoleto.RespuestaFacturaBoletoCreadoDTO;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Input.CrearFacturaAnuncioCineInputPort;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Input.CrearFacturaAnuncioInputPort;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output.CambioEstadoFacturasAnuncioOutputPort;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output.CambioMonetarioFacturaAnuncioOutputPort;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Output.ObtenerFacturaEspecificaOutputPort;
import com.example.facturacion.FacturaAnuncio.Dominio.EstadoFacturacion;
import com.example.facturacion.FacturaAnuncio.Dominio.FacturaAnuncio;
import com.example.facturacion.FacturaAnuncio.Infraestructura.Output.Mapper.FacturaAnuncioMapper;
import com.example.facturacion.FacturaBoleto.Dominio.EstadoFacturacionBoleto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class FacturaAnuncioKafkaAdaptador {

    private final CrearFacturaAnuncioInputPort crearFacturaAnuncioInputPort;
    private final CrearFacturaAnuncioCineInputPort crearFacturaAnuncioCineInputPort;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CambioEstadoFacturasAnuncioOutputPort cambioEstadoFacturasAnuncioOutputPort;
    private final ObjectMapper objectMapper;
    private final CambioMonetarioFacturaAnuncioOutputPort cambioMonetarioFacturaAnuncioOutputPort;
    private final ObtenerFacturaEspecificaOutputPort obtenerFacturaEspecificaOutputPort;
    private final FacturaAnuncioMapper facturaAnuncioMapper;



    @KafkaListener(topics = "generar-factura-anuncio", groupId = "factura-group")
    public void crearFacturaAnuncio(@Payload String mensaje, @Header(KafkaHeaders.CORRELATION_ID) String correlationId) throws Exception {


        //deserealiza
        AnuncioCreadoDTO solicitud = objectMapper.readValue(mensaje, AnuncioCreadoDTO.class);
        // Invocar el crear


        // ver que pasa
        System.out.println("Solicitud: " + solicitud.getAnuncioId());
        FacturaAnuncio facturaActual = this.crearFacturaAnuncioInputPort.crearFacturaAnuncio(solicitud);

        // Crear respuesta
        boolean existe = true;
//        if(facturaActual == null){
//            existe = false;
//        }
        // calcula precio en base a fechas



        // aca descuentoa
        // le quita al usuario a partir de aca se le agrega al cine
        AnuncioCreadoDTO acreditaCine = new AnuncioCreadoDTO();
        acreditaCine.setCorrelationId(solicitud.getCorrelationId());
        acreditaCine.setFactura(facturaActual.getId());
        acreditaCine.setAnuncioId(solicitud.getAnuncioId());
        acreditaCine.setCosto(solicitud.getCosto());
        acreditaCine.setUsuarioId(solicitud.getUsuarioId());
        acreditaCine.setFechainicio(solicitud.getFechainicio());
        acreditaCine.setFechafin(solicitud.getFechafin());

        System.out.println(solicitud.getFechafin());


        System.out.println("ACAAAAAAAAAAAAAA VER ");


        String respuestaDebito = objectMapper.writeValueAsString(acreditaCine);

        Message<String> kafkaMessageDebitoUsuario = MessageBuilder
                .withPayload(respuestaDebito)
                .setHeader(KafkaHeaders.TOPIC, "propiedad-anuncio-creado")
                .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                .build();
        kafkaTemplate.send(kafkaMessageDebitoUsuario);



    }


    // esucha para cambiar de estado la factura

    @KafkaListener(topics = "creacion-factura-anuncio-especifica", groupId = "factura-group")
    @Transactional
    public void manejarExitoFactura(
            @Payload String mensaje,
            @Header(value = KafkaHeaders.CORRELATION_ID, required = false) String correlationId
    )   throws Exception {
        List<ReplicacionFacturaAnuncioDTO> facturacionDW = new ArrayList<>();

        RespuestaFacturaAnuncioCreadaDTO solicitud = objectMapper.readValue(mensaje, RespuestaFacturaAnuncioCreadaDTO.class);

        //cambio a la factura del usuario
        this.cambioMonetarioFacturaAnuncioOutputPort.cambiarCantidad(solicitud.getFactura(), solicitud.getMonto());
        this.cambioEstadoFacturasAnuncioOutputPort.cambiarEstadoVenta(solicitud.getFactura(),
                EstadoFacturacion.EXITOSA);

        //obtener el anuncio general
        FacturaAnuncio facturaUsuario = this.obtenerFacturaEspecificaOutputPort.getFacturaAnuncioEspecifica(solicitud.getFactura());

        facturacionDW.add(this.facturaAnuncioMapper.toReplicacionDTO(facturaUsuario));

        //generar ingreso de facturacion
        for (DiasDescuentoAnunciosBloqueados cines: solicitud.getDineroCines()) {
            RespuestaAnuncioCreadoCineDTO nuevaFactura = new RespuestaAnuncioCreadoCineDTO();
            nuevaFactura.setAnuncioId(solicitud.getAnuncioId());
            nuevaFactura.setCosto(cines.getPrecio());
            nuevaFactura.setCorrelationId(correlationId);
            nuevaFactura.setCineId(cines.getCine());

            FacturaAnuncio facturaAnuncio = this.crearFacturaAnuncioCineInputPort.crearFacturaCineAnuncio(nuevaFactura);

            facturacionDW.add(this.facturaAnuncioMapper.toReplicacionDTO(facturaAnuncio));


            this.cambioEstadoFacturasAnuncioOutputPort.cambiarEstadoVenta(facturaAnuncio.getId(),
                    EstadoFacturacion.EXITOSA);
        }

        // enviar el otro evento al dw
        ListadoFacturadoAnuncio nuevoValor = new ListadoFacturadoAnuncio();
        nuevoValor.setListado(facturacionDW);
        String respuestaDebito = objectMapper.writeValueAsString(nuevoValor);

        Message<String> kafkaMessageDebitoUsuario = MessageBuilder
                .withPayload(respuestaDebito)
                .setHeader(KafkaHeaders.TOPIC, "ingreso-detalle-anuncio-facturacion")
                .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                .build();
        kafkaTemplate.send(kafkaMessageDebitoUsuario);

    }

    @KafkaListener(topics = "creacion-factura-anuncio-fallido", groupId = "factura-group")
    @Transactional
    public void manejarFalloFactura(
            @Payload String mensaje,
            @Header(value = KafkaHeaders.CORRELATION_ID, required = false) String correlationId
    )  throws Exception {
        //generar ingreso de facturacion

        RespuestaFacturaAnuncioCreadaDTO solicitud = objectMapper.readValue(mensaje, RespuestaFacturaAnuncioCreadaDTO.class);
        System.out.println(solicitud.getFactura()+ "----------------"+solicitud.getMonto()+ "-------------"+solicitud.getMotivoFallo());

        //cambio a la factura del usuario
        this.cambioMonetarioFacturaAnuncioOutputPort.cambiarCantidad(solicitud.getFactura(), solicitud.getMonto());
        this.cambioEstadoFacturasAnuncioOutputPort.cambiarEstadoVenta(solicitud.getFactura(),
                EstadoFacturacion.CANCELADA);

        //generar ingreso de facturacion
        for (DiasDescuentoAnunciosBloqueados cines: solicitud.getDineroCines()) {
            RespuestaAnuncioCreadoCineDTO nuevaFactura = new RespuestaAnuncioCreadoCineDTO();
            nuevaFactura.setAnuncioId(solicitud.getAnuncioId());
            nuevaFactura.setCosto(cines.getPrecio());
            nuevaFactura.setCorrelationId(correlationId);
            nuevaFactura.setCineId(cines.getCine());

            FacturaAnuncio facturaAnuncio = this.crearFacturaAnuncioCineInputPort.crearFacturaCineAnuncio(nuevaFactura);

            this.cambioEstadoFacturasAnuncioOutputPort.cambiarEstadoVenta(facturaAnuncio.getId(),
                    EstadoFacturacion.CANCELADA);
        }
    }

}
