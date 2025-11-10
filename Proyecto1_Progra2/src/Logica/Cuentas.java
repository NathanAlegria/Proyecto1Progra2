/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JOptionPane;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author Nathan
 */
public class Cuentas implements InterfaceCuentas {

    private static Cuentas instancia;
    private ArrayList<Usuarios> listaUsuarios;

    // Ya no se usa archivo, todo se maneja en memoria
    private Cuentas() {
        this.listaUsuarios = new ArrayList<>();
    }

    public static Cuentas getInstance() {
        if (instancia == null) {
            instancia = new Cuentas();
        }
        return instancia;
    }

    // ────────────────────────────────────────────────
    // Ya no se guarda ni se carga información
    // ────────────────────────────────────────────────
    @Override
    public void guardarUsuariosEnSistema() {
        // Sin persistencia: no se guarda nada
    }

    @Override
    public ArrayList<Usuarios> cargarUsuariosDelSistema() {
        return new ArrayList<>();
    }

    // ────────────────────────────────────────────────
    // Lógica de usuarios y partidas
    // ────────────────────────────────────────────────
    @Override
    public void finalizarJuego(String nombreGanador, String nombrePerdedor, boolean fueRetiro) {
        Usuarios ganador = buscarUsuario(nombreGanador);
        Usuarios perdedor = buscarUsuario(nombrePerdedor);
        final int PUNTOS_GANADOS = 3;

        if (ganador == null || perdedor == null) {
            System.err.println("Error: Uno o ambos usuarios no encontrados al finalizar el juego.");
            return;
        }

        String mensajeGanador, mensajePerdedor;

        if (!fueRetiro) {
            ganador.sumarPuntos(PUNTOS_GANADOS);
            mensajeGanador = String.format("🎉 VICTORIA: Venciste a %s, has ganado %d puntos.", perdedor.getUsuario(), PUNTOS_GANADOS);
            mensajePerdedor = String.format("💀 DERROTA: Fuiste vencido por %s.", ganador.getUsuario());

            JOptionPane.showMessageDialog(null,
                    String.format("¡JUGADOR %s VENCIO A JUGADOR %s! HAS GANADO %d PUNTOS.",
                            nombreGanador, nombrePerdedor, PUNTOS_GANADOS),
                    "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);

        } else {
            ganador.sumarPuntos(PUNTOS_GANADOS);
            mensajeGanador = String.format("🏆 VICTORIA (Retiro): %s se ha retirado, has ganado %d puntos.", perdedor.getUsuario(), PUNTOS_GANADOS);
            mensajePerdedor = String.format("🚪 RETIRO: Te has retirado de la partida contra %s.", ganador.getUsuario());

            JOptionPane.showMessageDialog(null,
                    String.format("JUGADOR %s SE HA RETIRADO. ¡FELICIDADES JUGADOR %s, HAS GANADO %d PUNTOS!",
                            nombrePerdedor, nombreGanador, PUNTOS_GANADOS),
                    "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);
        }

        ganador.agregarLog(mensajeGanador);
        perdedor.agregarLog(mensajePerdedor);
    }

    @Override
    public ArrayList<String> getLogsDeUsuario(String nombreUsuario) {
        Usuarios user = buscarUsuario(nombreUsuario);
        if (user != null) {
            return user.getLogDeMisJuegos();
        }
        return new ArrayList<>();
    }

    @Override
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

    @Override
    public boolean verificarCredenciales(String usuario, char[] contrasena) {
        Usuarios user = buscarUsuario(usuario);
        if (user == null || !user.getEstado()) {
            return false;
        }

        char[] storedPass = user.getContrasena();
        boolean match = Arrays.equals(storedPass, contrasena);

        Usuarios.limpiarContrasena(contrasena);
        Usuarios.limpiarContrasena(storedPass);

        return match;
    }

    @Override
    public Usuarios buscarUsuario(String usuario) {
        for (Usuarios user : listaUsuarios) {
            if (user.getUsuario().equalsIgnoreCase(usuario)) {
                return user;
            }
        }
        return null;
    }

    @Override
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
            JOptionPane.showMessageDialog(null, "Contraseña cambiada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Contraseña actual incorrecta.", "Error de Verificación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public boolean eliminarUsuario(String usuario, char[] contrasena) {
        Usuarios user = buscarUsuario(usuario);

        if (user == null || !user.getEstado()) {
            JOptionPane.showMessageDialog(null, "Error: Usuario no encontrado o ya inactivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (verificarCredenciales(usuario, contrasena)) {
            user.setEstado(false);
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Contraseña incorrecta para eliminar la cuenta.", "Error de Verificación", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public ArrayList<Usuarios> getRankingData() {
        ArrayList<Usuarios> activos = new ArrayList<>();
        for (Usuarios user : listaUsuarios) {
            if (user.getEstado()) {
                activos.add(user);
            }
        }

        Collections.sort(activos, Comparator.comparingInt(Usuarios::getPuntos).reversed());
        return activos;
    }

    @Override
    public ArrayList<Usuarios> getUsuariosRegistrados() {
        return listaUsuarios;
    }

    @Override
    public ArrayList<String> getLogsPorJugador(String nombreJugador) {
        Usuarios u = buscarUsuario(nombreJugador);
        if (u != null) {
            return u.getLogDeMisJuegos();
        }
        return new ArrayList<>();
    }
}
