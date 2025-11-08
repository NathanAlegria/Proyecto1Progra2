/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_progra2;

import javax.swing.*;
import Logica.InterfaceCuentas;
import Logica.Usuarios;


/**
 *
 * @author Nathan
 */
public class Main {
    
    /**
     * Punto de entrada de la aplicación.
     */
    public static void main(String[] args) {
        // Los objetos gráficos de Swing DEBEN ser inicializados en el Event Dispatch Thread (EDT).
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 1. Crear las instancias de los parámetros requeridos
                String jugadorBlanco = "Dra. Alucard";
                String jugadorNegro = "Sr. Helsing";
                
                // Creamos la instancia de la interfaz de cuentas de prueba
                InterfaceCuentas sistemaCuentas = new DummyCuentas();
                
                System.out.println("Iniciando Tablero para los jugadores: " + jugadorBlanco + " vs " + jugadorNegro);
                
                // 2. Instanciar la clase Tablero
                // NOTA: Se asume que la clase Tablero (con todas sus dependencias como Pieza, RuletaPanel, etc.)
                // está disponible y compila.
                new Tablero(jugadorBlanco, jugadorNegro, sistemaCuentas);
                
                // Si tu constructor de Tablero solo requiere dos argumentos (como en tus ejemplos anteriores)
                // usa new Tablero(jugadorBlanco, jugadorNegro);
            }
        });
    }
}


