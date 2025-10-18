/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.Arrays;

/**
 *
 * @author Nathan
 */
public class Usuarios {

    private String usuario;

    private char[] contra;
    private static final int maxIngresos = 20;

    public Usuarios(String usuario, char[] contra) {
        this.usuario = usuario;
        this.contra = contra.clone();
    }

    public String getUsuario() {
        return usuario;
    }

    public char[] getContra() {
        return contra.clone();
    }

    public void setUsuario(String nuevoUsuario) {
        this.usuario = nuevoUsuario;
    }

    public void setContra(char[] nuevaContra) {

        if (this.contra != null) {
            Arrays.fill(this.contra, '0');
        }
        this.contra = nuevaContra.clone();
    }

    public boolean confirmarcontra(char[] inputcontra) {
        return Arrays.equals(this.contra, inputcontra);
    }

    public static void limpiarContrasena(char[] password) {
        if (password != null) {
            Arrays.fill(password, '0');
        }
    }

    public void limpiarContrasenaAlmacenada() {
        limpiarContrasena(this.contra);
    }

    @Override
    protected void finalize() throws Throwable {
        limpiarContrasenaAlmacenada();
        super.finalize();
    }
}
