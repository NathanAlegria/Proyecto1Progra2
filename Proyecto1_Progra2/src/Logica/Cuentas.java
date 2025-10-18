/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Nathan
 */
public class Cuentas {

    private ArrayList<Usuarios> listaUsuarios = new ArrayList<>();
    private static final int LARGO_CONTRA = 5;
    

    public Cuentas() {
        listaUsuarios = new ArrayList<>();
    }
    

    private boolean esAlfanumerico(char c) {
        return (c >= 'a' && c <= 'z') || 
               (c >= 'A' && c <= 'Z') || 
               (c >= '0' && c <= '9');
    }

    public boolean registrarUsuario(String username, char[] passwordChars) {
        String password = new String(passwordChars).trim();
        username = username != null ? username.trim() : "";

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: El nombre de usuario está vacío", "Información", JOptionPane.INFORMATION_MESSAGE);
            Usuarios.limpiarContrasena(passwordChars);
            return false;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Error: La contraseña está vacía", "Información", JOptionPane.INFORMATION_MESSAGE);
            Usuarios.limpiarContrasena(passwordChars);
            return false;
        }

        if (password.length() != LARGO_CONTRA) {
            JOptionPane.showMessageDialog(null,
                    "Error: La contraseña debe tener EXACTAMENTE " + LARGO_CONTRA + " caracteres. Actual: " + password.length(),
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            Usuarios.limpiarContrasena(passwordChars);
            return false;
        }

        boolean tieneEspecial = false;
        for (char c : passwordChars) {
            if (!esAlfanumerico(c)) {
                tieneEspecial = true;
                break;
            }
        }

        if (!tieneEspecial) {
            JOptionPane.showMessageDialog(null,
                    "Error: La contraseña debe contener por lo menos 1 caracter especial (símbolo, puntuación, etc.).",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            Usuarios.limpiarContrasena(passwordChars);
            return false;
        }

        for (Usuarios user : listaUsuarios) {
            if (user.getUsuario().equalsIgnoreCase(username)) {
                JOptionPane.showMessageDialog(null, "Error el usuario: " + username + " Ya existe", "Información", JOptionPane.INFORMATION_MESSAGE);
                Usuarios.limpiarContrasena(passwordChars);
                return false;
            }
        }

        listaUsuarios.add(new Usuarios(username, passwordChars));

        JOptionPane.showMessageDialog(null, "ÉXITO: Usuario '" + username + "' registrado.\nTotal usuarios: " + listaUsuarios.size(), "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

        Usuarios.limpiarContrasena(passwordChars);
        return true;
    }

    public boolean verificarCredenciales(String username, char[] passwordChars) {
        username = username != null ? username.trim() : null;

        if (username == null || passwordChars == null) {
            JOptionPane.showMessageDialog(null, "Error: Credenciales nulas", "Información", JOptionPane.INFORMATION_MESSAGE);
            Usuarios.limpiarContrasena(passwordChars);
            return false;
        }

        for (Usuarios user : listaUsuarios) {
            if (user.getUsuario().equals(username)) {
                if (user.confirmarcontra(passwordChars)) {
                    JOptionPane.showMessageDialog(null, "Exito: Credenciales de: " + username, "Información", JOptionPane.INFORMATION_MESSAGE);
                    Usuarios.limpiarContrasena(passwordChars);
                    return true;
                }
            }
        }

        JOptionPane.showMessageDialog(null, "Error Credenciales inválidas.", "Información", JOptionPane.INFORMATION_MESSAGE);
        Usuarios.limpiarContrasena(passwordChars);
        return false;
    }

    public Usuarios buscarUsuario(String username) {
        if (username == null) {
            return null;
        }

        for (Usuarios user : listaUsuarios) {
            if (user.getUsuario().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public int getNumUsuarios() {
        return listaUsuarios.size();
    }
}
