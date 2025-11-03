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

    //Lista que almacena usuarios para rankings
    ArrayList<Usuarios> getRankingData();
}