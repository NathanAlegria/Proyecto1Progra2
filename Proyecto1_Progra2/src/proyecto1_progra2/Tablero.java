/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_progra2;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URL;

/**
 *
 * @author Nathan
 */

public class Tablero extends JFrame implements ActionListener {

    //Atributos    
    private static final int FILAS = 6;
    private static final int COLUMNAS = 6;
    // Color semi-transparente para resaltar movimientos
    private static final Color HIGHLIGHT_COLOR = new Color(0, 255, 0, 100);
    
    // Matriz de botones para el Tablero
    private JButton[][] botonesTablero;
    
    // Iconos para piezas NEGRAS
    private ImageIcon iconHombreLoboNegro;
    private ImageIcon iconVampiroNegro;
    private ImageIcon iconNecromancerNegro;
    private ImageIcon iconZombieNegro;
    
    // Iconos para piezas BLANCAS
    private ImageIcon iconHombreLoboBlanco;
    private ImageIcon iconVampiroBlanco;
    private ImageIcon iconNecromancerBlanco;
    private ImageIcon iconZombieBlanco;
    
    // Matriz para las piezas
    private Pieza[][] estadoTablero;  
    
    // Pieza seleccionada para mover o atacar
    private Pieza piezaSeleccionada = null;
    private int selectedRow = -1;
    private int selectedCol = -1;
    
    // Control de turnos
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

        // Panel para el label del turno
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
    
