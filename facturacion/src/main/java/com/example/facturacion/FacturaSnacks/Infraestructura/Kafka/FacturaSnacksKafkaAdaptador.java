package com.example.facturacion.FacturaSnacks.Infraestructura.Kafka;


import com.example.comun.DTO.FacturaBoleto.*;
import com.example.comun.DTO.eventos.VerificarRespuestaDTO;
import com.example.facturacion.FacturaBoleto.Aplicacion.Ports.input.CrearFacturaBoletoInputPort;
import com.example.facturacion.FacturaBoleto.Aplicacion.Ports.output.CambioEstadoFacturasOutputPort;
import com.example.facturacion.FacturaBoleto.Dominio.EstadoFacturacionBoleto;
import com.example.facturacion.FacturaBoleto.Dominio.FacturaBoleto;
import com.example.facturacion.FacturaSnacks.Aplicacion.Ports.Input.CrearFacturaSnacksInputPort;
import com.example.facturacion.FacturaSnacks.Aplicacion.Ports.Output.CambioEstadoFacturasSnacksOutputPort;
import com.example.facturacion.FacturaSnacks.Dominio.FacturaSnacks;
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

@Component
@AllArgsConstructor
public class FacturaSnacksKafkaAdaptador {

    //crear-factura-snacks-directa
    private final CrearFacturaSnacksInputPort facturaSnacksInputPort;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final CambioEstadoFacturasSnacksOutputPort cambioEstadoFacturasSnacksOutputPort;

    @KafkaListener(topics = "crear-factura-snacks-directa", groupId = "factura-group")
    public void crearFacturaBoleto(@Payload String mensaje, @Header(KafkaHeaders.CORRELATION_ID) String correlationId) throws Exception {
        //deserealiza
        try {
            FacturaSnackCreadaDTO solicitud = objectMapper.readValue(mensaje, FacturaSnackCreadaDTO.class);
            System.out.println("aca2");
            FacturaSnacks facturaActual = this.facturaSnacksInputPort.crearFacturaSnacks(solicitud);

            // Crear respuesta
            boolean existe = true;
            if(facturaActual == null){
                existe = false;
            }


            VerificarRespuestaDTO respuesta = new VerificarRespuestaDTO();
            respuesta.setExiste(existe);
            respuesta.setCorrelationId(correlationId);
            // Serializar y enviar respuesta con correlationId header

            //aca enviar para quitar al usuario y agregar dinero al cine
            // agrega el cine
            CobroCineDTO cobro = new CobroCineDTO();
            cobro.setFactura(facturaActual.getId());
            cobro.setCosto(facturaActual.getMonto());
            cobro.setIdCine(solicitud.getIdCine());
            cobro.setCorrelationId(solicitud.getCorrelationId());
            cobro.setVentaId(solicitud.getVentaSnackId());

            String respuestaMensaje = objectMapper.writeValueAsString(cobro);
//
//            Message<String> kafkaMessage = MessageBuilder
//                    .withPayload(respuestaMensaje)
//                    .setHeader(KafkaHeaders.TOPIC, "propiedad-facturacion-generada")
//                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
//                    .build();
//            kafkaTemplate.send(kafkaMessage);

            // le quita al usuario
            DebitoUsuario debito = new DebitoUsuario();
            debito.setMotivo("Pago de boletos de cine para la funcion");
            debito.setCorrelationId(solicitud.getCorrelationId());
            debito.setFactura(facturaActual.getId());
            debito.setVentaId(solicitud.getVentaSnackId());
            debito.setMonto(facturaActual.getMonto());
            debito.setUserId(solicitud.getUsuarioId());


            String respuestaDebito = objectMapper.writeValueAsString(debito);

            Message<String> kafkaMessageDebitoUsuario = MessageBuilder
                    .withPayload(respuestaDebito)
                    .setHeader(KafkaHeaders.TOPIC, "debito-usuario-snacks")
                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                    .build();
            kafkaTemplate.send(kafkaMessageDebitoUsuario);



        }catch (Exception e){
            throw new RuntimeException("Error enviando evento de creación de venta", e);

        }


    }


    @KafkaListener(topics = "factura-snacks-actualizada", groupId = "factura-group")
    @Transactional
    public void manejarExitoFactura(
            @Payload String mensaje,
            @Header(value = KafkaHeaders.CORRELATION_ID, required = false) String correlationId
    )   throws Exception {

        RespuestaFacturaBoletoCreadoDTO solicitud = objectMapper.readValue(mensaje, RespuestaFacturaBoletoCreadoDTO.class);

        this.cambioEstadoFacturasSnacksOutputPort.cambiarEstadoVenta(solicitud.getFactura(),
                EstadoFacturacionBoleto.COMPLETADA);
    }

    @KafkaListener(topics = "factura-snacks-fallido", groupId = "factura-group")
    @Transactional
    public void manejarFalloFactura(
            @Payload String mensaje,
            @Header(value = KafkaHeaders.CORRELATION_ID, required = false) String correlationId
    )  throws Exception {
        RespuestaFacturaBoletoCreadoDTO solicitud = objectMapper.readValue(mensaje, RespuestaFacturaBoletoCreadoDTO.class);
        System.out.println("enviando evento de fallido"+solicitud.getFactura());

        this.cambioEstadoFacturasSnacksOutputPort.cambiarEstadoVenta(solicitud.getFactura(),
                EstadoFacturacionBoleto.CANCELADA);
    }
}
