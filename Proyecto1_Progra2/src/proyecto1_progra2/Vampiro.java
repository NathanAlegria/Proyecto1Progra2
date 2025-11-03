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
public class Vampiro extends Pieza {
    
    // Vampiro: 3 ataque, 4 vida, 5 escudo (Stats iniciales)
    public Vampiro(String color) {
        super("Vampiro", color, 3, 4, 5);
    }
    
    @Override
    public boolean mover(int nuevaX, int nuevaY) {
        return true; 
    }

    // Ataque Especial: Chupar Sangre
    @Override
    public void ataqueEspecial(Pieza piezaEnemiga, int casillaDestinoX, int casillaDestinoY, Tablero tablero) {
        
        // Quita 1 punto de vida al enemigo (ignora escudo)
        piezaEnemiga.restarVidaIgnorandoEscudo(1);
        
        // Restaura 1 punto a su propia vida
        this.vida += 1;
        
        JOptionPane.showMessageDialog(null, "Vampiro chupa sangre a " + piezaEnemiga.nombre + 
                                      ". Quita 1 vida y restaura 1 vida. Nueva vida de Vampiro: " + this.vida);
    }
}