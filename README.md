# INICIO DE LA APLICACIÓN

Esta aplicación es el desarrollo de un sistema de administración de torneos
de futbol para una gran municipalidad de un lugar en específico, el objetivo
será construir esta aplicación _BACKEND_ en una arquitectura de micro servicios 
(aplicación API RESTFul), generando una aplicación más mantenible; hasta el momento 
no se ha definido de cuantos micro servicios tendrémos, pero se lista al final de
este enunciado los MS tentativos para la aplicación. Efectivamente, la aplicación está
desarrollada en Spring Boot versión 3.3.2 y estamos manejando diferentes bases
de datos Postgres para cada MS (cada MS tendrá su BD independiente). Para la comunicación
entre los micros estaremos usando Feign así como ORM base será JPA e Hibernate para el
tema de las consultas SQL, las entidades estarán manejadas por Lombok y tendremos adecuado
en cada uno de los controladores la respectiva documentación Swagger.

Las diferentes bases de datos están manejadas con contenedores de Docker para tener
mejor independizadas las tareas así como mantenibilidad de la aplicación.

* _**MÓDULOS (MICROS) TENTATIVOS**:_
  * Personas
  * Deportistas
  * Torneos
  * Históricos
  * Activos Fíjos
  * Entrenamiento
  * Usuarios (Autorización / Autenticación)

---------------------------------------------------------------------------------------

# Desarrollado por: #
Desarrollador de Backend: [Juan Sebastian Medina Toro](https://www.linkedin.com/in/juan-sebastian-medina-toro-887491249/).

---------------------------------------------------------------------------------------

### Levantamiento de la aplicación:
Para correr la aplicación en ambiente de desarrollo necesitamos:

**NOTA:** Si no vamos a usar la imagen ya dockerizada, podemos levantar la base de
datos solamente con los comandos de a continuación, en caso contrario, bastaría
con ejecutar la imagen y levantamos tanto la aplicación como el contenedor, pero,
_haga esta parte si ya la aplicación está completamente desarrollada_. 
* Levantar las bases de datos de cada micro servicio, requerimos que se
posicione en cada uno de los micros y ejecute el comando de docker que le 
corresponda:

````dockerfile
**************************************
*** Para el MS de persons ejecute: ***
**************************************
docker-compose -p ms_propoligas_persons up -d
````
````dockerfile
****************************************
*** Para el MS de sportsman ejecute: ***
****************************************
docker-compose -p ms_propoligas_sportsman up -d
````

En caso de levantar con imagen dockerizada, solo bastaría el comando
````dockerfile
$ docker-compose -p ms_propoligas_container-persons up -d
````
Este comando se explica más a detalle cuando generamos el tema de la imagen,
que en últimas lo que hacemos es que generamos el contenedor en embebido.

---------------------------------------------------------------------------------------
# DOCUMENTACIÓN SWAGGER #
* La documentación _**SWAGGER**_ de los proyectos en ambiente de desarrollo están
en las siguientes URL (Puede acceder desde cualquier navegador):
  
  * <<< MS DE PERSONAS >>>
    ````dockerfile
    $ http://localhost:13551/business/swagger-ui/index.html
    ````
  * <<< MS DE DEPORTISTAS >>>
    ````dockerfile
    $ http://localhost:13552/business/swagger-ui/index.html
    ````
---------------------------------------------------------------------------------------
# EJECUCIÓN DE IMÁGENES DE DOCKER #

* Ejecutando el comando para crear la imagen estando dentro del proyecto persons 
 (raíz dónde está el Dockerfile - Este comando lo ejecutamos si haremos la
  ejecución por segmentos, sino, entonces podríamos ejecutar el comando que
  sigue a continuación [docker-compose] ):
````dockerfile
$ docker build -t propoligas_person_image --no-cache --build-arg JAR_FILE=target/*.jar .
````

* Levantamiento de la imagen y el contenedor de una vez:
**Tendremos algunos casos en los que ya tenemos definido el ``docker-compose.yml`` que tiene la info**
**de la base de datos en docker y allí combinamos esto con la imagen de docker que generaremos;**
**este será el caso más general que tendremos en esta APP, por tanto, siempre que vayamos a**
**generar el archivo, ``nos paramos en la raíz del micro servicio y ejecutamos el comando siguiente``:**

NOTA: Lo único que cambia es el como se llama el JAR generado luego de Clean-Compile-Install.

* Usar primero el comando:
````dockerfile
$ docker network create propoligas-network
````
Debemos crear una red virtual para comunicar internamente cada contenedor en local (porque ya están
dockerizados). El comando solo se ejecuta una vez, por lo general.

* Luego usamos el comando:
````dockerfile
$ docker-compose -p ms_propoligas_container-persons up -d
````
El comando anterior fue para el ``MS de Persons``, cambia el nombre según el MS a ejecutar y debemos estar
posicionados en el micro servicio al que le vamos a hacer la generación

* Verificamos que los contenedores con sus respectivas BD queden dentro de la red usando el comando:
````dockerfile
$ docker network inspect propoligas-network
````

---------------------------------------------------------------------------------------
# SEED (Ambiente de desarrollo)
Hasta la fecha (Agost 13/2024) no se ha planteado un seed para estos casos, sin embargo,
se está desarrollando por medio de contenedores y en cada MS se está generado en DEV una
carpeta llamada **``containers``**, esta carpeta contiene la información en local para las
pruebas, por tanto, en la raíz del macro proyecto propoligas encontramos una carpeta
llamada ``backup``, esta carpeta contiene un .rar actualizado por fechas de la información,
lo único que se tendría que hacer es si no se tiene información al levantar la imagen, es
descomprimir el .rar actualizado y pegarlo en la raíz del containers de la aplicación
deseada, de este modo se tendrá información de prueba, por ahora no se contempla un 
End Point para este tema.

---------------------------------------------------------------------------------------
# Ejecución de aplicaciones pero por perfiles:
Una vez compilada la aplicación y generado el JAR usando Clean-Compile-Install
desde las opciones de Maven, para poder ejecutar con un perfil específico el JAR
generado en una consola aparte, usamos el comando:
````dockerfile
$ java -jar .\persons-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
````
_Explicación del comando:_ Nos ubicamos en la carpeta donde se encuentre el JAR,
ejecutamos el comando por consola, pero, le agregamos la bandera de
--spring.profiles.active=dev, el *dev* sale del nombre que le dimos al archivo
application-dev.properties, después del - el nombre que le hayamos dado, en este 
caso, le dimos dev, entonces está será la bandera y ejecutamos la aplicación con
las configuraciones de ese archivo.

Si no aplico la bandera --spring.profiles.active=dev entonces la aplicación tomará
la configuración por defecto, es decir, tomará el application.properties, es muy
importante tener esto en cuenta.

Se tiene pensado 3 ambientes de trabajo:
* *dev* = Ambiente de Desarrollo
* *qa* = Ambiente de Pruebas
* *pdn* = Ambiente de Producción

---------------------------------------------------------------------------------------
# INFORMACIÓN ADICIONAL:
Estamos trabajando un _**MONOREPO**_ para esta aplicación, lo que quiere decir que
todos los micro servicios se encuentran alojados dentro de un proyecto de maven,
generando una programación "por dominios", pero cada uno de los MS se encuentra
independizado, y si uno requiere de otro, implementa la comunicación con Feign.