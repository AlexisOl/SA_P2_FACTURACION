package com.example.facturacion.FacturaAnuncio.Infraestructura.Kafka;


import com.example.comun.DTO.FacturaAnuncio.AnuncioCreadoDTO;
import com.example.comun.DTO.FacturaAnuncio.RespuestaFacturaAnuncioCreadaDTO;
import com.example.facturacion.FacturaAnuncio.Aplicacion.Ports.Input.CrearFacturaAnuncioInputPort;
import com.example.facturacion.FacturaAnuncio.Dominio.FacturaAnuncio;
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
public class FacturaAnuncioKafkaAdaptador {

    private final CrearFacturaAnuncioInputPort crearFacturaAnuncioInputPort;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;



    @KafkaListener(topics = "crear-factura-anuncio", groupId = "factura-group")
    public void crearFacturaAnuncio(@Payload String mensaje, @Header(KafkaHeaders.CORRELATION_ID) String correlationId) throws Exception {


        //deserealiza
        AnuncioCreadoDTO solicitud = objectMapper.readValue(mensaje, AnuncioCreadoDTO.class);
        // Invocar el crear


        // ver que pasa
        System.out.println("Solicitud: " + solicitud.getAnuncioId());
        FacturaAnuncio facturaActual = this.crearFacturaAnuncioInputPort.crearFacturaAnuncio(solicitud);

        // Crear respuesta
        boolean existe = true;
        if(facturaActual == null){
            existe = false;
        }


        RespuestaFacturaAnuncioCreadaDTO respuesta = new RespuestaFacturaAnuncioCreadaDTO();
        respuesta.setEstado(existe);
        respuesta.setCorrelationId(correlationId);
        // Serializar y enviar respuesta con correlationId header
        String respuestaMensaje = objectMapper.writeValueAsString(respuesta);
        if (existe) {

            Message<String> kafkaMessage = MessageBuilder
                    .withPayload(respuestaMensaje)
                    .setHeader(KafkaHeaders.TOPIC, "cine-actualizado")
                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                    .build();
            kafkaTemplate.send(kafkaMessage);
        } else {
            Message<String> kafkaMessage = MessageBuilder
                    .withPayload(respuestaMensaje)
                    .setHeader(KafkaHeaders.TOPIC, "propiedad-anuncio-fallido")
                    .setHeader(KafkaHeaders.CORRELATION_ID, correlationId)
                    .build();
            kafkaTemplate.send(kafkaMessage);
        }


    }
}
