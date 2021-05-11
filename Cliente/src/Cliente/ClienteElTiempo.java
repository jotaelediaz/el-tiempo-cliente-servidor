// ClienteElTiempo
// (CC) JotaEle, 2016

// Crea un Cliente con su interfaz correspondiente para hacer peticiones
// sobre el tiempo atmosférico en España usando un puerto especificado por nosotros 
// y establececiendo una conexión con el Servidor mediante el uso de Sockets TCP.

// Es importante tener en cuenta que el cliente no se puede ejecutar si no
// lanzamos antes el servidor para que esté escuchando a la espera de peticiones.

package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import GUI.*;
import java.util.ArrayList;
import org.json.*;

public class ClienteElTiempo {
    
    // -- INTERFAZ DE USUARIO --
    
    public static PrincipalView vistaPrincipal;
    public static final ClienteElTiempo instance = new ClienteElTiempo();
    
    public static String serverStatusText = " ... ";   // Texto con información sobre peticiones.
        
    // -- IDENTIFICACIÓN DEL SERVIDOR Y PUERTO AL CONECTARSE ---
        
    private static final String host = "localhost";   // Nombre del host donde se ejecuta el servidor.
    private static final int port = 8989;             // Puerto en el que espera el servidor.
    
    
    public static void main(String[] args) {

        try {

            // Envíamos el mensaje inicial de conexión "HELLO"
            // Es importante notar que por cada petición se crea
            // y cierra un socket con el servidor.
            String respuesta = enviarMensaje("HELLO");
            
            if(respuesta.startsWith("OK")) {
                serverStatusText = "Enviado mensaje de conexión al servidor (HELLO)   ···   ";
                serverStatusText += "Conexión TCP establecida en el puerto "+port+" y en la dirección '"+host+"'  (OK)";
                
                // Creamos la ventana de la interfaz
                vistaPrincipal = new PrincipalView();
                vistaPrincipal.showView(instance);
            }
            
            // Excepciones:
            } catch (UnknownHostException e) {
                    System.err.println("Error: Nombre de host no encontrado.");
            } catch (IOException e) {
                    System.err.println("Error al abrir el socket. ¿Has comprobado que el servidor esté ejecutándose?");
            }   
    }


