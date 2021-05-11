# El Tiempo - Aplicación Java Cliente/Servidor
Esta sencilla aplicación Java está diseñada para hacer una demostración de la arquitectura cliente-servidor. Tanto servidor como cliente están diseñados para escuchar en el puerto 8989. La aplicación cliente se conecta con la dirección «localhost» por defecto, por lo que en principio la aplicación se debe probar ejecutando ambos en la misma máquina. El paso de mensajes entre los procesos se realiza mediante el uso de sockets TCP, lo cual garantiza la seguridad e integridad de la comunicación entre ambos procesos.

* La aplicación servidor se encarga de recoger y enviar al cliente los datos del tiempo en todas las ciudades de España y usa interfaz de línea de comandos.
* La aplicación cliente cuenta con una interfaz gráfica con un mapa interactivo. Requiere que esté funcionando previamente el servidor para funcionar.

## Instalación y requisitos
La forma más fácil de editar el proyecto es utilizando el IDE Netbeans.

## Notas
Si encuentras cualquier error o quieres proponer una mejora en el proyecto, simplemente envía un pull request.

