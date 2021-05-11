// ServidorElTiempo
// (CC) JotaEle, 2016

// Crea un servidor Concurrente (Es decir, acepta peticiones de varios clientes a la vez) que escucha en 
// un puerto especificado por nosotros y establece una conexión con el cliente mediante el 
// uso de Sockets TCP. Nuestro servidor recibirá las peticiones del cliente y las gestionará
// para devolverle en formato JSON los datos meteorológicos solicitados de la ciudad deseada.

package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorElTiempo extends Thread {

    public static void main(String[] args) {

        int port = 8989;               // Puerto de escucha
        byte []buffer = new byte[256]; // Array de bytes auxiliar para recibir o enviar datos.
        int bytesLeidos = 0;           // Número de bytes leídos


        // Creamos un ServerSocket que se encargará de escuchar 
        // en el puerto y gestionar la conexión.
        ServerSocket socketServidor;

        System.out.println("**** EL TIEMPO INTERACTIVO: SERVIDOR ****\n");
        System.out.println("Puerto en el que se escucha: "+port+"\n");
     
        try {

            // Habilitamos el ServerSocket en modo pasivo,
            // escuchando en el puerto que hemos indicado.
            socketServidor = new ServerSocket(port); 

            // -- BUCLE DEL SERVIDOR --

            while(true) {

                // Creamos un Socket que será el que contenga 
                // la tubería de bytes entre cliente y servidor.
                Socket socketConexion = null;

                try {
                    // Al usar accept() sobre el ServerSocket se nos devuelve
                    // el socket que contiene el pipe cliente-servidor.
                    // Lo asignamos en el socket que nos hemos creado.
                    socketConexion = socketServidor.accept();
                } catch (IOException e) {
                    System.out.println("Error: no se pudo aceptar la conexión solicitada");
                }


                // Creamos un objeto de la clase ProcesadorElTiempo, pasándole como 
                // argumento el nuevo socket, para que descargue y procese la petición.
                // Este esquema de trabajo permite que se puedan usar hebras más fácilmente.
                ProcesadorElTiempo procesador = new ProcesadorElTiempo(socketConexion);

                // Lanzamos el procesado de manera que pueda ser concurrente.
                procesador.start();				
            }

        } catch (IOException e) {
                System.err.println("Error al escuchar en el puerto " + port);
        }
    }
}