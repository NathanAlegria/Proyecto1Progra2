/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_progra2;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

/**
 *
 * @author Nathan
 */
public class Tablero extends JFrame implements ActionListener {

    private static final int FILAS = 6;
    private static final int COLUMNAS = 6;
    private static final Color HIGHLIGHT_COLOR = new Color(0, 255, 0, 100);
    private JButton[][] botonesTablero;
    private ImageIcon iconHombreLobo;
    private ImageIcon iconVampiro;
    private ImageIcon iconNecromancer;
    private ImageIcon iconZombie;
    private Pieza[][] estadoTablero;  
    private Pieza piezaSeleccionada = null;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private String jugadorActualColor = "Blanco"; 
    private JLabel turnoLabel;
        
    public Tablero() {
        this.estadoTablero = new Pieza[FILAS][COLUMNAS];
        
        cargarIconos();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        
        int windowHeight = screenHeight - 50;
        int windowWidth = windowHeight + 200;

        setTitle("Vampire Wargame - Partida");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(windowWidth, windowHeight);
        setLayout(new BorderLayout());

        JPanel panelPrincipal = new JPanel(new BorderLayout());

        JPanel panelTablero = new JPanel(new GridLayout(FILAS, COLUMNAS));
        botonesTablero = new JButton[FILAS][COLUMNAS];
        
        Color colorClaro = new Color(240, 240, 240);
        Color colorOscuro = new Color(50, 50, 50);
        
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                JButton boton = new JButton();
                boton.setOpaque(true);
                boton.setBorderPainted(false);
                
                if ((i + j) % 2 == 0) {
                    boton.setBackground(colorClaro);
                    boton.setForeground(colorOscuro);
                } else {
                    boton.setBackground(colorOscuro);
                    boton.setForeground(colorClaro);
                }
                
                boton.setFont(new Font("Arial", Font.BOLD, 10)); 
                
                boton.setActionCommand(i + "," + j); 
                boton.addActionListener(this);

                botonesTablero[i][j] = boton;
                panelTablero.add(boton);
            }
        }
        
        panelPrincipal.add(panelTablero, BorderLayout.CENTER);

        JPanel panelControles = new JPanel();
        turnoLabel = new JLabel("Turno: " + jugadorActualColor + " | Controles: Ruleta y Mensajes");
        panelControles.add(turnoLabel); 

        add(panelPrincipal, BorderLayout.CENTER);
        add(panelControles, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
        inicializarPiezasEnTableroLogico(); 
        actualizarTableroVisual();
    }
    
    private void inicializarPiezasEnTableroLogico() {
        String[] orden = {"HombreLobo", "Vampiro", "Necromancer", "Necromancer", "Vampiro", "HombreLobo"};
        String colorNegro = "Negro";
        String colorBlanco = "Blanco";
        
        for (int j = 0; j < COLUMNAS; j++) {
            estadoTablero[0][j] = crearNuevaPieza(orden[j], colorNegro);
            estadoTablero[FILAS - 1][j] = crearNuevaPieza(orden[j], colorBlanco);
        }
    }
    
    
    private Pieza crearNuevaPieza(String tipo, String color) {
        switch (tipo) {
            case "HombreLobo":
                return new HombreLobo(color);
            case "Vampiro":
                return new Vampiro(color);
            case "Necromancer":
                return new Necromancer(color);
            case "Zombie":
                return new Zombie(color);
            default:
                return null;
        }
    }
    
    public void colocarPieza(Pieza pieza, int r, int c) {
        if (r >= 0 && r < FILAS && c >= 0 && c < COLUMNAS && estadoTablero[r][c] == null) {
            estadoTablero[r][c] = pieza;
        }
    }

    private void cargarIconos() {
        final int iconSize = 125; 
        try {
            iconHombreLobo = crearIconoEscalado("HombreLobo.jpg", iconSize);
            iconVampiro = crearIconoEscalado("Vampiro.jpg", iconSize);
            iconNecromancer = crearIconoEscalado("Nercromancer.jpg", iconSize);
            iconZombie = crearIconoEscalado("Zombie.jpg", iconSize);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar iconos: " + e.getMessage(), "Error de Iconos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private ImageIcon crearIconoEscalado(String path, int size) {
        String resourcePath = "/Imagenes/" + path; 
        
        try {
            URL imageUrl = getClass().getResource(resourcePath);

            if (imageUrl == null) {
                System.err.println("Recurso no encontrado: " + resourcePath); 
                return null;
            }
            
            ImageIcon originalIcon = new ImageIcon(imageUrl); 
            
            Image image = originalIcon.getImage(); 
            Image scaledImage = image.getScaledInstance(size, size, Image.SCALE_SMOOTH);
            
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            throw new RuntimeException("Fallo al crear icono escalado para: " + path, e);
        }
    }
    
    public void actualizarTableroVisual() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                JButton boton = botonesTablero[i][j];
                Pieza pieza = estadoTablero[i][j];

                if (pieza == null) {
                    boton.setIcon(null);
                    boton.setText(null);
                } else {
                    setPiezaVisual(i, j, pieza.getNombre());
                }
            }
        }
        
        this.revalidate();
        this.repaint();
    }

    private void setPiezaVisual(int fila, int columna, String tipo) {
        JButton boton = botonesTablero[fila][columna];
        ImageIcon icon = null;
        
        switch (tipo) {
            case "HombreLobo":
                icon = iconHombreLobo;
                boton.setText(null);
                break;
            case "Vampiro":
                icon = iconVampiro;
                boton.setText(null);
                break;
            case "Necromancer":
                icon = iconNecromancer;
                boton.setText(null);
                break;
            case "Zombie": 
                icon = iconZombie;
                boton.setText(null); 
                break;
            default:
                boton.setIcon(null); 
                boton.setText(null);
                break;
        }
        
        boton.setIcon(icon);
    }
    
    private void resetBorders() {
        Color colorClaro = new Color(240, 240, 240);
        Color colorOscuro = new Color(50, 50, 50);

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                JButton boton = botonesTablero[i][j];
                boton.setBorder(null);
                
                if ((i + j) % 2 == 0) {
                    boton.setBackground(colorClaro);
                } else {
                    boton.setBackground(colorOscuro);
                }
                
                if (estadoTablero[i][j] == null) {
                    boton.setIcon(null);
                }
            }
        }
    }
    
    private boolean MovimientoValido(int r, int c, int endR, int endC, int maxDistancia, int currentDistancia) {
          if (r == endR && c == endC) {
              return true;
          }

          if (currentDistancia >= maxDistancia) {
              return false;
          }
        
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue; 
                
                int nextR = r + dr;
                int nextC = c + dc;

                if (nextR >= 0 && nextR < FILAS && nextC >= 0 && nextC < COLUMNAS) {
                    
                    int diffR = Math.abs(endR - nextR);
                    int diffC = Math.abs(endC - nextC);
                    int remainingDist = Math.max(diffR, diffC);
                    
                    if (remainingDist <= maxDistancia - (currentDistancia + 1)) {
                        
                        if (nextR == endR && nextC == endC) {
                            return MovimientoValido(nextR, nextC, endR, endC, maxDistancia, currentDistancia + 1);
                        }
                        
                        if (estadoTablero[nextR][nextC] == null) {
                            if (MovimientoValido(nextR, nextC, endR, endC, maxDistancia, currentDistancia + 1)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private boolean isPathValid(int startR, int startC, int endR, int endC, int maxDistancia) {
        int distR = Math.abs(startR - endR);
        int distC = Math.abs(startC - endC); 
        int distanciaTotal = Math.max(distR, distC);
        
        if (distanciaTotal == 0 || distanciaTotal > maxDistancia) {
            return false; 
        }
        
        if (maxDistancia == 1) {
              return true; 
        }

        return MovimientoValido(startR, startC, endR, endC, maxDistancia, 0);
    }
    
    
    private boolean moverPieza(int startR, int startC, int endR, int endC) {
        if (estadoTablero[endR][endC] != null) {
              return false;
        }
        
        Pieza pieza = estadoTablero[startR][startC];
        estadoTablero[endR][endC] = pieza;
        estadoTablero[startR][startC] = null; 

        cambiarTurno();
        return true;
    }
    
    private void cambiarTurno() {
        jugadorActualColor = jugadorActualColor.equals("Blanco") ? "Negro" : "Blanco";
        if (turnoLabel != null) {
            turnoLabel.setText("Turno: " + jugadorActualColor + " | Controles: Ruleta y Mensajes");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String comando = e.getActionCommand();
            String[] coordenadas = comando.split(",");
            int r = Integer.parseInt(coordenadas[0]);
            int c = Integer.parseInt(coordenadas[1]);
            
            ManejoClick(r, c);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al manejar el clic: " + ex.getMessage(), "Error de Juego", JOptionPane.ERROR_MESSAGE);
            piezaSeleccionada = null;
            selectedRow = -1;
            selectedCol = -1;
            actualizarTableroVisual(); 
        }
    }
    
    
    private void ManejoClick(int r, int c) {
        
        resetBorders();
        boolean accionEjecutada = false;

        if (piezaSeleccionada == null) {
            
            Pieza piezaEnCasilla = estadoTablero[r][c];

            if (piezaEnCasilla != null && piezaEnCasilla.getColor().equals(jugadorActualColor)) {
                
                piezaSeleccionada = piezaEnCasilla;
                selectedRow = r;
                selectedCol = c;
                
                int maxDistanciaMovimiento = 1;
                int maxDistanciaAtaque = 1;

                if (piezaSeleccionada.getNombre().equals("HombreLobo")) {
                    maxDistanciaMovimiento = 2;
                    maxDistanciaAtaque = 1; 
                } else if (piezaSeleccionada.getNombre().equals("Necromancer")) {
                    maxDistanciaMovimiento = 1; // Movimiento normal solo a 1
                    maxDistanciaAtaque = 2; //Ataque Lanza
                }
                
                for (int endR = 0; endR < FILAS; endR++) {
                    for (int endC = 0; endC < COLUMNAS; endC++) {
                        
                        JButton targetButton = botonesTablero[endR][endC];
                        Pieza destino = estadoTablero[endR][endC];
                        
                        if (destino == null) {
                            // Resaltar MOVIMIENTO a casillas vacías
                            if (isPathValid(selectedRow, selectedCol, endR, endC, maxDistanciaMovimiento)) {
                                targetButton.setIcon(null); 
                                targetButton.setBackground(HIGHLIGHT_COLOR);
                            } 
                        } 
                        else if (!destino.getColor().equals(jugadorActualColor)) {
                            // Resaltar ATAQUE a piezas enemigas
                            if (isPathValid(selectedRow, selectedCol, endR, endC, maxDistanciaAtaque)) {
                                targetButton.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                            }
                        }
                    }
                }
                
            } else {
                selectedRow = -1;
                selectedCol = -1;
            }
        } 
        else {
            if (r == selectedRow && c == selectedCol) {
                accionEjecutada = true;
            } else {
                Pieza destino = estadoTablero[r][c];
                
                final int distR = Math.abs(selectedRow - r);
                final int distC = Math.abs(selectedCol - c);
                final int distanciaTotal = Math.max(distR, distC);

                int maxDistanciaMovimiento = piezaSeleccionada.getNombre().equals("HombreLobo") ? 2 : 1;
                int maxDistanciaAtaque = piezaSeleccionada.getNombre().equals("Necromancer") ? 2 : 1;

                // Si la casilla destino está vacía
                if (destino == null) {
                    if (isPathValid(selectedRow, selectedCol, r, c, maxDistanciaMovimiento)) {
                        
                        if (piezaSeleccionada.getNombre().equals("Necromancer")) {
                            
                            if (distanciaTotal > 1) {
                                JOptionPane.showMessageDialog(this, "Movimiento de Necromancer solo a casillas adyacentes.", "Error de Reglas", JOptionPane.WARNING_MESSAGE);
                            } else {
                                Necromancer muerte = (Necromancer) piezaSeleccionada;
                                String opcion = JOptionPane.showInputDialog(this, "¿Qué desea hacer con la Necromancer?\n1: Mover a casilla\n2: Conjurar Zombie");
                                
                                if ("1".equals(opcion)) {
                                    accionEjecutada = moverPieza(selectedRow, selectedCol, r, c);
                                } else if ("2".equals(opcion)) {
                                    muerte.conjurarZombie(this, r, c);
                                    cambiarTurno();
                                    accionEjecutada = true;
                                }
                            }
                            
                        } else {
                            accionEjecutada = moverPieza(selectedRow, selectedCol, r, c);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Movimiento no válido para esta pieza o distancia.", "Error de Reglas", JOptionPane.WARNING_MESSAGE);
                    }
                } 
                // Si la casilla destino está ocupada
                else {
                    if (isPathValid(selectedRow, selectedCol, r, c, maxDistanciaAtaque) && !destino.getColor().equals(piezaSeleccionada.getColor())) {
                        
                        String ataqueTipo = JOptionPane.showInputDialog(this, "¿Qué ataque desea usar?\n1: Normal (Espada)\n2: Especial");
                            
                        if ("1".equals(ataqueTipo)) {
                            // Ataque Normal: Solo a distancia 1
                            if (distanciaTotal > 1) {
                                 JOptionPane.showMessageDialog(this, "Ataque Normal: Solo se puede hacer a piezas adyacentes.", "Error de Rango", JOptionPane.WARNING_MESSAGE);
                            } else {
                                piezaSeleccionada.atacar(destino);
                                
                                if (destino.getVida() <= 0) {
                                    estadoTablero[r][c] = piezaSeleccionada;
                                    estadoTablero[selectedRow][selectedCol] = null;
                                }
                                cambiarTurno();
                                accionEjecutada = true;
                            }

                        } else if ("2".equals(ataqueTipo)) {
                            
                            if (piezaSeleccionada.getNombre().equals("Necromancer")) {
                                accionEjecutada = manejarAtaqueEspecialNecromancer((Necromancer)piezaSeleccionada, destino, r, c);
                            } else {
                                piezaSeleccionada.ataqueEspecial(destino, r, c, this);
                                accionEjecutada = true;
                            }
                            
                            if (accionEjecutada && destino.getVida() <= 0) {
                                estadoTablero[r][c] = null;
                            }

                            if (accionEjecutada) {
                                cambiarTurno();
                            }

                        } else {
                            JOptionPane.showMessageDialog(this, "Ataque cancelado o inválido.", "Ataque Inválido", JOptionPane.WARNING_MESSAGE);
                        }
                        
                    } else if (destino.getColor().equals(piezaSeleccionada.getColor())) {
                        JOptionPane.showMessageDialog(this, "No puedes atacar a una pieza de tu mismo color.", "Ataque Inválido", JOptionPane.WARNING_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Ataque no válido para esta pieza o distancia.", "Error de Reglas", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }
        
        if (accionEjecutada) {
            piezaSeleccionada = null;
            selectedRow = -1;
            selectedCol = -1;
        } else if (piezaSeleccionada != null) {
            botonesTablero[selectedRow][selectedCol].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        }
        
        actualizarTableroVisual(); 
    }
    
    
    private boolean manejarAtaqueEspecialNecromancer(Necromancer muerte, Pieza destino, int r, int c) {
        String tipoAtaque = JOptionPane.showInputDialog(
            "Seleccione ataque especial de Necromancer:\n1: Lanza (rango 2, ignora escudo)\n2: Zombie Attack (adyacente a Zombie, 1 daño)");
        
        if ("1".equals(tipoAtaque)) { 
            int dist = Math.max(Math.abs(selectedRow - r), Math.abs(selectedCol - c));
            if (dist == 2 && isPathValid(selectedRow, selectedCol, r, c, 2)) {
                muerte.lanzarLanza(destino);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Lanza: Rango debe ser exactamente 2 casillas con camino libre.", "Error de Rango", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } else if ("2".equals(tipoAtaque)) { 
            int dist = Math.max(Math.abs(selectedRow - r), Math.abs(selectedCol - c));
            // Ataque Zombie no tiene restricción de movimiento del Necromancer.
            if (dist <= 2) { 
                if (isAdjacentToFriendlyZombie(r, c, muerte.getColor())) {
                    muerte.ataqueZombie(destino);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "Zombie Attack: La pieza enemiga debe estar adyacente a un Zombie propio.", "Error de Posicionamiento", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Zombie Attack: El Necromancer solo puede atacar a 2 casillas de distancia.", "Error de Rango", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return false;
    }
    
    private boolean isAdjacentToFriendlyZombie(int r, int c, String color) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue; 
                int nextR = r + dr;
                int nextC = c + dc;
                
                if (nextR >= 0 && nextR < FILAS && nextC >= 0 && nextC < COLUMNAS) {
                    Pieza p = estadoTablero[nextR][nextC];
                    if (p != null && "Zombie".equals(p.getNombre()) && color.equals(p.getColor())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private String girarRuleta(int intentosRestantes) {
        if (intentosRestantes <= 0) {
            return null;
        }
        
        String[] tipos = {"HombreLobo", "Vampiro", "Necromancer"};
        String piezaSeleccionadaRuleta = tipos[(int) (Math.random() * tipos.length)];
        
        boolean piezaExiste = false;
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                Pieza p = estadoTablero[i][j];
                if (p != null && jugadorActualColor.equals(p.getColor()) && piezaSeleccionadaRuleta.equals(p.getNombre())) {
                    piezaExiste = true;
                    break;
                }
            }
            if (piezaExiste) break;
        }

        if (piezaExiste) {
            return piezaSeleccionadaRuleta;
        } else {
            return girarRuleta(intentosRestantes - 1);
        }
    }
}