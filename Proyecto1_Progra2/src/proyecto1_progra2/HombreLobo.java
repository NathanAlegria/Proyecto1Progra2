/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_progra2;

/**
 *
 * @author Nathan
 */
public class HombreLobo extends Pieza {
    
    // Hombre Lobo: 5 ataque, 5 vida, 2 escudo
    public HombreLobo(String color) {
        super("HombreLobo", color, 5, 5, 2);
    }

    // Se mueve hasta 2 casillas
    @Override
    public boolean mover(int nuevaX, int nuevaY) {
        return true; 
    }
    
}