    // Envía un mensaje al servidor y devuelve la respuesta obtenida de él.
    public static String enviarMensaje(String mensaje) throws IOException {
        
        Socket socketServicio;  // Socket para la conexión TCP
        
        // Establecemos que el socket que se conecte a "host" en el "port".
        // Así, tendremos conectado mediante un pipe ambos procesos.
        socketServicio = new Socket (host,port);
        
        byte []bufferEnvio;
        byte []bufferRecepcion= new byte[27648];
        int bytesLeidos = 0;
        
        // Establecemos cuales serán los flujos donde el cliente enviará
        // y recibirá resultados del servidor mediante InputStream y OutputStream.
        InputStream inputStream = socketServicio.getInputStream();
        OutputStream outputStream = socketServicio.getOutputStream();

        // --- ENVÍO DE DATOS AL SERVIDOR ----

        // Para enviar un string por OutputStream hay que pasarlo a un array de bytes.
        // De entrada, enviamos el mensaje "HELLO" al servidor.
        bufferEnvio = mensaje.getBytes();

        PrintWriter outPrinter = new PrintWriter(socketServicio.getOutputStream(),true);

        // Enviamos el array por el outputStream;
        outputStream.write(bufferEnvio,0,bufferEnvio.length);

        // Aunque le indiquemos a TCP que queremos enviar varios arrays de bytes, sólo
        // los enviará efectivamente cuando considere que tiene suficientes datos que enviar...
        // Podemos usar "flush()" para obligar a TCP a que no espere para hacer el envío:
        outputStream.flush();
       
        // ----- RESPUESTA DEL SERVIDOR -----

        BufferedReader inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));

        // Leemos la respuesta del servidor. Para ello le pasamos un array de bytes, que 
        //intentará rellenar. El método "read(...)" devolverá el número de bytes leídos.
        bytesLeidos = inputStream.read(bufferRecepcion);

        // Transformamos la respuesta a String
        String respuesta = new String(bufferRecepcion);
        
        
        // Una vez terminado el servicio, cerramos el socket 
        //(automáticamente se cierran el inputStream y el outputStream)
        socketServicio.close();
        
        return respuesta;
    }
    
    // Parsea el JSON recibido del servidor y lo transforma en un Array con los datos útiles.
    public ArrayList<String> ParsingDatosHoy(String datos) {
        
        // Creamos un array que contendrá, en orden, los datos que necesitamos.
        ArrayList<String> datosTiempo = new ArrayList();

        // Extraemos los diferentes sub-JSON que nos harán falta...
        JSONObject json = new JSONObject(datos);
        JSONArray weatherA = json.getJSONArray("weather");
        JSONObject weather = weatherA.getJSONObject(0);
        JSONObject main = json.getJSONObject("main");
        JSONObject wind = json.getJSONObject("wind");
        JSONObject sys = json.getJSONObject("sys");
        
        // Extraemos los datos necesarios del JSON
        datosTiempo.add(json.getString("name"));                       // Nombre de la Ciudad
        datosTiempo.add(weather.getString("icon"));                    // Icono del Tiempo Actual
        datosTiempo.add(Double.toString(main.getDouble("temp")));      // Temperatura Actual
        datosTiempo.add(Double.toString(main.getDouble("temp_min")));  // Temperatura Mínima
        datosTiempo.add(Double.toString(main.getDouble("temp_max")));  // Temperatura Máxima
        datosTiempo.add(Integer.toString(main.getInt("humidity")));    // Humedad
        datosTiempo.add(Double.toString(wind.getDouble("speed")));     // Velocidad del Viento
        datosTiempo.add(Integer.toString(sys.getInt("sunrise")));      // Hora de amanecer
        datosTiempo.add(Integer.toString(sys.getInt("sunset")));       // Hora de atardecer
        
        return datosTiempo;
    }
    
    // Parsea el JSON recibido del servidor y lo transforma en un Array con los datos útiles.
    public ArrayList<String> ParsingDatosDias(String datos) {
        
        // Creamos un array que contendrá, en orden, los datos que necesitamos.
        ArrayList<String> datosTiempo = new ArrayList();

        // Extraemos los diferentes sub-JSON que nos harán falta...
        JSONObject json = new JSONObject(datos);
        JSONArray listA = json.getJSONArray("list");
        
        JSONObject list1 = listA.getJSONObject(0);
        JSONArray weatherA1 = list1.getJSONArray("weather");
        JSONObject weather1 = weatherA1.getJSONObject(0);
        JSONObject temp1 = list1.getJSONObject("temp");

        JSONObject list2 = listA.getJSONObject(1);
        JSONArray weatherA2 = list2.getJSONArray("weather");
        JSONObject weather2 = weatherA2.getJSONObject(0);
        JSONObject temp2 = list2.getJSONObject("temp");
        
        JSONObject list3 = listA.getJSONObject(2);
        JSONArray weatherA3 = list3.getJSONArray("weather");
        JSONObject weather3 = weatherA3.getJSONObject(0);
        JSONObject temp3 = list3.getJSONObject("temp"); 
        
        // Extraemos los datos necesarios del JSON
        datosTiempo.add(Integer.toString(list1.getInt("dt")));     // Fecha de Predicción
        datosTiempo.add(weather1.getString("icon"));               // Icono del Tiempo
        datosTiempo.add(Double.toString(temp1.getDouble("min")));  // Temperatura Mínima
        datosTiempo.add(Double.toString(temp1.getDouble("max")));  // Temperatura Máxima
        
        datosTiempo.add(Integer.toString(list2.getInt("dt")));     // Fecha de Predicción
        datosTiempo.add(weather2.getString("icon"));               // Icono del Tiempo
        datosTiempo.add(Double.toString(temp2.getDouble("min")));  // Temperatura Mínima
        datosTiempo.add(Double.toString(temp2.getDouble("max")));  // Temperatura Máxima

        datosTiempo.add(Integer.toString(list3.getInt("dt")));     // Fecha de Predicción
        datosTiempo.add(weather3.getString("icon"));               // Icono del Tiempo
        datosTiempo.add(Double.toString(temp3.getDouble("min")));  // Temperatura Mínima
        datosTiempo.add(Double.toString(temp3.getDouble("max")));  // Temperatura Máxima
        
        return datosTiempo;
    }
}