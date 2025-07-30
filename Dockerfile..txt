# Imagen base con Java 17
FROM eclipse-temurin:17-jdk-alpine

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR generado en el build
COPY target/*.jar app.jar

# Exponer el puerto que usará la app (8080 por defecto en Spring Boot)
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
