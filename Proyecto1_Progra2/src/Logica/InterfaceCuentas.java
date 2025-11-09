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

    // CAMBIO 1: Nuevo método para manejar el fin de juego (suma puntos y registra logs)
    void finalizarJuego(String ganador, String perdedor, boolean fueRetiro);

    // CAMBIO 2: Método para obtener los logs de un usuario (para "Mis ultimos juegos")
    ArrayList<String> getLogsDeUsuario(String nombreUsuario);

    // ... (El resto de métodos permanece igual)
    //registra usuario
    boolean registrarUsuario(String usuario, char[] contrasena);

    //Verifica credenciales del usuario
    boolean verificarCredenciales(String usuario, char[] contrasena);

    //busca usuario por su nombre
    Usuarios buscarUsuario(String usuario);

    //Cambir contra
    boolean cambiarContrasena(String usuario, char[] contrasenaAntigua, char[] contrasenaNueva);

    //Eliminar Usuario
    boolean eliminarUsuario(String usuario, char[] contrasena);

    //Lista que almacena usuarios activos para rankings
    ArrayList<Usuarios> getRankingData();

    // Método para obtener todos los usuarios registrados (activos e inactivos)
    ArrayList<Usuarios> getUsuariosRegistrados();

    ArrayList<String> getLogsPorJugador(String nombreJugador);

}
