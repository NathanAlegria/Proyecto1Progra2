/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;
/**
 *
 * @author Nathan
 */
public class Cuentas {

    private static Cuentas instancia;
    private ArrayList<Usuarios> listaUsuarios;

    private Cuentas() {
        this.listaUsuarios = new ArrayList<>();
    }

    public static Cuentas getInstance() {
        if (instancia == null) {
            instancia = new Cuentas();
        }
        return instancia;
    }

    public boolean registrarUsuario(String usuario, char[] contrasena) {
        if (buscarUsuario(usuario) != null) {
            JOptionPane.showMessageDialog(null, "El nombre de usuario ya existe.", "Error de Registro", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (contrasena.length != 5) {
            JOptionPane.showMessageDialog(null, "La contraseña debe tener exactamente 5 caracteres.", "Error de Contraseña", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        Usuarios nuevoUsuario = new Usuarios(usuario, contrasena);
        listaUsuarios.add(nuevoUsuario);
        JOptionPane.showMessageDialog(null, "Usuario creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }

    public boolean verificarCredenciales(String usuario, char[] contrasena) {
        Usuarios user = buscarUsuario(usuario);
        if (user == null || !user.getEstado()) {
            return false;
        }

        char[] storedPass = user.getContrasena();
        boolean match = Arrays.equals(storedPass, contrasena);
        
        Usuarios.limpiarContrasena(storedPass); 
        return match;
    }

    public Usuarios buscarUsuario(String usuario) {
        for (Usuarios user : listaUsuarios) {
            if (user.getUsuario().equalsIgnoreCase(usuario)) {
                return user;
            }
        }
        return null;
    }

    public boolean cambiarContrasena(String usuario, char[] contrasenaAntigua, char[] contrasenaNueva) {
        Usuarios user = buscarUsuario(usuario);

        if (user == null || !user.getEstado()) {
            JOptionPane.showMessageDialog(null, "Error: Usuario no encontrado o inactivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (contrasenaNueva.length != 5) {
            JOptionPane.showMessageDialog(null, "La nueva contraseña debe tener exactamente 5 caracteres.", "Error de Contraseña", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (verificarCredenciales(usuario, contrasenaAntigua)) {
            user.setContrasena(contrasenaNueva);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Contraseña actual incorrecta.", "Error de Verificación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean eliminarUsuario(String usuario, char[] contrasena) {
        Usuarios user = buscarUsuario(usuario);

        if (user == null || !user.getEstado()) {
            JOptionPane.showMessageDialog(null, "Error: Usuario no encontrado o ya inactivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (verificarCredenciales(usuario, contrasena)) {
            user.setEstado(false);
            JOptionPane.showMessageDialog(null, "Cuenta de usuario " + usuario + " eliminada (Inactiva) exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Contraseña incorrecta para eliminar la cuenta.", "Error de Verificación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public ArrayList<Usuarios> getRankingData() {
        ArrayList<Usuarios> activos = new ArrayList<>();
        for (Usuarios user : listaUsuarios) {
            if (user.getEstado()) {
                activos.add(user);
            }
        }
        return activos;
    }
}
