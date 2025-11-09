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
public class Cuentas implements InterfaceCuentas {

    private static Cuentas instancia;
    private ArrayList<Usuarios> listaUsuarios;
    // CAMBIO: Se elimina la lista de logs global, ahora están en Usuarios.java
    // private ArrayList<String> logDeJuegos; 

    private Cuentas() {
        this.listaUsuarios = new ArrayList<>();
        // CAMBIO: Se elimina la inicialización de la lista de logs global
        // this.logDeJuegos = new ArrayList<>(); 
    }

    public static Cuentas getInstance() {
        if (instancia == null) {
            instancia = new Cuentas();
        }
        return instancia;
    }

    // =======================================================================
    // --- CAMBIO: Implementación de la lógica de fin de juego ---
    // =======================================================================
    @Override
    public void finalizarJuego(String nombreGanador, String nombrePerdedor, boolean fueRetiro) {
        Usuarios ganador = buscarUsuario(nombreGanador);
        Usuarios perdedor = buscarUsuario(nombrePerdedor);
        final int PUNTOS_GANADOS = 3;

        if (ganador == null || perdedor == null) {
            // Manejar error si alguno de los usuarios no existe
            System.err.println("Error: Uno o ambos usuarios no encontrados al finalizar el juego.");
            return; 
        }

        // 1. Sumar puntos al ganador
        ganador.sumarPuntos(PUNTOS_GANADOS);

        // 2. Generar y registrar el mensaje de log
        String mensajeGanador, mensajePerdedor;
        
        if (!fueRetiro) {
            // Victoria por Destrucción Total
            mensajeGanador = String.format("🎉 VICTORIA: Venciste a %s, has ganado %d puntos.", 
                                           perdedor.getUsuario(), PUNTOS_GANADOS);
            mensajePerdedor = String.format("💀 DERROTA: Fuiste vencido por %s.", 
                                            ganador.getUsuario());
            
            // Mensaje a mostrar en pantalla (el jugador Logueado debe ser el que gano)
            JOptionPane.showMessageDialog(null, 
                String.format("JUGADOR %s VENCIO A JUGADOR %s, FELICIDADES HAS GANADO %d PUNTOS", 
                              nombreGanador, nombrePerdedor, PUNTOS_GANADOS), 
                "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);

        } else {
            // Victoria por Retiro del Oponente
            mensajeGanador = String.format("🏆 VICTORIA: %s se ha retirado, has ganado %d puntos.", 
                                           perdedor.getUsuario(), PUNTOS_GANADOS);
            mensajePerdedor = String.format("🚪 RETIRO: Te has retirado de la partida contra %s.", 
                                            ganador.getUsuario());
            
            // Mensaje a mostrar en pantalla (el jugador Logueado debe ser el que gano)
            JOptionPane.showMessageDialog(null, 
                String.format("JUGADOR %s SE HA RETIRADO, FELICIDADES JUGADOR %s, HAS GANADO %d PUNTOS", 
                              nombrePerdedor, nombreGanador, PUNTOS_GANADOS), 
                "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);
        }

        // Registrar el log en el historial personal de AMBOS jugadores
        ganador.agregarLog(mensajeGanador);
        perdedor.agregarLog(mensajePerdedor);
        
        // NOTA: La lógica de cerrar la ventana de juego y volver al MENU PRINCIPAL 
        // debe implementarse en la clase de la interfaz gráfica que llama a este método.
    }
    
    // CAMBIO: Implementación del método para obtener los logs de un usuario
    @Override
    public ArrayList<String> getLogsDeUsuario(String nombreUsuario) {
        Usuarios user = buscarUsuario(nombreUsuario);
        if (user != null) {
            return user.getLogDeMisJuegos();
        }
        return new ArrayList<>(); // Retorna una lista vacía si el usuario no existe
    }


    // =======================================================================
    // --- EL RESTO DE MÉTODOS EXISTENTES PERMANECE IGUAL ---
    // =======================================================================

    // ... (El resto del código de Cuentas.java, como registrarUsuario, 
    // verificarCredenciales, etc., sigue aquí) ...
    
    // Se elimina el método auxiliar getLogDeJuegos() que era para la lista global
    /*
    public ArrayList<String> getLogDeJuegos() {
        return logDeJuegos;
    }
    */
    
    public boolean registrarUsuario(String usuario, char[] contrasena) {
        // ... (código existente) ...
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

    // ... (resto de métodos: verificarCredenciales, buscarUsuario, cambiarContrasena, eliminarUsuario, getRankingData, getUsuariosRegistrados) ...
    
    public boolean verificarCredenciales(String usuario, char[] contrasena) {
        // ... (código existente) ...
        Usuarios user = buscarUsuario(usuario);
        if (user == null || !user.getEstado()) {
            return false;
        }

        char[] storedPass = user.getContrasena();
        boolean match = Arrays.equals(storedPass, contrasena);
        return match;
    }

    public Usuarios buscarUsuario(String usuario) {
        // ... (código existente) ...
        for (Usuarios user : listaUsuarios) {
            if (user.getUsuario().equalsIgnoreCase(usuario)) {
                return user;
            }
        }
        return null;
    }

    public boolean cambiarContrasena(String usuario, char[] contrasenaAntigua, char[] contrasenaNueva) {
        // ... (código existente) ...
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

    public boolean eliminarUsuario(String usuario, char[] contrasena) {
        // ... (código existente) ...
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

    public ArrayList<Usuarios> getRankingData() {
        // ... (código existente) ...
        ArrayList<Usuarios> activos = new ArrayList<>();
        for (Usuarios user : listaUsuarios) {
            if (user.getEstado()) {
                activos.add(user);
            }
        }
        return activos;
    }
    @Override
    public ArrayList<Usuarios> getUsuariosRegistrados() {
        return listaUsuarios;
    }
    
    @Override
    public ArrayList<String> getLogsPorJugador(String nombreJugador) {
        ArrayList<String> logs = new ArrayList<>();
        for (Usuarios u : listaUsuarios) {
            if (u.getUsuario().equals(nombreJugador)) {
                logs.addAll(u.getLogDeMisJuegos());
                break;
            }
        }
        return logs;
    }
}