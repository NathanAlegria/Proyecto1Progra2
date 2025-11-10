/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;

/**
 *
 * @author Nathan
 */

public class Usuarios implements Serializable {
 
    private String usuario;
    private char[] contra;
    private int puntos;
    private String fechaCreacion;
    private boolean estado;
    
    private ArrayList<String> logDeMisJuegos; 

    public Usuarios(String usuario, String password) {
        this.usuario = usuario;
        this.contra = password.toCharArray(); 
        
        this.puntos = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.fechaCreacion = sdf.format(new Date());
        this.estado = true;
        this.logDeMisJuegos = new ArrayList<>(); 
    }
    
    public Usuarios(String usuario, char[] passwordArray) {
        this.usuario = usuario;
        this.contra = Arrays.copyOf(passwordArray, passwordArray.length);
        
        this.puntos = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.fechaCreacion = sdf.format(new Date());
        this.estado = true;
        this.logDeMisJuegos = new ArrayList<>(); 
        
        limpiarContrasena(passwordArray);
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
        return Arrays.copyOf(contra, contra.length); 
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
    
    public ArrayList<String> getLogDeMisJuegos() {
        return logDeMisJuegos;
    }
    
    public void agregarLog(String log) {
        this.logDeMisJuegos.add(log);
    }
}