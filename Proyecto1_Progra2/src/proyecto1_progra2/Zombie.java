/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_progra2;

/**
 *
 * @author Nathan
 */
public final class Zombie extends Pieza {
    
    // Zombie: 1 ataque, 1 vida, 0 escudo
    public Zombie(String color) {
        super("Zombie", color, 1, 1, 0); 
    }

    // El Zombie NO se puede mover
    @Override
    public boolean mover(int nuevaX, int nuevaY) {
        return false;
    }
}
