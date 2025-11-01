FROM bellsoft/liberica-openjdk-alpine:21.0.3

WORKDIR /app

COPY target/mcsv-facturacion.jar /app/mcsv-facturacion.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "mcsv-facturacion.jar"]
