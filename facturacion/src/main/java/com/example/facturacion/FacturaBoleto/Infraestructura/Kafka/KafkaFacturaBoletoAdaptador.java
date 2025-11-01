package com.example.facturacion.FacturaBoleto.Infraestructura.Kafka;


import com.example.comun.DTO.FacturaAnuncio.AnuncioCreadoDTO;
import com.example.comun.DTO.FacturaAnuncio.RespuestaFacturaAnuncioCreadaDTO;
import com.example.comun.DTO.FacturaBoleto.CobroCineDTO;
import com.example.comun.DTO.FacturaBoleto.DebitoUsuario;
import com.example.comun.DTO.FacturaBoleto.FacturaBoletoCreadoDTO;
import com.example.comun.DTO.FacturaBoleto.RespuestaFacturaBoletoCreadoDTO;
import com.example.comun.DTO.eventos.VerificarRespuestaDTO;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Input.CrearFacturaAnuncioInputPort;
import com.example.facturacion.FacturaAnuncio.Dominio.FacturaAnuncio;
import com.example.facturacion.FacturaBoleto.Aplicacion.Ports.input.CrearFacturaBoletoInputPort;
import com.example.facturacion.FacturaBoleto.Aplicacion.Ports.output.CambioEstadoFacturasOutputPort;
import com.example.facturacion.FacturaBoleto.Dominio.EstadoFacturacionBoleto;
import com.example.facturacion.FacturaBoleto.Dominio.FacturaBoleto;
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

public class KafkaFacturaBoletoAdaptador {

    private final CrearFacturaBoletoInputPort crearFacturaBoletoInputPort;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final CambioEstadoFacturasOutputPort cambioEstadoFacturasOutputPort;


    @KafkaListener(topics = "crear-factura-boleto", groupId = "factura-group")
    public void crearFacturaBoleto(@Payload String mensaje, @Header(KafkaHeaders.CORRELATION_ID) String correlationId) throws Exception {
        //deserealiza

        try {
            FacturaBoletoCreadoDTO solicitud = objectMapper.readValue(mensaje, FacturaBoletoCreadoDTO.class);
            System.out.println("aca");
            FacturaBoleto facturaActual = this.crearFacturaBoletoInputPort.crearFacturaBoleto(solicitud);


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
            cobro.setCosto(solicitud.getMontoTotal());
            cobro.setIdCine(solicitud.getIdCine());
            cobro.setCorrelationId(solicitud.getCorrelationId());
            cobro.setVentaId(solicitud.getVentaId());

            String respuestaMensaje = objectMapper.writeValueAsString(cobro);

            Message<String> kafkaMessage = MessageBuilder
                    .withPayload(respuestaMensaje)
                    .setHeader(KafkaHeaders.TOPIC, "propiedad-facturacion-generada")
                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                    .build();
            kafkaTemplate.send(kafkaMessage);

            // le quita al usuario
            DebitoUsuario debito = new DebitoUsuario();
            debito.setMotivo("Pago de boletos de cine para la funcion");
            debito.setCorrelationId(solicitud.getCorrelationId());
            debito.setFactura(facturaActual.getId());
            debito.setVentaId(solicitud.getVentaId());
            debito.setMonto(solicitud.getMontoTotal());
            debito.setUserId(solicitud.getUsuarioId());
            debito.setIdCine(solicitud.getIdCine());



            String respuestaDebito = objectMapper.writeValueAsString(debito);

            Message<String> kafkaMessageDebitoUsuario = MessageBuilder
                    .withPayload(respuestaDebito)
                    .setHeader(KafkaHeaders.TOPIC, "debito-usuario")
                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                    .build();
            kafkaTemplate.send(kafkaMessageDebitoUsuario);



        }catch (Exception e){
            throw new RuntimeException("Error enviando evento de creación de venta", e);

        }


//        if (existe) {
//
//            Message<String> kafkaMessage = MessageBuilder
//                    .withPayload(respuestaMensaje)
//                    .setHeader(KafkaHeaders.TOPIC, "cine-actualizado")
//                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
//                    .build();
//            kafkaTemplate.send(kafkaMessage);
//        } else {
//            Message<String> kafkaMessage = MessageBuilder
//                    .withPayload(respuestaMensaje)
//                    .setHeader(KafkaHeaders.TOPIC, "propiedad-anuncio-fallido")
//                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
//                    .build();
//            kafkaTemplate.send(kafkaMessage);
//        }


    }


    @KafkaListener(topics = "factura-actualizada", groupId = "factura-group")
    @Transactional
    public void manejarExitoFactura(
            @Payload String mensaje,
            @Header(value = KafkaHeaders.CORRELATION_ID, required = false) String correlationId
    )   throws Exception {

        RespuestaFacturaBoletoCreadoDTO solicitud = objectMapper.readValue(mensaje, RespuestaFacturaBoletoCreadoDTO.class);

        this.cambioEstadoFacturasOutputPort.cambiarEstadoVenta(solicitud.getFactura(),
                EstadoFacturacionBoleto.COMPLETADA);
    }

    @KafkaListener(topics = "factura-fallido", groupId = "factura-group")
    @Transactional
    public void manejarFalloFactura(
            @Payload String mensaje,
            @Header(value = KafkaHeaders.CORRELATION_ID, required = false) String correlationId
    )  throws Exception {
        RespuestaFacturaBoletoCreadoDTO solicitud = objectMapper.readValue(mensaje, RespuestaFacturaBoletoCreadoDTO.class);
        System.out.println("enviando evento de fallido"+solicitud.getFactura());

        this.cambioEstadoFacturasOutputPort.cambiarEstadoVenta(solicitud.getFactura(),
                EstadoFacturacionBoleto.CANCELADA);
    }

}
