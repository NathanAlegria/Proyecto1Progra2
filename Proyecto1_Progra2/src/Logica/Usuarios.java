/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Usuarios {

    private String usuario;
    private char[] contra; 
    private int puntos;
    private String fechaCreacion;
    private boolean estado;

    public Usuarios(String usuario, char[] contra) {
        this.usuario = usuario;
        this.contra = Arrays.copyOf(contra, contra.length); 
        this.puntos = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.fechaCreacion = sdf.format(new Date());
        this.estado = true; 
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

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
