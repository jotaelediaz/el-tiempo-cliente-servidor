
// ProcesadorElTiempo
// (CC) JotaEle, 2016

// Lee las peticiones del cliente y lleva a cabo el procesamiento
// de dichas peticiones, obteniendo los datos meteorológicos solicitados
// usando la API de OpenWeatherMap.
// Se encarga de usar flujos de lectura y escritura para leer y enviar
// los datos del cliente usando el socket TCP del servidor.

package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.*;


public class ProcesadorElTiempo extends Thread {

    private Socket socketServicio;     // Referencia a un socket para enviar/recibir las peticiones/respuestas
    private InputStream inputStream;   // Stream de lectura (por aquí se recibe lo que envía el cliente)
    private OutputStream outputStream; // Stream de escritura (por aquí se envían los datos al cliente)

    private final String APIKey = "af0211bde65fb19415d99bf2a5b55c88"; // Mi clave para la API de OpenWeatherMap


    // Constructor que recibe y asigna a nuestro socket el creado por la otra clase.
    public ProcesadorElTiempo(Socket socketServicio) {

            this.socketServicio = socketServicio;

    }


    //Se realiza el procesamiento (Lo hace run para que se le pueda llamar concurrentemente)
    public void run(){

        // Array de bytes de Lectura de datos.
        byte [] datosRecibidos=new byte[27648];
        int bytesRecibidos = 0;

        // Array de bytes para enviar la respuesta.
        byte [] datosEnviar; 

        try {

                //Obtenemos los flujos de escritura/lectura
                inputStream = socketServicio.getInputStream();
                outputStream = socketServicio.getOutputStream();


                // ---- LECTURA DE DATOS DEL CLIENTE ----

                // Leemos el número de bytes recibidos con read.
                bytesRecibidos = inputStream.read(datosRecibidos);

                // Creamos un String a partir de un array de bytes de tamaño "bytesRecibidos":
                String peticion = new String(datosRecibidos,0,bytesRecibidos);

                // ---- PROCESADO DE LA ENTRADA ----

                String respuesta = null;

                // Si la petición es "HELLO", respondemos "OK" para confirmar la conexión.
                if( peticion.startsWith("HELLO") ) {
                    
                    respuesta = "OK";
                    System.out.println("Recibida petición HELLO del cliente. Devolviendo OK.");
                    
                // Si la petición es "HOY"+código_ciudad, mandamos los datos del tiempo actual para esa ciudad.
                } else if ( peticion.startsWith("HOY") ) {
                    
                    respuesta = peticion.substring(3);
                    System.out.println("Recibida petición HOY del cliente. Enviando tiempo actual de la ciudad #"+respuesta+".");

                    respuesta = getTiempoHoy(respuesta);

                // Si la petición es "DIA"+código_ciudad, mandamos la previsión de los próximos días para esa ciudad.
                } else if ( peticion.startsWith("DIA") ) {
                    
                    respuesta = peticion.substring(3);
                    System.out.println("Recibida petición DIA del cliente. Enviando pronóstico de la ciudad #"+respuesta+".");
                    
                    respuesta = getTiempoDias(respuesta);
                    
                // Si recibimos un mensaje "BYE", cerramos el socket con el cliente.
                } else if( peticion.startsWith("BYE") ) {
                    
                    respuesta = "BYE";
                    System.out.println("Recibida petición BYE del cliente. Cerrando conexión con él.");
                }

                // ---- ENVÍO DE LOS DATOS AL CLIENTE ----

                // Convertimos el String de respuesta en una array de bytes:
                datosEnviar = respuesta.getBytes();

                // Enviamos al cliente el JSON con la respuesta:
                outputStream.write(datosEnviar, 0, datosEnviar.length);
                
                
                if(peticion.startsWith("BYE")) { socketServicio.close(); }

        } catch (IOException e) {
                System.err.println("Error al obtener los flujos de entrada/salida.");
        }

    }

    // Busca en OpenWeather Map el tiempo de hoy para la ciudad escogida y descargamos los datos del JSON.
    private String getTiempoHoy(String IdCiudad) throws IOException {

        JSONObject json;  
        json = readJsonFromUrl("http://api.openweathermap.org/data/2.5/weather?id="+IdCiudad+"&appid="+APIKey);

        return json.toString();
    }

   // Busca en OpenWeather Map la predicción de 5 días para la ciudad escogida y descargamos los datos del JSON.
    private String getTiempoDias(String IdCiudad) throws IOException {

        JSONObject json;
        json = readJsonFromUrl("http://api.openweathermap.org/data/2.5/forecast/daily?id="+IdCiudad+"&cnt=3&appid="+APIKey);

        return json.toString();
    }

    // Facilita la lectura de objetos JSON
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    // Permite obtener objetos JSON directamente desde una URL
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
        
}
