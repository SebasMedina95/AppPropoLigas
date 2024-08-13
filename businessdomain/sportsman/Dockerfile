# Utiliza una imagen base de OpenJDK JRE 21 sobre Alpine
FROM eclipse-temurin:21-alpine

# Crear un grupo y un usuario sin privilegios
RUN addgroup -g 1028 devopsc && adduser -D -G devopsc admin

# Copiar el archivo JAR en el contenedor
COPY target/sportsman-0.0.1-SNAPSHOT.jar /app.jar

# Asignación de permisos al usuario
RUN chown -R admin:devopsc /tmp

# Establecer el usuario actual como 'admin'
USER admin

# Expone el puerto que usará la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app.jar"]
