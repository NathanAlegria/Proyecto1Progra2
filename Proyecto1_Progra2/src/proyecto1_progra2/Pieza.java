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
public abstract class Pieza {

    // Atributos base
    protected String color;
    protected int ataqueNormal;
    protected int vida;
    protected int escudo;
    protected int posicionX; // Ahora no se usan, el Tablero las maneja, pero se mantienen por modelo.
    protected int posicionY; // Ahora no se usan, el Tablero las maneja, pero se mantienen por modelo.
    protected String nombre;

    // Constructor
    public Pieza(String nombre, String color, int ataqueNormal, int vida, int escudo) {
        this.nombre = nombre;
        this.color = color;
        this.ataqueNormal = ataqueNormal;
        this.vida = vida;
        this.escudo = escudo;
    }

    // --- MÉTODOS REQUERIDOS ---
    // Función Abstracta: Obliga a las subclases a definir cómo moverse
    public abstract boolean mover(int nuevaX, int nuevaY);

    // Función Final: Lógica de ataque normal, no se puede sobrescribir
    public final void atacar(Pieza piezaEnemiga) {
        int dano = this.ataqueNormal;
        piezaEnemiga.restarVida(dano);

        JOptionPane.showMessageDialog(null, this.nombre + " (" + this.color + ") ataca a " + piezaEnemiga.nombre
                + ". Daño: " + dano + ". Enemigo restante: " + piezaEnemiga.getEscudo() + " Escudo, " + piezaEnemiga.getVida() + " Vida.");
    }

    // Lógica para usar la habilidad especial de la pieza (Polimorfismo)
    // Se pasa el Tablero para que piezas como la Muerte puedan crear otras piezas.
    public void ataqueEspecial(Pieza piezaEnemiga, int casillaDestinoX, int casillaDestinoY, Tablero tablero) {
        // Implementación base: Por defecto usa el ataque normal si no hay especial.
        this.atacar(piezaEnemiga);
    }

    // --- LÓGICA DE DAÑO ---
    // Resta vida y escudo tras un ataque normal
    public void restarVida(int dano) {
        int restante = dano;

        // 1. Gasta Escudo
        if (this.escudo > 0) {
            int escudoPerdido = Math.min(this.escudo, restante);
            this.escudo -= escudoPerdido;
            restante -= escudoPerdido;
        }

        // 2. Gasta Vida
        if (restante > 0) {
            this.vida -= restante;
        }
    }

    // Método para restar vida IGNORANDO el escudo (usado por el ataque Lanza de la Muerte)
    public void restarVidaIgnorandoEscudo(int dano) {
        this.vida -= dano;
    }

    // --- GETTERS ---
    public int getVida() {
        return vida;
    }

    public int getEscudo() {
        return escudo;
    }

    public String getColor() {
        return color;
    }

    public String getNombre() {
        return nombre;
    }
}
