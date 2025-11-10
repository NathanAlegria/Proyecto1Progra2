/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Logica;

import java.util.ArrayList;
import Logica.Usuarios;

/**
 *
 * @author Nathan
 */
public interface InterfaceCuentas {

    void finalizarJuego(String ganador, String perdedor, boolean fueRetiro);

    ArrayList<String> getLogsDeUsuario(String nombreUsuario);

    boolean registrarUsuario(String usuario, char[] contrasena);

    boolean verificarCredenciales(String usuario, char[] contrasena);

    Usuarios buscarUsuario(String usuario);

    boolean cambiarContrasena(String usuario, char[] contrasenaAntigua, char[] contrasenaNueva);

    boolean eliminarUsuario(String usuario, char[] contrasena);

    ArrayList<Usuarios> getRankingData();

    ArrayList<Usuarios> getUsuariosRegistrados();

    ArrayList<String> getLogsPorJugador(String nombreJugador);

    void guardarUsuariosEnSistema();

    ArrayList<Usuarios> cargarUsuariosDelSistema();
}