    //Crea las piezas en el Tablero según su color
    private void inicializarPiezasEnTableroLogico() {
        String[] orden = {"HombreLobo", "Vampiro", "Necromancer", "Necromancer", "Vampiro", "HombreLobo"};
        String colorNegro = "Negro";
        String colorBlanco = "Blanco";
        
        for (int j = 0; j < COLUMNAS; j++) {
            // Fila 0 para las piezas Negras
            estadoTablero[0][j] = crearNuevaPieza(orden[j], colorNegro);
            // Última fila para las piezas Blancas
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
    
    //Colocar pieza en algun lugar del Tablero
    public void colocarPieza(Pieza pieza, int r, int c) {
        if (r >= 0 && r < FILAS && c >= 0 && c < COLUMNAS && estadoTablero[r][c] == null) {
            estadoTablero[r][c] = pieza;
        }
    }

    
    private void cargarIconos() {
        final int iconSize = 125;    
        try {
            // Iconos NEGROS
            iconHombreLoboNegro = crearIconoEscalado("HombreLobo.jpg", iconSize);
            iconVampiroNegro = crearIconoEscalado("Vampiro.jpg", iconSize);
            iconNecromancerNegro = crearIconoEscalado("Nercromancer.jpg", iconSize);
            iconZombieNegro = crearIconoEscalado("Zombie.jpg", iconSize);
            
            // Iconos BLANCOS
            iconHombreLoboBlanco = crearIconoEscalado("HombreLoboB.jpg", iconSize);
            iconVampiroBlanco = crearIconoEscalado("VampiroB.jpg", iconSize);
            iconNecromancerBlanco = crearIconoEscalado("NecromancerB.jpg", iconSize);
            iconZombieBlanco = crearIconoEscalado("ZombieB.jpg", iconSize);
            
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
    
    
    //Recorre el tablero actualiza la apariencia de todos los botones    
    public void actualizarTableroVisual() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                JButton boton = botonesTablero[i][j];
                Pieza pieza = estadoTablero[i][j];

                if (pieza == null) {
                    boton.setIcon(null);
                    boton.setText(null);
                } else {
                    // Muestra el icono correcto según el tipo de pieza y su color
                    setPiezaVisual(i, j, pieza.getNombre(), pieza.getColor());
                }
            }
        }
        
        this.revalidate();
        this.repaint();
    }

    /// Asigna el icono al botón basándose en el tipo y color de la pieza.
    private void setPiezaVisual(int fila, int columna, String tipo, String color) {
        JButton boton = botonesTablero[fila][columna];
        ImageIcon icon = null;
        
        boolean esBlanco = color.equals("Blanco");
        
        switch (tipo) {
            case "HombreLobo":
                icon = esBlanco ? iconHombreLoboBlanco : iconHombreLoboNegro;
                break;
            case "Vampiro":
                icon = esBlanco ? iconVampiroBlanco : iconVampiroNegro;
                break;
            case "Necromancer":
                icon = esBlanco ? iconNecromancerBlanco : iconNecromancerNegro;
                break;
            case "Zombie":    
                icon = esBlanco ? iconZombieBlanco : iconZombieNegro;    
                break;
            default:
                boton.setIcon(null);    
                boton.setText(null);
                return;
        }
        
        boton.setIcon(icon);
        boton.setText(null);
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
    
    //Lógica recursiva para verificar si existe un camino libre de obstáculos
    
    private boolean MovimientoValido(int FilaA, int ColumnaA, int FilaF, int ColumnaF, int maxDistancia, int currentDistancia) {
          if (FilaA == FilaF && ColumnaA == ColumnaF) {
              return true;
          }

          if (currentDistancia >= maxDistancia) {
              return false;
          }
        
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;    
                
                int nextR = FilaA + dr;
                int nextC = ColumnaA + dc;

                if (nextR >= 0 && nextR < FILAS && nextC >= 0 && nextC < COLUMNAS) {
                    
                    int diffR = Math.abs(FilaF - nextR);
                    int diffC = Math.abs(ColumnaF - nextC);
                    int remainingDist = Math.max(diffR, diffC);
                    
                    if (remainingDist <= maxDistancia - (currentDistancia + 1)) {
                        
                        if (nextR == FilaF && nextC == ColumnaF) {
                            // Se encontró el destino
                            return MovimientoValido(nextR, nextC, FilaF, ColumnaF, maxDistancia, currentDistancia + 1);
                        }
                        
                        if (estadoTablero[nextR][nextC] == null) {
                            // Si la casilla está vacía, continuar buscando el camino
                            if (MovimientoValido(nextR, nextC, FilaF, ColumnaF, maxDistancia, currentDistancia + 1)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    //Función principal para validar si el camino es libre y está dentro de la distancia.
    
    private boolean isPathValid(int InicioF, int InicioC, int FilaF, int ColumnaF, int maxDistancia) {
        int distR = Math.abs(InicioF - FilaF);
        int distC = Math.abs(InicioC - ColumnaF);    
        int distanciaTotal = Math.max(distR, distC);
        
        if (distanciaTotal == 0 || distanciaTotal > maxDistancia) {
            return false;    
        }
        
        if (maxDistancia == 1) {
              return true; // Si solo mueve 1, no necesita validación de camino libre.
        }

        // Para movimientos mayores a 1 , se usa la función recursiva.
        return MovimientoValido(InicioF, InicioC, FilaF, ColumnaF, maxDistancia, 0);
    }
    
    
    //Movimiento de pieza a casilla vacía
    private boolean moverPieza(int InicioF, int InicioC, int FilaF, int ColumnaF) {
        if (estadoTablero[FilaF][ColumnaF] != null) {
              return false; // El destino no está vacío
        }
        
        Pieza pieza = estadoTablero[InicioF][InicioC];
        estadoTablero[FilaF][ColumnaF] = pieza;
        estadoTablero[InicioF][InicioC] = null; // Vacía la casilla de origen

        cambiarTurno();
        return true;
    }
    
    //Cambia el turno
    private void cambiarTurno() {
        jugadorActualColor = jugadorActualColor.equals("Blanco") ? "Negro" : "Blanco";
        if (turnoLabel != null) {
            turnoLabel.setText("Turno: " + jugadorActualColor + " | Controles: Ruleta y Mensajes");
        }
    }

    //Manejo de clicks de los botones del Tablero
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
    
    
    //Lógica principal del juego al hacer clic en una casilla.
    
    private void ManejoClick(int Fila, int Columna) {
        
        resetBorders(); // Limpia resaltados anteriores
        boolean accionEjecutada = false;

        if (piezaSeleccionada == null) {
            // FASE 1: Selección de Pieza Propia ---
            
            Pieza piezaEnCasilla = estadoTablero[Fila][Columna];

            if (piezaEnCasilla != null && piezaEnCasilla.getColor().equals(jugadorActualColor)) {
                
                // Excepción al Zombie Previene la selección directa del Zombie
                if (piezaEnCasilla.getNombre().equals("Zombie")) {
                      JOptionPane.showMessageDialog(this, "El Zombie no puede ser movido ni atacado directamente, solo es controlado por el Necromancer.", "Reglas del Zombie", JOptionPane.WARNING_MESSAGE);
                      return;
                }
                
                piezaSeleccionada = piezaEnCasilla;
                selectedRow = Fila;
                selectedCol = Columna;
                
                // Determinar el rango de movimiento y ataque para el resaltado
                int maxDistanciaMovimiento = 1;
                int maxDistanciaAtaque = 1;

                if (piezaSeleccionada.getNombre().equals("HombreLobo")) {
                    maxDistanciaMovimiento = 2;    
                    maxDistanciaAtaque = 1;    
                } else if (piezaSeleccionada.getNombre().equals("Necromancer")) {
                    maxDistanciaMovimiento = 1;    
                    // Para el Necromancer, el resaltado de ataque será hasta 2
                    maxDistanciaAtaque = 2;    
                }
                
                // Resaltar movimientos y ataques posibles
                for (int FilaF = 0; FilaF < FILAS; FilaF++) {
                    for (int ColumnaF = 0; ColumnaF < COLUMNAS; ColumnaF++) {
                        
                        JButton targetButton = botonesTablero[FilaF][ColumnaF];
                        Pieza destino = estadoTablero[FilaF][ColumnaF];
                        
                        if (destino == null) {
                            // Resaltar movimiento a casillas vacías
                            if (isPathValid(selectedRow, selectedCol, FilaF, ColumnaF, maxDistanciaMovimiento)) {
                                targetButton.setIcon(null);    
                                targetButton.setBackground(HIGHLIGHT_COLOR);
                            }    
                        }    
                        else if (!destino.getColor().equals(jugadorActualColor)) {
                            // Resaltar ataque a piezas enemigas
                            // Para el Necromancer, también resaltamos si el enemigo está adyacente a un zombie.
                            if (isPathValid(selectedRow, selectedCol, FilaF, ColumnaF, maxDistanciaAtaque) ||    
                                (piezaSeleccionada.getNombre().equals("Necromancer") && isAdjacentToFriendlyZombie(FilaF, ColumnaF, jugadorActualColor))) {
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
            //FASE 2: Movimiento o Ataque a Casilla Destino
            if (Fila == selectedRow && Columna == selectedCol) {
                // Deselección de la pieza
                accionEjecutada = true;
            } else {
                Pieza destino = estadoTablero[Fila][Columna];
                
                final int distR = Math.abs(selectedRow - Fila);
                final int distC = Math.abs(selectedCol - Columna);
                final int distanciaTotal = Math.max(distR, distC);

                int maxDistanciaMovimiento = piezaSeleccionada.getNombre().equals("HombreLobo") ? 2 : 1;
                int maxDistanciaAtaque = piezaSeleccionada.getNombre().equals("Necromancer") ? 2 : 1;
                String nombrePieza = piezaSeleccionada.getNombre();

                // Si la casilla destino está vacía (Intento de Movimiento o Conjuro)
                if (destino == null) {
                    
                    //Permite Conjurar a cualquier casilla vacía
                    if (nombrePieza.equals("Necromancer")) {
                        Necromancer muerte = (Necromancer) piezaSeleccionada;
                        int choiceIndex = -1; // -1: Cancelar, 0: Mover, 1: Conjurar
                        
                        if (distanciaTotal == 1 && isPathValid(selectedRow, selectedCol, Fila, Columna, 1)) {
                            // Opciones adyacentes: Mover (índice 0) o Conjurar (índice 1)
                            Object[] options = {"Mover a casilla", "Conjurar Zombie"};
                            choiceIndex = JOptionPane.showOptionDialog(
                                this,    
                                "¿Qué desea hacer con la Necromancer?",    
                                "Acción de Necromancer",
                                JOptionPane.YES_NO_OPTION,    
                                JOptionPane.QUESTION_MESSAGE,    
                                null,    
                                options,    
                                options[0]
                            );
                        } else {
                            // Opciones distantes: Conjurar (índice 0) o Cancelar (índice 1)
                            Object[] options = {"Conjurar Zombie", "Cancelar"};
                            int distantChoice = JOptionPane.showOptionDialog(
                                this,    
                                "Esta casilla está fuera del rango de movimiento.\n¿Desea Conjurar un Zombie aquí?",    
                                "Conjurar a Distancia",
                                JOptionPane.YES_NO_OPTION,    
                                JOptionPane.QUESTION_MESSAGE,    
                                null,    
                                options,    
                                options[0]
                            );
                            
                            // Mapeo: 0 en diálogo distante es Conjurar (que se comporta como índice 1 en la ejecución)
                            if (distantChoice == 0) {
                                choiceIndex = 1;    
                            } else {
                                choiceIndex = -1; // Cancelar
                            }
                        }
                        
                        if (choiceIndex == 0) { // Mover (Opción 1 del input original)
                            accionEjecutada = moverPieza(selectedRow, selectedCol, Fila, Columna);
                        } else if (choiceIndex == 1) { // Conjurar (Opción 2 del input original)
                            muerte.conjurarZombie(this, Fila, Columna);
                            cambiarTurno();
                            accionEjecutada = true;
                        }
                        
                    } else if (isPathValid(selectedRow, selectedCol, Fila, Columna, maxDistanciaMovimiento)) {
                        // Movimiento normal para Hombre Lobo o Vampiro
                        accionEjecutada = moverPieza(selectedRow, selectedCol, Fila, Columna);
                    } else {
                        JOptionPane.showMessageDialog(this, "Movimiento no válido para esta pieza o distancia.", "Error de Reglas", JOptionPane.WARNING_MESSAGE);
                    }
                }    
                // Si la casilla destino está ocupada (Intento de Ataque)
                else {
                    if (!destino.getColor().equals(piezaSeleccionada.getColor())) {
                        
                        boolean isStandardAttackPossible = isPathValid(selectedRow, selectedCol, Fila, Columna, maxDistanciaAtaque);
                        boolean isZombieAttackPossible = nombrePieza.equals("Necromancer") && isAdjacentToFriendlyZombie(Fila, Columna, jugadorActualColor);
                        
                        if (isStandardAttackPossible || isZombieAttackPossible) {
                            
                            String ataqueTipo = "0"; // 0: Cancelar, 1: Normal, 2: Especial
                            boolean isLongRangeZombieAttack = isZombieAttackPossible && !isStandardAttackPossible;
                            
                            if (nombrePieza.equals("Vampiro")) {
                                Object[] options = {"Ataque Normal (Espada)", "Ataque Especial"};
                                int choice = JOptionPane.showOptionDialog(
                                    this,    
                                    "¿Qué ataque desea usar?",    
                                    "Selección de Ataque",
                                    JOptionPane.YES_NO_OPTION,    
                                    JOptionPane.QUESTION_MESSAGE,    
                                    null,    
                                    options,    
                                    options[0]
                                );
                                if (choice == 0) ataqueTipo = "1";
                                else if (choice == 1) ataqueTipo = "2";
                            } else if (nombrePieza.equals("Necromancer")) {
                                if (isLongRangeZombieAttack) {
                                    //Forzar Ataque Zombie si está fuera de rango 2 pero adyacente a Zombie
                                    ataqueTipo = "2";
                                    JOptionPane.showMessageDialog(this, "El objetivo está fuera de rango de Ataque Normal/Lanza. Se forzará el Ataque Zombie (Especial).", "Ataque Zombie a Distancia", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    Object[] options = {"Ataque Normal (Espada)", "Ataque Especial (Lanza/Zombie)"};
                                    int choice = JOptionPane.showOptionDialog(
                                        this,    
                                        "¿Qué ataque desea usar?",    
                                        "Selección de Ataque",
                                        JOptionPane.YES_NO_OPTION,    
                                        JOptionPane.QUESTION_MESSAGE,    
                                        null,    
                                        options,    
                                        options[0]
                                    );
                                    if (choice == 0) ataqueTipo = "1";
                                    else if (choice == 1) ataqueTipo = "2";
                                }
                            } else {
                                // Caso por defecto para piezas como HombreLobo que solo tienen ataque normal.
                                ataqueTipo = "1";
                            }
                                
                            if ("1".equals(ataqueTipo)) {
                                // Ataque Normal: Solo a distancia 1
                                if (distanciaTotal > 1) {
                                      JOptionPane.showMessageDialog(this, "Ataque Normal: Solo se puede hacer a piezas adyacentes.", "Error de Rango", JOptionPane.WARNING_MESSAGE);
                                } else {
                                    piezaSeleccionada.atacar(destino);
                                    if (destino.getVida() <= 0) {
                                        estadoTablero[Fila][Columna] = null;    
                                    }
                                    cambiarTurno();
                                    accionEjecutada = true;
                                }

                            } else if ("2".equals(ataqueTipo) && (nombrePieza.equals("Vampiro") || nombrePieza.equals("Necromancer"))) {
                                
                                if (nombrePieza.equals("Necromancer")) {
                                    // Pasar la posición del Necromancer para el chequeo de la Lanza.
                                    accionEjecutada = manejarAtaqueEspecialNecromancer((Necromancer)piezaSeleccionada, destino, Fila, Columna, selectedRow, selectedCol, isLongRangeZombieAttack);
                                } else {
                                    // Ataque especial de Vampiro
                                    piezaSeleccionada.ataqueEspecial(destino, Fila, Columna, this);
                                    accionEjecutada = true;
                                }
                                
                                if (accionEjecutada && destino.getVida() <= 0) {
                                    estadoTablero[Fila][Columna] = null;    
                                }

                                if (accionEjecutada) {
                                    cambiarTurno();
                                }

                            } else if (!"0".equals(ataqueTipo)) {
                                JOptionPane.showMessageDialog(this, "Ataque cancelado o inválido.", "Ataque Inválido", JOptionPane.WARNING_MESSAGE);
                            }
                            
                        } else {
                            JOptionPane.showMessageDialog(this, "Ataque no válido para esta pieza o distancia.", "Error de Reglas", JOptionPane.WARNING_MESSAGE);
                        }

                    } else if (destino.getColor().equals(piezaSeleccionada.getColor())) {
                        JOptionPane.showMessageDialog(this, "No puedes atacar a una pieza de tu mismo color.", "Ataque Inválido", JOptionPane.WARNING_MESSAGE);
                    } else {
                        // Pieza fuera de rango
                        JOptionPane.showMessageDialog(this, "Ataque no válido para esta pieza o distancia.", "Error de Reglas", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }
        
        // Finalización del turno/acción
        if (accionEjecutada) {
            piezaSeleccionada = null;
            selectedRow = -1;
            selectedCol = -1;
        } else if (piezaSeleccionada != null) {
            // Si la pieza sigue seleccionada (acción cancelada/fallida)
            botonesTablero[selectedRow][selectedCol].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        }
        
        actualizarTableroVisual();    
    }
    
    
    //Ataque especiales de Necromancer
    
    private boolean manejarAtaqueEspecialNecromancer(Necromancer muerte, Pieza destino, int r, int c, int necR, int necC, boolean forzarZombieAttack) {
        
        String tipoAtaque = "0";

        if (forzarZombieAttack) {
            tipoAtaque = "2"; // Forzar la opción 2 si el Necromancer está atacando a larga distancia via Zombie
        } else {
            Object[] options = {"Lanza (Rango 2, ignora escudo)", "Zombie Attack (Adyacente a Zombie, Daño 1)"};
            int choice = JOptionPane.showOptionDialog(
                this,    
                "Seleccione ataque especial de Necromancer:",    
                "Ataque Especial",
                JOptionPane.YES_NO_OPTION,    
                JOptionPane.QUESTION_MESSAGE,    
                null,    
                options,    
                options[0]
            );

            if (choice == 0) tipoAtaque = "1"; // Lanza
            else if (choice == 1) tipoAtaque = "2"; // Zombie Attack
        }
        
        if ("1".equals(tipoAtaque)) {    
            // Lanza: Rango 2. Requiere camino libre.
            int dist = Math.max(Math.abs(necR - r), Math.abs(necC - c));
            if (dist == 2 && isPathValid(necR, necC, r, c, 2)) {
                muerte.lanzarLanza(destino);
                if (destino.getVida() <= 0) {
                    estadoTablero[r][c] = null;
                }
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Lanza: Rango debe ser exactamente 2 casillas con camino libre.", "Error de Rango", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } else if ("2".equals(tipoAtaque)) {    
            // Ataque Zombie: Revisa que el objetivo esté adyacente a un Zombie amigo
            if (isAdjacentToFriendlyZombie(r, c, muerte.getColor())) {
                
                muerte.ataqueZombie(destino); // Ejecuta 1 daño
                
                if (destino.getVida() <= 0) {
                    estadoTablero[r][c] = null;
                }
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Zombie Attack: La pieza enemiga DEBE estar adyacente a un Zombie propio.", "Error de Posicionamiento", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }
        return false;
    }
    
    /// Verifica si la casilla (r, c) es adyacente a un Zombie del mismo color.
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
    
    /**
     * Simula el giro de la ruleta para obtener una pieza existente del jugador actual.
     * (Función recursiva para reintentar si la pieza no existe, hasta 3 intentos).
     */
    private String girarRuleta(int intentosRestantes) {
        if (intentosRestantes <= 0) {
            return null; // Falló en encontrar una pieza existente después de los reintentos
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