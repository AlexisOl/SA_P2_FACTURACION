package com.example.facturacion.FacturaBoleto.Infraestructura.Kafka;


import com.example.comun.DTO.FacturaAnuncio.AnuncioCreadoDTO;
import com.example.comun.DTO.FacturaAnuncio.RespuestaFacturaAnuncioCreadaDTO;
import com.example.comun.DTO.FacturaBoleto.CobroCineDTO;
import com.example.comun.DTO.FacturaBoleto.FacturaBoletoCreadoDTO;
import com.example.comun.DTO.eventos.VerificarRespuestaDTO;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Input.CrearFacturaAnuncioInputPort;
import com.example.facturacion.FacturaAnuncio.Dominio.FacturaAnuncio;
import com.example.facturacion.FacturaBoleto.Aplicacion.Ports.input.CrearFacturaBoletoInputPort;
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

@Component
@AllArgsConstructor

public class KafkaFacturaBoletoAdaptador {

    private final CrearFacturaBoletoInputPort crearFacturaBoletoInputPort;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


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

            CobroCineDTO cobro = new CobroCineDTO();
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
}
