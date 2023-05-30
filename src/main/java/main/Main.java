package main;

import processing.core.PApplet;
import processing.serial.*;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;


public class Main extends PApplet {

    static Serial serialPort;


    public static void main(String[] args) {
        PApplet.main(new String[]{Main.class.getName()});
    }

    public void settings(){
        size(500,500);
    }

    public void setup(){
        String puerto = "COM1";
        int baudRate = 9600;
        serialPort = new Serial(this, puerto, baudRate);
        serialPort.bufferUntil('\n');
    }

    public void draw(){

    }

    public void serialEvent(Serial port) {
        String datosRecibidos = port.readStringUntil('\n');
        if (datosRecibidos != null) {
            datosRecibidos = datosRecibidos.trim();
            println("Datos recibidos: " + datosRecibidos);
        }
    }

    public String POSTrequest(String url, String json) {
        try {
            URL page = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) page.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("accept", "application/json");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write(json);
            writer.flush();

            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            is.close();
            baos.close();
            os.close();
            connection.disconnect();
            return new String(baos.toByteArray(), "UTF-8");
        }catch (IOException ex){
            ex.printStackTrace();
            return "";
        }
    }



}
