# INICIO DE LA APLICACIÓN

Esta aplicación es el desarrollo de un sistema de administración de torneos
de futbol para una gran municipalidad de un lugar en específico, el objetivo
será construir esta aplicación _BACKEND_ en una arquitectura de micro servicios,
generando una aplicación más mantenible, hasta el momento no se ha
definido de cuantos micro servicios tendrémos. Efectivamente, la aplicación está
desarrollada en Spring Boot versión 3.3.2 y estamos manejando diferentes bases
de datos Postgres para cada BD que tendrá cada micro servicio. Para la comunicación
entre los micros estaremos usando Feign así como ORM base será JPA e Hibernate.
Las diferentes bases de datos están manejadas con contenedores de Docker para tener
mejor independizadas las tareas así como mantenibilidad de la aplicación.

* MÓDULOS (MICROS) TENTATIVOS:
  * Personas
  * Deportistas
  * Torneos
  * Históricos
  * Activos Fíjos
  * Entrenamiento
  * Usuarios (Autorización / Autenticación)

### Desarrollado por: ###
Desarrollador de Backend: [Juan Sebastian Medina Toro](https://www.linkedin.com/in/juan-sebastian-medina-toro-887491249/)

### Levantamiento de la aplicación:
Para correr la aplicación en ambiente de desarrollo necesitamos:

* Levantar las bases de datos de cada micro servicio, requerimos que se
posicione en cada uno ded los micros y ejecute el comando de docker:
````dockerfile
$ docker compose up -d
````

* La documentación de los proyectos en ambiente de desarrollo están
en las siguientes URL (Puede acceder desde cualquier navegador):

  * <<< MS DE PERSONAS >>>
    ````dockerfile
    $ http://localhost:13551/swagger-ui/index.html
    ````
  * <<< MS DE DEPORTISTAS >>>
    ````dockerfile
    $ http://localhost:13552/swagger-ui/index.html
    ````

# Información adicional:
Estamos trabajando un _MONOREPO_ para esta aplicación, lo que quiere decir que
todos los micro servicios se encuentran alojados dentro de un proyecto de maven,
generando una programación "por dominios", pero cada uno de los MS se encuentra
independizado, y si uno requiere de otro, implementa la comunicación con Feign.
