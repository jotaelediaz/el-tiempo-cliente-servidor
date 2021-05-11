/**
 *
 * @author jotaele
 */
package GUI;

import Cliente.ClienteElTiempo;
import static Cliente.ClienteElTiempo.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jotaele
 */
public class PrincipalView extends javax.swing.JFrame {

    ClienteElTiempo modelo;
    private ArrayList ArrayList;
    private String idCiudadActual = "6357709";
    
    public void showView(ClienteElTiempo m) {
        
        modelo = m;
        
        
        ServerTexto.setText(modelo.serverStatusText);
        
        
        this.setVisible(true);
        
        // Ocultamos el panel de tiempos al inicio.
        PanelHoy.setVisible(false);
        TituloCiudad.setVisible(false);
        BotonPrevision.setVisible(false);
        PanelDias.setVisible(false);

    }
    /**
     * Creates new form PrincipalView
     */
    public PrincipalView() {
        this.setTitle("El Tiempo Interactivo"); // Título de la ventana
        setResizable(false); // Para impedir que se pueda cambiar el tamaño de la ventana.
        initComponents();
    }
    
    
    private void ActualizarDatosHoy(String idCiudad) {
        
        // Ocultamos el texto de presentación y mostramos el panel de Hoy.
        Presentacion.setVisible(false);
        Presentacion1.setVisible(false);
        Presentacion2.setVisible(false);
        PanelHoy.setVisible(true);
        TituloCiudad.setVisible(true);
        BotonPrevision.setVisible(true);
        PanelDias.setVisible(false);
        
        // Creamos el Mensaje para el servidor con la ciudad elegida...
        String mensaje = "HOY"+idCiudad;
        
        String resp = null;
        
        // Mensaje de información de estado de la petición al servidor...
        serverStatusText = "Enviado mensaje de solicitud del tiempo actual (HOY"+idCiudad+")   ···  ";
        
        // Enviamos el mensaje al Servidor...
        try {
            resp = modelo.enviarMensaje(mensaje);    
        } catch (IOException ex) {
            Logger.getLogger(PrincipalView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Obtenemos del JSON recibido los datos útiles para nuestro programa.
        ArrayList<String> arr = modelo.ParsingDatosHoy(resp);
        
        // Pasamos las temperaturas de Kelvin a Celsius.
        double t_Act = (Double.parseDouble(arr.get(2))-273);
        double t_Min = (Double.parseDouble(arr.get(3))-273);
        double t_Max = (Double.parseDouble(arr.get(4))-273);
        
        // Pasamos las horas de amanecer y atardecer de Unix a String
        Double SegundosAmanecer = (Double.parseDouble(arr.get(7)));
        Date dateAmanecer = new Date((long) (SegundosAmanecer*1000L)); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat aman = new SimpleDateFormat("HH:mm"); // the format of your date
        aman.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String StringAmanecer = aman.format(dateAmanecer);
        
        Double SegundosAtardecer = (Double.parseDouble(arr.get(8)));
        Date dateAtardecer = new Date((long) (SegundosAtardecer*1000L)); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat atar = new SimpleDateFormat("HH:mm"); // the format of your date
        atar.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String StringAtardecer = atar.format(dateAtardecer);

        // Actualizamos los datos del panel con los obtenidos.
        TituloCiudad.setText(arr.get(0));                       // Nombre de la Ciudad
        PanelHoy.setTiempo(arr.get(1));                         // Icono del Tiempo Actual
        PanelHoy.setTemperatura(String.format("%.1f", t_Act));  // Temperatura Actual
        PanelHoy.setMinima(String.format("%.1f", t_Min));       // Temperatura Mínima
        PanelHoy.setMaxima(String.format("%.1f", t_Max));       // Temperatura Máxima
        PanelHoy.setHumedad(arr.get(5));                        // Humedad
        PanelHoy.setViento(arr.get(6));                         // Velocidad del Viento
        PanelHoy.setAmanecer(StringAmanecer);                   // Hora de amanecer
        PanelHoy.setAtardecer(StringAtardecer);                 // Hora de atardecer

        // Mensaje de información de estado de la petición al servidor...
        serverStatusText += "Respuesta recibida del servidor con los datos de "+arr.get(0)+"  (201)";
        ServerTexto.setText(serverStatusText);
    }
    
    // Actualiza los y envía un mensaje al servidor pidiendo los datos "DIA" de la
    // última ciudad solicitada.
    private void ActualizarDatosDias() {
        
        // Ocultamos el botón de previsión
        BotonPrevision.setVisible(false);
        PanelDias.setVisible(true);
        
        // Creamos el Mensaje para el servidor con la ciudad elegida...
        String mensaje = "DIA"+idCiudadActual;
        String resp = null;
        
        
        // Mensaje de información de estado de la petición al servidor...
        serverStatusText = "Enviado mensaje de solicitud del tiempo a tres días (DIA"+idCiudadActual+")   ···  ";
        
        
        // Enviamos el mensaje al Servidor...
        try {
            resp = modelo.enviarMensaje(mensaje);    
        } catch (IOException ex) {
            Logger.getLogger(PrincipalView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Obtenemos del JSON recibido los datos útiles para nuestro programa.
        ArrayList<String> arr = modelo.ParsingDatosDias(resp);
       
        // Obtenemos las fechas de predicción
        Double Segundos1 = (Double.parseDouble(arr.get(0)));
        Date date1 = new Date((long) (Segundos1*1000L)); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat aman1 = new SimpleDateFormat("dd-MM-yyyy"); // the format of your date
        aman1.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String StringDate1 = aman1.format(date1);
        
        Double Segundos2 = (Double.parseDouble(arr.get(4)));
        Date date2 = new Date((long) (Segundos2*1000L)); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat aman2 = new SimpleDateFormat("dd-MM-yyyy"); // the format of your date
        aman2.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String StringDate2 = aman2.format(date2);
        
        Double Segundos3 = (Double.parseDouble(arr.get(8)));
        Date date3 = new Date((long) (Segundos3*1000L)); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat aman3 = new SimpleDateFormat("dd-MM-yyyy"); // the format of your date
        aman3.setTimeZone(TimeZone.getTimeZone("GMT+1"));
        String StringDate3 = aman3.format(date3);
        
        // Pasamos las temperaturas de Kelvin a Celsius.
        
        double t_Min1 = (Double.parseDouble(arr.get(2))-273);
        double t_Max1 = (Double.parseDouble(arr.get(3))-273);
      
        double t_Min2 = (Double.parseDouble(arr.get(6))-273);
        double t_Max2 = (Double.parseDouble(arr.get(7))-273);

        double t_Min3 = (Double.parseDouble(arr.get(10))-273);
        double t_Max3 = (Double.parseDouble(arr.get(11))-273);
        
        // Actualizamos los datos del panel con los obtenidos.
        PanelDias.setFecha1(StringDate1);                       // Fecha Prevista 1
        PanelDias.setFecha2(StringDate2);                       // Fecha Prevista 2
        PanelDias.setFecha3(StringDate3);                       // Fecha Prevista 3
        PanelDias.setMinima1(String.format("%.1f", t_Min1));    // Temperatura Mínima 1
        PanelDias.setMaxima1(String.format("%.1f", t_Max1));    // Temperatura Máxima 1
        PanelDias.setMinima2(String.format("%.1f", t_Min2));    // Temperatura Mínima 2
        PanelDias.setMaxima2(String.format("%.1f", t_Max2));    // Temperatura Máxima 2
        PanelDias.setMinima3(String.format("%.1f", t_Min3));    // Temperatura Mínima 3
        PanelDias.setMaxima3(String.format("%.1f", t_Max3));    // Temperatura Máxima 3 
        PanelDias.setTiempo1(arr.get(1));                       // Icono del Tiempo 1
        PanelDias.setTiempo2(arr.get(5));                       // Icono del Tiempo 2
        PanelDias.setTiempo3(arr.get(9));                       // Icono del Tiempo 3
        
        // Mensaje de información de estado de la petición al servidor...
        serverStatusText += "Respuesta recibida del servidor con la predicción a tres días  (202)";
        ServerTexto.setText(serverStatusText);
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        PanelHoy = new GUI.TiempoHoyPanel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        TituloCiudad = new javax.swing.JLabel();
        BotonPrevision = new javax.swing.JButton();
        PanelDias = new GUI.TiempoDiasPanel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        ServerTexto = new javax.swing.JLabel();
        Presentacion = new javax.swing.JLabel();
        Presentacion1 = new javax.swing.JLabel();
        Presentacion2 = new javax.swing.JLabel();
        BLasPalmas = new javax.swing.JLabel();
        BTenerife = new javax.swing.JLabel();
        BMelilla = new javax.swing.JLabel();
        BCeuta = new javax.swing.JLabel();
        BSegovia = new javax.swing.JLabel();
        BAvila = new javax.swing.JLabel();
        BSalamanca = new javax.swing.JLabel();
        BZamora = new javax.swing.JLabel();
        BValladolid = new javax.swing.JLabel();
        BBurgos = new javax.swing.JLabel();
        BSoria = new javax.swing.JLabel();
        BPalencia = new javax.swing.JLabel();
        BLeon = new javax.swing.JLabel();
        BPontevedra = new javax.swing.JLabel();
        BACoruna = new javax.swing.JLabel();
        BOurense = new javax.swing.JLabel();
        BLugo = new javax.swing.JLabel();
        BOviedo = new javax.swing.JLabel();
        BSantander = new javax.swing.JLabel();
        BBilbao = new javax.swing.JLabel();
        BDonosti = new javax.swing.JLabel();
        BVitoria = new javax.swing.JLabel();
        BPamplona = new javax.swing.JLabel();
        BLogrono = new javax.swing.JLabel();
        BMadrid = new javax.swing.JLabel();
        BGuadalajara = new javax.swing.JLabel();
        BToledo = new javax.swing.JLabel();
        BCuenca = new javax.swing.JLabel();
        BAlbacete = new javax.swing.JLabel();
        BCiudadReal = new javax.swing.JLabel();
        BTeruel = new javax.swing.JLabel();
        BZaragoza = new javax.swing.JLabel();
        BHuesca = new javax.swing.JLabel();
        BLleida = new javax.swing.JLabel();
        BGirona = new javax.swing.JLabel();
        BBarcelona = new javax.swing.JLabel();
        BTarragona = new javax.swing.JLabel();
        BPalma = new javax.swing.JLabel();
        BCastellon = new javax.swing.JLabel();
        BValencia = new javax.swing.JLabel();
        BAlicante = new javax.swing.JLabel();
        BMurcia = new javax.swing.JLabel();
        BCaceres = new javax.swing.JLabel();
        BBadajoz = new javax.swing.JLabel();
        BCadiz = new javax.swing.JLabel();
        BHuelva = new javax.swing.JLabel();
        BCordoba = new javax.swing.JLabel();
        BSevilla = new javax.swing.JLabel();
        BJaen = new javax.swing.JLabel();
        BMalaga = new javax.swing.JLabel();
        BAlmeria = new javax.swing.JLabel();
        BGranada = new javax.swing.JLabel();
        Mapa = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(1190, 620));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });
        getContentPane().setLayout(null);

        PanelHoy.setBackground(new java.awt.Color(230, 230, 230));
        getContentPane().add(PanelHoy);
        PanelHoy.setBounds(670, 130, 490, 210);

        jLabel2.setFont(new java.awt.Font("Ubuntu", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("El Tiempo Interactivo");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(10, 10, 1170, 40);

        jSeparator2.setBackground(new java.awt.Color(222, 222, 222));
        jSeparator2.setForeground(new java.awt.Color(204, 202, 202));
        getContentPane().add(jSeparator2);
        jSeparator2.setBounds(10, 560, 1170, 20);

        TituloCiudad.setFont(new java.awt.Font("Ubuntu", 0, 25)); // NOI18N
        TituloCiudad.setForeground(new java.awt.Color(119, 119, 119));
        TituloCiudad.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TituloCiudad.setText("Hoy");
        getContentPane().add(TituloCiudad);
        TituloCiudad.setBounds(670, 90, 490, 30);

        BotonPrevision.setFont(new java.awt.Font("Ubuntu", 0, 15)); // NOI18N
        BotonPrevision.setText("Mostrar pronóstico de los próximos días");
        BotonPrevision.setContentAreaFilled(false);
        BotonPrevision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BotonPrevisionActionPerformed(evt);
            }
        });
        getContentPane().add(BotonPrevision);
        BotonPrevision.setBounds(670, 390, 490, 50);

        javax.swing.GroupLayout PanelDiasLayout = new javax.swing.GroupLayout(PanelDias);
        PanelDias.setLayout(PanelDiasLayout);
        PanelDiasLayout.setHorizontalGroup(
            PanelDiasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 490, Short.MAX_VALUE)
        );
        PanelDiasLayout.setVerticalGroup(
            PanelDiasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 170, Short.MAX_VALUE)
        );

        getContentPane().add(PanelDias);
        PanelDias.setBounds(670, 360, 490, 170);

        jSeparator3.setBackground(new java.awt.Color(222, 222, 222));
        jSeparator3.setForeground(new java.awt.Color(204, 202, 202));
        getContentPane().add(jSeparator3);
        jSeparator3.setBounds(10, 60, 1170, 20);

        jLabel4.setBackground(new java.awt.Color(222, 222, 222));
        jLabel4.setFont(new java.awt.Font("Ubuntu Light", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(185, 185, 185));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("JotaEle Díaz - UGR");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(980, 570, 170, 40);

        ServerTexto.setFont(new java.awt.Font("Ubuntu", 0, 14)); // NOI18N
        ServerTexto.setForeground(new java.awt.Color(128, 128, 128));
        ServerTexto.setText("Texto de estado del Servidor");
        getContentPane().add(ServerTexto);
        ServerTexto.setBounds(29, 570, 1000, 40);

        Presentacion.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        Presentacion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Presentacion.setText("el tiempo que hace en su capital en este momento.");
        getContentPane().add(Presentacion);
        Presentacion.setBounds(670, 330, 490, 30);

        Presentacion1.setFont(new java.awt.Font("Ubuntu", 0, 54)); // NOI18N
        Presentacion1.setForeground(new java.awt.Color(144, 144, 144));
        Presentacion1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Presentacion1.setText("¡Bienvenido!");
        getContentPane().add(Presentacion1);
        Presentacion1.setBounds(670, 220, 490, 50);

        Presentacion2.setFont(new java.awt.Font("Ubuntu", 0, 18)); // NOI18N
        Presentacion2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Presentacion2.setText("Selecciona  cualquier provincia en el mapa para conocer");
        getContentPane().add(Presentacion2);
        Presentacion2.setBounds(670, 300, 490, 30);

        BLasPalmas.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BLasPalmas.setForeground(new java.awt.Color(254, 254, 254));
        BLasPalmas.setText("Las Palmas");
        BLasPalmas.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BLasPalmas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BLasPalmasMouseClicked(evt);
            }
        });
        getContentPane().add(BLasPalmas);
        BLasPalmas.setBounds(130, 490, 60, 30);

        BTenerife.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BTenerife.setForeground(new java.awt.Color(254, 254, 254));
        BTenerife.setText("Tenerife");
        BTenerife.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BTenerife.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BTenerifeMouseClicked(evt);
            }
        });
        getContentPane().add(BTenerife);
        BTenerife.setBounds(70, 480, 49, 30);

        BMelilla.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BMelilla.setForeground(new java.awt.Color(254, 254, 254));
        BMelilla.setText("Melilla");
        BMelilla.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BMelilla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BMelillaMouseClicked(evt);
            }
        });
        getContentPane().add(BMelilla);
        BMelilla.setBounds(360, 480, 49, 30);

        BCeuta.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BCeuta.setForeground(new java.awt.Color(254, 254, 254));
        BCeuta.setText("Ceuta");
        BCeuta.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BCeuta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BCeutaMouseClicked(evt);
            }
        });
        getContentPane().add(BCeuta);
        BCeuta.setBounds(270, 460, 49, 30);

        BSegovia.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BSegovia.setForeground(new java.awt.Color(254, 254, 254));
        BSegovia.setText("Segovia");
        BSegovia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BSegovia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BSegoviaMouseClicked(evt);
            }
        });
        getContentPane().add(BSegovia);
        BSegovia.setBounds(290, 230, 50, 20);

        BAvila.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BAvila.setForeground(new java.awt.Color(254, 254, 254));
        BAvila.setText("Ávila");
        BAvila.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BAvila.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BAvilaMouseClicked(evt);
            }
        });
        getContentPane().add(BAvila);
        BAvila.setBounds(270, 250, 40, 20);

        BSalamanca.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BSalamanca.setForeground(new java.awt.Color(254, 254, 254));
        BSalamanca.setText("Salamanca");
        BSalamanca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BSalamanca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BSalamancaMouseClicked(evt);
            }
        });
        getContentPane().add(BSalamanca);
        BSalamanca.setBounds(220, 230, 70, 30);

        BZamora.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BZamora.setForeground(new java.awt.Color(254, 254, 254));
        BZamora.setText("Zamora");
        BZamora.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BZamora.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BZamoraMouseClicked(evt);
            }
        });
        getContentPane().add(BZamora);
        BZamora.setBounds(230, 190, 50, 20);

        BValladolid.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BValladolid.setForeground(new java.awt.Color(254, 254, 254));
        BValladolid.setText("Valladolid");
        BValladolid.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BValladolid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BValladolidMouseClicked(evt);
            }
        });
        getContentPane().add(BValladolid);
        BValladolid.setBounds(270, 200, 60, 30);

        BBurgos.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BBurgos.setForeground(new java.awt.Color(254, 254, 254));
        BBurgos.setText("Burgos");
        BBurgos.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BBurgos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BBurgosMouseClicked(evt);
            }
        });
        getContentPane().add(BBurgos);
        BBurgos.setBounds(310, 150, 50, 30);

        BSoria.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BSoria.setForeground(new java.awt.Color(254, 254, 254));
        BSoria.setText("Soria");
        BSoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BSoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BSoriaMouseClicked(evt);
            }
        });
        getContentPane().add(BSoria);
        BSoria.setBounds(360, 190, 40, 30);

        BPalencia.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BPalencia.setForeground(new java.awt.Color(254, 254, 254));
        BPalencia.setText("Palencia");
        BPalencia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BPalencia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BPalenciaMouseClicked(evt);
            }
        });
        getContentPane().add(BPalencia);
        BPalencia.setBounds(270, 170, 60, 20);

        BLeon.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BLeon.setForeground(new java.awt.Color(254, 254, 254));
        BLeon.setText("León");
        BLeon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BLeon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BLeonMouseClicked(evt);
            }
        });
        getContentPane().add(BLeon);
        BLeon.setBounds(230, 150, 50, 20);

        BPontevedra.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BPontevedra.setForeground(new java.awt.Color(254, 254, 254));
        BPontevedra.setText("Pontevedra");
        BPontevedra.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BPontevedra.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BPontevedraMouseClicked(evt);
            }
        });
        getContentPane().add(BPontevedra);
        BPontevedra.setBounds(110, 160, 70, 20);

        BACoruna.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BACoruna.setForeground(new java.awt.Color(254, 254, 254));
        BACoruna.setText("A Coruña");
        BACoruna.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BACoruna.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BACorunaMouseClicked(evt);
            }
        });
        getContentPane().add(BACoruna);
        BACoruna.setBounds(130, 130, 70, 20);

        BOurense.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BOurense.setForeground(new java.awt.Color(254, 254, 254));
        BOurense.setText("Ourense");
        BOurense.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BOurense.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BOurenseMouseClicked(evt);
            }
        });
        getContentPane().add(BOurense);
        BOurense.setBounds(170, 170, 50, 20);

        BLugo.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BLugo.setForeground(new java.awt.Color(254, 254, 254));
        BLugo.setText("Lugo");
        BLugo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BLugo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BLugoMouseClicked(evt);
            }
        });
        getContentPane().add(BLugo);
        BLugo.setBounds(180, 140, 40, 13);

        BOviedo.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BOviedo.setForeground(new java.awt.Color(254, 254, 254));
        BOviedo.setText("Oviedo");
        BOviedo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BOviedo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BOviedoMouseClicked(evt);
            }
        });
        getContentPane().add(BOviedo);
        BOviedo.setBounds(220, 120, 60, 13);

        BSantander.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BSantander.setForeground(new java.awt.Color(254, 254, 254));
        BSantander.setText("Santander");
        BSantander.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BSantander.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BSantanderMouseClicked(evt);
            }
        });
        getContentPane().add(BSantander);
        BSantander.setBounds(290, 120, 50, 20);

        BBilbao.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BBilbao.setForeground(new java.awt.Color(254, 254, 254));
        BBilbao.setText("Bilbao");
        BBilbao.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BBilbao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BBilbaoMouseClicked(evt);
            }
        });
        getContentPane().add(BBilbao);
        BBilbao.setBounds(350, 120, 50, 13);

        BDonosti.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BDonosti.setForeground(new java.awt.Color(254, 254, 254));
        BDonosti.setText("Donosti");
        BDonosti.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BDonosti.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BDonostiMouseClicked(evt);
            }
        });
        getContentPane().add(BDonosti);
        BDonosti.setBounds(380, 130, 60, 13);

        BVitoria.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BVitoria.setForeground(new java.awt.Color(254, 254, 254));
        BVitoria.setText("Vitoria");
        BVitoria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BVitoria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BVitoriaMouseClicked(evt);
            }
        });
        getContentPane().add(BVitoria);
        BVitoria.setBounds(350, 147, 50, 13);

        BPamplona.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BPamplona.setForeground(new java.awt.Color(254, 254, 254));
        BPamplona.setText("Pamplona");
        BPamplona.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BPamplona.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BPamplonaMouseClicked(evt);
            }
        });
        getContentPane().add(BPamplona);
        BPamplona.setBounds(390, 150, 50, 30);

        BLogrono.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BLogrono.setForeground(new java.awt.Color(254, 254, 254));
        BLogrono.setText("Logroño");
        BLogrono.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BLogrono.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BLogronoMouseClicked(evt);
            }
        });
        getContentPane().add(BLogrono);
        BLogrono.setBounds(350, 160, 50, 30);

        BMadrid.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BMadrid.setForeground(new java.awt.Color(254, 254, 254));
        BMadrid.setText("Madrid");
        BMadrid.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BMadrid.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BMadridMouseClicked(evt);
            }
        });
        getContentPane().add(BMadrid);
        BMadrid.setBounds(310, 250, 60, 30);

        BGuadalajara.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BGuadalajara.setForeground(new java.awt.Color(254, 254, 254));
        BGuadalajara.setText("Guadalajara");
        BGuadalajara.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BGuadalajara.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BGuadalajaraMouseClicked(evt);
            }
        });
        getContentPane().add(BGuadalajara);
        BGuadalajara.setBounds(340, 230, 60, 30);

        BToledo.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BToledo.setForeground(new java.awt.Color(254, 254, 254));
        BToledo.setText("Toledo");
        BToledo.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BToledo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BToledoMouseClicked(evt);
            }
        });
        getContentPane().add(BToledo);
        BToledo.setBounds(290, 280, 60, 30);

        BCuenca.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BCuenca.setForeground(new java.awt.Color(254, 254, 254));
        BCuenca.setText("Cuenca");
        BCuenca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BCuenca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BCuencaMouseClicked(evt);
            }
        });
        getContentPane().add(BCuenca);
        BCuenca.setBounds(370, 280, 60, 30);

        BAlbacete.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BAlbacete.setForeground(new java.awt.Color(254, 254, 254));
        BAlbacete.setText("Albacete");
        BAlbacete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BAlbacete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BAlbaceteMouseClicked(evt);
            }
        });
        getContentPane().add(BAlbacete);
        BAlbacete.setBounds(370, 330, 60, 18);

        BCiudadReal.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BCiudadReal.setForeground(new java.awt.Color(254, 254, 254));
        BCiudadReal.setText("Ciudad Real");
        BCiudadReal.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BCiudadReal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BCiudadRealMouseClicked(evt);
            }
        });
        getContentPane().add(BCiudadReal);
        BCiudadReal.setBounds(290, 330, 80, 18);

        BTeruel.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BTeruel.setForeground(new java.awt.Color(254, 254, 254));
        BTeruel.setText("Teruel");
        BTeruel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BTeruel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BTeruelMouseClicked(evt);
            }
        });
        getContentPane().add(BTeruel);
        BTeruel.setBounds(410, 240, 70, 30);

        BZaragoza.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BZaragoza.setForeground(new java.awt.Color(254, 254, 254));
        BZaragoza.setText("Zaragoza");
        BZaragoza.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BZaragoza.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BZaragozaMouseClicked(evt);
            }
        });
        getContentPane().add(BZaragoza);
        BZaragoza.setBounds(400, 200, 60, 20);

        BHuesca.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BHuesca.setForeground(new java.awt.Color(254, 254, 254));
        BHuesca.setText("Huesca");
        BHuesca.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BHuesca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BHuescaMouseClicked(evt);
            }
        });
        getContentPane().add(BHuesca);
        BHuesca.setBounds(440, 170, 60, 30);

        BLleida.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BLleida.setForeground(new java.awt.Color(254, 254, 254));
        BLleida.setText("Lleida");
        BLleida.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BLleida.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BLleidaMouseClicked(evt);
            }
        });
        getContentPane().add(BLleida);
        BLleida.setBounds(490, 180, 50, 20);

        BGirona.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BGirona.setForeground(new java.awt.Color(254, 254, 254));
        BGirona.setText("Girona");
        BGirona.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BGirona.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BGironaMouseClicked(evt);
            }
        });
        getContentPane().add(BGirona);
        BGirona.setBounds(560, 170, 60, 30);

        BBarcelona.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BBarcelona.setForeground(new java.awt.Color(254, 254, 254));
        BBarcelona.setText("Barcelona");
        BBarcelona.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BBarcelona.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BBarcelonaMouseClicked(evt);
            }
        });
        getContentPane().add(BBarcelona);
        BBarcelona.setBounds(530, 200, 60, 30);

        BTarragona.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BTarragona.setForeground(new java.awt.Color(254, 254, 254));
        BTarragona.setText("Tarragona");
        BTarragona.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BTarragona.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BTarragonaMouseClicked(evt);
            }
        });
        getContentPane().add(BTarragona);
        BTarragona.setBounds(480, 220, 60, 40);

        BPalma.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BPalma.setForeground(new java.awt.Color(254, 254, 254));
        BPalma.setText("Palma");
        BPalma.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BPalma.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BPalmaMouseClicked(evt);
            }
        });
        getContentPane().add(BPalma);
        BPalma.setBounds(550, 290, 60, 30);

        BCastellon.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BCastellon.setForeground(new java.awt.Color(254, 254, 254));
        BCastellon.setText("Castellón");
        BCastellon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BCastellon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BCastellonMouseClicked(evt);
            }
        });
        getContentPane().add(BCastellon);
        BCastellon.setBounds(450, 260, 60, 40);

        BValencia.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BValencia.setForeground(new java.awt.Color(254, 254, 254));
        BValencia.setText("Valencia");
        BValencia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BValencia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BValenciaMouseClicked(evt);
            }
        });
        getContentPane().add(BValencia);
        BValencia.setBounds(430, 300, 60, 40);

        BAlicante.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BAlicante.setForeground(new java.awt.Color(254, 254, 254));
        BAlicante.setText("Alicante");
        BAlicante.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BAlicante.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BAlicanteMouseClicked(evt);
            }
        });
        getContentPane().add(BAlicante);
        BAlicante.setBounds(430, 348, 60, 30);

        BMurcia.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BMurcia.setForeground(new java.awt.Color(254, 254, 254));
        BMurcia.setText("Murcia");
        BMurcia.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BMurcia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BMurciaMouseClicked(evt);
            }
        });
        getContentPane().add(BMurcia);
        BMurcia.setBounds(390, 370, 49, 18);

        BCaceres.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BCaceres.setForeground(new java.awt.Color(254, 254, 254));
        BCaceres.setText("Cáceres");
        BCaceres.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BCaceres.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BCaceresMouseClicked(evt);
            }
        });
        getContentPane().add(BCaceres);
        BCaceres.setBounds(220, 280, 60, 40);

        BBadajoz.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BBadajoz.setForeground(new java.awt.Color(254, 254, 254));
        BBadajoz.setText("Badajoz");
        BBadajoz.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BBadajoz.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BBadajozMouseClicked(evt);
            }
        });
        getContentPane().add(BBadajoz);
        BBadajoz.setBounds(210, 338, 60, 30);

        BCadiz.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BCadiz.setForeground(new java.awt.Color(254, 254, 254));
        BCadiz.setText("Cádiz");
        BCadiz.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BCadiz.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BCadizMouseClicked(evt);
            }
        });
        getContentPane().add(BCadiz);
        BCadiz.setBounds(230, 440, 60, 20);

        BHuelva.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BHuelva.setForeground(new java.awt.Color(254, 254, 254));
        BHuelva.setText("Huelva");
        BHuelva.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BHuelva.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BHuelvaMouseClicked(evt);
            }
        });
        getContentPane().add(BHuelva);
        BHuelva.setBounds(200, 390, 50, 18);

        BCordoba.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BCordoba.setForeground(new java.awt.Color(254, 254, 254));
        BCordoba.setText("Córdoba");
        BCordoba.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BCordoba.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BCordobaMouseClicked(evt);
            }
        });
        getContentPane().add(BCordoba);
        BCordoba.setBounds(270, 370, 50, 18);

        BSevilla.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BSevilla.setForeground(new java.awt.Color(254, 254, 254));
        BSevilla.setText("Sevilla");
        BSevilla.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BSevilla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BSevillaMouseClicked(evt);
            }
        });
        getContentPane().add(BSevilla);
        BSevilla.setBounds(240, 400, 50, 18);

        BJaen.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BJaen.setForeground(new java.awt.Color(254, 254, 254));
        BJaen.setText("Jaén");
        BJaen.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BJaen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BJaenMouseClicked(evt);
            }
        });
        getContentPane().add(BJaen);
        BJaen.setBounds(330, 370, 40, 18);

        BMalaga.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BMalaga.setForeground(new java.awt.Color(254, 254, 254));
        BMalaga.setText("Málaga");
        BMalaga.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BMalaga.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BMalagaMouseClicked(evt);
            }
        });
        getContentPane().add(BMalaga);
        BMalaga.setBounds(280, 430, 49, 18);

        BAlmeria.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BAlmeria.setForeground(new java.awt.Color(254, 254, 254));
        BAlmeria.setText("Almería");
        BAlmeria.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BAlmeria.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BAlmeriaMouseClicked(evt);
            }
        });
        getContentPane().add(BAlmeria);
        BAlmeria.setBounds(370, 410, 49, 18);

        BGranada.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        BGranada.setForeground(new java.awt.Color(254, 254, 254));
        BGranada.setText("Granada");
        BGranada.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        BGranada.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BGranadaMouseClicked(evt);
            }
        });
        getContentPane().add(BGranada);
        BGranada.setBounds(310, 410, 50, 18);

        Mapa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GUI/mapatiempo.png"))); // NOI18N
        getContentPane().add(Mapa);
        Mapa.setBounds(30, 90, 612, 440);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BotonPrevisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BotonPrevisionActionPerformed
        ActualizarDatosDias();
    }//GEN-LAST:event_BotonPrevisionActionPerformed

    private void BGranadaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BGranadaMouseClicked
        idCiudadActual = "2517117";
        ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BGranadaMouseClicked

    private void BAlmeriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BAlmeriaMouseClicked
       idCiudadActual = "2521886";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BAlmeriaMouseClicked

    private void BMalagaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BMalagaMouseClicked
        idCiudadActual = "2514256";
        ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BMalagaMouseClicked

    private void BJaenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BJaenMouseClicked
       idCiudadActual = "2516395";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BJaenMouseClicked

    private void BCordobaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BCordobaMouseClicked
       idCiudadActual = "2519240";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BCordobaMouseClicked

    private void BSevillaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BSevillaMouseClicked
       idCiudadActual = "2510911";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BSevillaMouseClicked

    private void BHuelvaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BHuelvaMouseClicked
       idCiudadActual = "2516548";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BHuelvaMouseClicked

    private void BBadajozMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BBadajozMouseClicked
       idCiudadActual = "2521420";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BBadajozMouseClicked

    private void BCaceresMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BCaceresMouseClicked
       idCiudadActual = "2520611";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BCaceresMouseClicked

    private void BCadizMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BCadizMouseClicked
       idCiudadActual = "2520600";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BCadizMouseClicked

    private void BMurciaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BMurciaMouseClicked
       idCiudadActual = "2513416";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BMurciaMouseClicked

    private void BAlicanteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BAlicanteMouseClicked
       idCiudadActual = "2521978";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BAlicanteMouseClicked

    private void BValenciaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BValenciaMouseClicked
       idCiudadActual = "2509954";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BValenciaMouseClicked

    private void BCastellonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BCastellonMouseClicked
       idCiudadActual = "2519752";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BCastellonMouseClicked

    private void BPalmaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BPalmaMouseClicked
       idCiudadActual = "2512989";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BPalmaMouseClicked

    private void BTarragonaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BTarragonaMouseClicked
       idCiudadActual = "3108288";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BTarragonaMouseClicked

    private void BBarcelonaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BBarcelonaMouseClicked
       idCiudadActual = "3128760";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BBarcelonaMouseClicked

    private void BGironaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BGironaMouseClicked
       idCiudadActual = "3121456";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BGironaMouseClicked

    private void BLleidaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BLleidaMouseClicked
        idCiudadActual = "3118514";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BLleidaMouseClicked

    private void BHuescaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BHuescaMouseClicked
       idCiudadActual = "3120514";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BHuescaMouseClicked

    private void BZaragozaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BZaragozaMouseClicked
       idCiudadActual = "3104324";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BZaragozaMouseClicked

    private void BTeruelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BTeruelMouseClicked
       idCiudadActual = "3108126";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BTeruelMouseClicked

    private void BCiudadRealMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BCiudadRealMouseClicked
       idCiudadActual = "2519402";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BCiudadRealMouseClicked

    private void BAlbaceteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BAlbaceteMouseClicked
       idCiudadActual = "2522258";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BAlbaceteMouseClicked

    private void BCuencaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BCuencaMouseClicked
       idCiudadActual = "3124132";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BCuencaMouseClicked

    private void BToledoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BToledoMouseClicked
       idCiudadActual = "2510409";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BToledoMouseClicked

    private void BMadridMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BMadridMouseClicked
       idCiudadActual = "3117735";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BMadridMouseClicked

    private void BGuadalajaraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BGuadalajaraMouseClicked
       idCiudadActual = "3121070";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BGuadalajaraMouseClicked

    private void BLogronoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BLogronoMouseClicked
       idCiudadActual = "3118150";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BLogronoMouseClicked

    private void BPamplonaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BPamplonaMouseClicked
       idCiudadActual = "3114472";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BPamplonaMouseClicked

    private void BVitoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BVitoriaMouseClicked
       idCiudadActual = "3104499";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BVitoriaMouseClicked

    private void BDonostiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BDonostiMouseClicked
       idCiudadActual = "3110044";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BDonostiMouseClicked

    private void BBilbaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BBilbaoMouseClicked
       idCiudadActual = "3128026";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BBilbaoMouseClicked

    private void BSantanderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BSantanderMouseClicked
       idCiudadActual = "3109718";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BSantanderMouseClicked

    private void BOviedoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BOviedoMouseClicked
       idCiudadActual = "3114711";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BOviedoMouseClicked

    private void BLugoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BLugoMouseClicked
       idCiudadActual = "3117814";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BLugoMouseClicked

    private void BOurenseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BOurenseMouseClicked
       idCiudadActual = "3114965";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BOurenseMouseClicked

    private void BACorunaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BACorunaMouseClicked
       idCiudadActual = "3119841";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BACorunaMouseClicked

    private void BPontevedraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BPontevedraMouseClicked
       idCiudadActual = "3113209";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BPontevedraMouseClicked

    private void BLeonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BLeonMouseClicked
       idCiudadActual = "3118532";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BLeonMouseClicked

    private void BPalenciaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BPalenciaMouseClicked
       idCiudadActual = "3114531";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BPalenciaMouseClicked

    private void BSoriaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BSoriaMouseClicked
       idCiudadActual = "3108681";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BSoriaMouseClicked

    private void BBurgosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BBurgosMouseClicked
       idCiudadActual = "3127461";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BBurgosMouseClicked

    private void BValladolidMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BValladolidMouseClicked
       idCiudadActual = "3106672";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BValladolidMouseClicked

    private void BZamoraMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BZamoraMouseClicked
       idCiudadActual = "3104342";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BZamoraMouseClicked

    private void BSalamancaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BSalamancaMouseClicked
       idCiudadActual = "3111108";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BSalamancaMouseClicked

    private void BAvilaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BAvilaMouseClicked
       idCiudadActual = "3129136";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BAvilaMouseClicked

    private void BSegoviaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BSegoviaMouseClicked
       idCiudadActual = "3109256";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BSegoviaMouseClicked

    private void BCeutaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BCeutaMouseClicked
       idCiudadActual = "6362987";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BCeutaMouseClicked

    private void BMelillaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BMelillaMouseClicked
       idCiudadActual = "2513947";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BMelillaMouseClicked

    private void BTenerifeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BTenerifeMouseClicked
       idCiudadActual = "6360638";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BTenerifeMouseClicked

    private void BLasPalmasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BLasPalmasMouseClicked
       idCiudadActual = "6360186";
       ActualizarDatosHoy(idCiudadActual);
    }//GEN-LAST:event_BLasPalmasMouseClicked

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            modelo.enviarMensaje("BYE");
        } catch (IOException ex) {
            Logger.getLogger(PrincipalView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PrincipalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PrincipalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PrincipalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrincipalView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                    new PrincipalView().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BACoruna;
    private javax.swing.JLabel BAlbacete;
    private javax.swing.JLabel BAlicante;
    private javax.swing.JLabel BAlmeria;
    private javax.swing.JLabel BAvila;
    private javax.swing.JLabel BBadajoz;
    private javax.swing.JLabel BBarcelona;
    private javax.swing.JLabel BBilbao;
    private javax.swing.JLabel BBurgos;
    private javax.swing.JLabel BCaceres;
    private javax.swing.JLabel BCadiz;
    private javax.swing.JLabel BCastellon;
    private javax.swing.JLabel BCeuta;
    private javax.swing.JLabel BCiudadReal;
    private javax.swing.JLabel BCordoba;
    private javax.swing.JLabel BCuenca;
    private javax.swing.JLabel BDonosti;
    private javax.swing.JLabel BGirona;
    private javax.swing.JLabel BGranada;
    private javax.swing.JLabel BGuadalajara;
    private javax.swing.JLabel BHuelva;
    private javax.swing.JLabel BHuesca;
    private javax.swing.JLabel BJaen;
    private javax.swing.JLabel BLasPalmas;
    private javax.swing.JLabel BLeon;
    private javax.swing.JLabel BLleida;
    private javax.swing.JLabel BLogrono;
    private javax.swing.JLabel BLugo;
    private javax.swing.JLabel BMadrid;
    private javax.swing.JLabel BMalaga;
    private javax.swing.JLabel BMelilla;
    private javax.swing.JLabel BMurcia;
    private javax.swing.JLabel BOurense;
    private javax.swing.JLabel BOviedo;
    private javax.swing.JLabel BPalencia;
    private javax.swing.JLabel BPalma;
    private javax.swing.JLabel BPamplona;
    private javax.swing.JLabel BPontevedra;
    private javax.swing.JLabel BSalamanca;
    private javax.swing.JLabel BSantander;
    private javax.swing.JLabel BSegovia;
    private javax.swing.JLabel BSevilla;
    private javax.swing.JLabel BSoria;
    private javax.swing.JLabel BTarragona;
    private javax.swing.JLabel BTenerife;
    private javax.swing.JLabel BTeruel;
    private javax.swing.JLabel BToledo;
    private javax.swing.JLabel BValencia;
    private javax.swing.JLabel BValladolid;
    private javax.swing.JLabel BVitoria;
    private javax.swing.JLabel BZamora;
    private javax.swing.JLabel BZaragoza;
    private javax.swing.JButton BotonPrevision;
    private javax.swing.JLabel Mapa;
    private GUI.TiempoDiasPanel PanelDias;
    private GUI.TiempoHoyPanel PanelHoy;
    private javax.swing.JLabel Presentacion;
    private javax.swing.JLabel Presentacion1;
    private javax.swing.JLabel Presentacion2;
    private javax.swing.JLabel ServerTexto;
    private javax.swing.JLabel TituloCiudad;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    // End of variables declaration//GEN-END:variables
}
