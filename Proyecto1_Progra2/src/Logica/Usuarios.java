/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Nathan
 */

public class Usuarios {

    private String usuario;
    private char[] contra;
    private int puntos;
    private String fechaCreacion;
    private boolean estado;
    
    // CAMBIO 1: Lista para guardar los logs de juegos individuales
    private ArrayList<String> logDeMisJuegos; 

    public Usuarios(String usuario, char[] contra) {
        this.usuario = usuario;
        this.contra = Arrays.copyOf(contra, contra.length);
        this.puntos = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.fechaCreacion = sdf.format(new Date());
        this.estado = true;
        
        // CAMBIO 2: Inicializar la lista de logs
        this.logDeMisJuegos = new ArrayList<>(); 
    }

    public static void limpiarContrasena(char[] array) {
        if (array != null) {
            Arrays.fill(array, ' ');
        }
    }

    public String getUsuario() {
        return usuario;
    }

    public char[] getContrasena() {
        return contra;
    }

    public void setContrasena(char[] nuevaContrasena) {
        limpiarContrasena(this.contra);
        this.contra = Arrays.copyOf(nuevaContrasena, nuevaContrasena.length);
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }
    
    // CAMBIO 3: Método para sumar puntos fácilmente
    public void sumarPuntos(int puntosASumar) {
        this.puntos += puntosASumar;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
    
    // CAMBIO 4: Getter para los logs
    public ArrayList<String> getLogDeMisJuegos() {
        return logDeMisJuegos;
    }
    
    // CAMBIO 5: Método para agregar un log
    public void agregarLog(String log) {
        this.logDeMisJuegos.add(log);
    }
}