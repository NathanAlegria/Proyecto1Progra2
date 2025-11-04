/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_progra2;
import javax.swing.JOptionPane;
/**
 *
 * @author Nathan
 */
 
public class Necromancer extends Pieza {
    
    // Necromancer: 4 ataque, 3 vida, 1 escudo
    public Necromancer(String color) {
        super("Necromancer", color, 4, 3, 1);
    }
    
    // Se mueve 1 casilla
    @Override
    public boolean mover(int nuevaX, int nuevaY) {
        return true; 
    }

    // Método para conjurar 
    public void conjurarZombie(Tablero tablero, int nuevaX, int nuevaY) {
        tablero.colocarPieza(new Zombie(this.color), nuevaX, nuevaY);
        JOptionPane.showMessageDialog(null, "¡" + this.nombre + " ha conjurado un Zombie en (" + nuevaX + ", " + nuevaY + ")!");
    }
    
    // Ataque Especial: Lanza
    public void lanzarLanza(Pieza piezaEnemiga) {
        //Mitad del ataque normal
        int dano = this.ataqueNormal / 2; // 2 puntos
        
        //Ignora el escudo
        piezaEnemiga.restarVidaIgnorandoEscudo(dano);
        
        JOptionPane.showMessageDialog(null, "Necromancer lanza LANZA a " + piezaEnemiga.nombre + ". Daño crítico: " + dano +
                                      ". Enemigo restante: " + piezaEnemiga.getEscudo() + " Escudo, " + piezaEnemiga.getVida() + " Vida.");
    }
    
    // Ataque Especial: Zombie
    public void ataqueZombie(Pieza piezaEnemiga) {
        // Potencia: 1 punto de daño
        int dano = 1;
        
        piezaEnemiga.restarVida(dano);
        
        JOptionPane.showMessageDialog(null, "Necromancer ordena ATAQUE ZOMBIE a " + piezaEnemiga.nombre + ". Daño: " + dano +
                                      ". Enemigo restante: " + piezaEnemiga.getEscudo() + " Escudo, " + piezaEnemiga.getVida() + " Vida.");
    }
}
