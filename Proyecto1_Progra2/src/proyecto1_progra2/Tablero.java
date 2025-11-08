/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_progra2;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.*;
import Logica.InterfaceCuentas;
import Logica.Usuarios;
import Logica.Cuentas;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

/**
 *
 * @author Nathan
 */
public class Tablero extends JFrame implements ActionListener {

    private Usuarios usuarioActual;
    private static final int FILAS = 6;
    private static final int COLUMNAS = 6;
    private static final Color HIGHLIGHT_COLOR = new Color(0, 255, 0, 100);

    private JButton[][] botonesTablero;
    private ImageIcon iconHombreLoboNegro, iconVampiroNegro, iconNecromancerNegro, iconZombieNegro;
    private ImageIcon iconHombreLoboBlanco, iconVampiroBlanco, iconNecromancerBlanco, iconZombieBlanco;

    public Pieza[][] estadoTablero;

    private Pieza piezaSeleccionada = null;
    private int selectedRow = -1;
    private int selectedCol = -1;

    private String jugadorActualColor = "Blanco";
    private JLabel turnoLabel;

    // Ruleta
    private final RuletaPanel ruletaPanel;
    private final JButton botonRuleta;
    private final JButton botonRendirse;
    private JLabel girosRestantesLabel;

    private String nombreJugadorBlanco;
    private String nombreJugadorNegro;

    private String piezaDelTurno = null;
    private int girosRestantes = 0;
    private boolean turnoEnCurso = false;

    private InterfaceCuentas sistemaCuentas; // INTERFACE PARA PUNTOS Y USUARIOS
    private Menu menuReferencia; // NUEVO CAMPO PARA ALMACENAR LA REFERENCIA AL MENÚ

    // CONSTRUCTOR ACTUALIZADO PARA RECIBIR LA REFERENCIA DEL MENÚ
    public Tablero(String jugadorBlanco, String jugadorNegro, InterfaceCuentas sistemaCuentas, Menu menuReferencia) {
        this.estadoTablero = new Pieza[FILAS][COLUMNAS];
        this.sistemaCuentas = sistemaCuentas;
        this.nombreJugadorBlanco = jugadorBlanco;
        this.nombreJugadorNegro = jugadorNegro;
        this.menuReferencia = menuReferencia; // ALMACENAR REFERENCIA
        this.usuarioActual = sistemaCuentas.buscarUsuario(nombreJugadorBlanco);

        cargarIconos();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        int windowHeight = screenHeight - 50;
        int windowWidth = windowHeight + 250;

        setTitle("Vampire Wargame - Partida");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(windowWidth, windowHeight);
        setLayout(new BorderLayout());

        // Panel Principal
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
                boton.setBackground((i + j) % 2 == 0 ? colorClaro : colorOscuro);
                boton.setFont(new Font("Arial", Font.BOLD, 10));
                boton.setActionCommand(i + "," + j);
                boton.addActionListener(this);
                botonesTablero[i][j] = boton;
                panelTablero.add(boton);
            }
        }
        panelPrincipal.add(panelTablero, BorderLayout.CENTER);

        // Panel de controles inferior
        JPanel panelControles = new JPanel();
        turnoLabel = new JLabel("Turno: " + jugadorActualColor + " | Controles: Ruleta y Mensajes");
        panelControles.add(turnoLabel);

        // Panel lateral para ruleta y botones
        JPanel panelLateral = new JPanel();
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelLateral.setPreferredSize(new Dimension(250, windowHeight));

        JLabel ruletaTitulo = new JLabel("Ruleta de Turno");
        ruletaTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelLateral.add(ruletaTitulo);

        panelLateral.add(Box.createRigidArea(new Dimension(0, 5)));

        ruletaPanel = new RuletaPanel(this);
        ruletaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelLateral.add(ruletaPanel);

        panelLateral.add(Box.createRigidArea(new Dimension(0, 15))); // SEPARACION

        // Botón Rendirse
        botonRendirse = new JButton("Rendirse");
        botonRendirse.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonRendirse.setBackground(Color.RED);
        botonRendirse.setOpaque(true);
        botonRendirse.setForeground(Color.WHITE);
        botonRendirse.addActionListener(e -> rendirse());
        panelLateral.add(botonRendirse);

        panelLateral.add(Box.createRigidArea(new Dimension(0, 15))); // SEPARACION

        // Botón Ruleta
        botonRuleta = new JButton("GIRAR RULETA");
        botonRuleta.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonRuleta.setOpaque(true);
        botonRuleta.setBackground(Color.LIGHT_GRAY);
        botonRuleta.addActionListener(e -> {
            if (!turnoEnCurso) {
                iniciarTurno();
            } else if (ruletaPanel.isGirando()) {
                ruletaPanel.detener();
            } else {
                JOptionPane.showMessageDialog(this, "Debes mover la pieza " + piezaDelTurno + " para terminar el turno.", "Mover Pieza", JOptionPane.WARNING_MESSAGE);
            }
        });
        panelLateral.add(botonRuleta);

        panelLateral.add(Box.createRigidArea(new Dimension(0, 5)));

        girosRestantesLabel = new JLabel("Giros restantes: 0");
        girosRestantesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelLateral.add(girosRestantesLabel);

        add(panelPrincipal, BorderLayout.CENTER);
        add(panelControles, BorderLayout.SOUTH);
        add(panelLateral, BorderLayout.EAST);

        setLocationRelativeTo(null);
        setVisible(true);

        inicializarPiezasEnTableroLogico();
        actualizarTableroVisual();
        actualizarEstadoRuleta();
    }

    // ===========================================
    // --- NUEVO MÉTODO PARA RENDIRSE ---
    // ===========================================
    // ESTE CÓDIGO PERTENECE A LA CLASE TABLERO.JAVA
    private void rendirse() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Deseas rendirte? El contrincante obtendrá 3 puntos.",
                "Confirmar Rendirse",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {

            // 1. Determinar ganador y perdedor
            // El perdedor es el jugador cuyo turno es actualmente (jugadorActualColor)
            String nombrePerdedor = jugadorActualColor.equals("Blanco") ? nombreJugadorBlanco : nombreJugadorNegro;
            String nombreGanador = jugadorActualColor.equals("Blanco") ? nombreJugadorNegro : nombreJugadorBlanco;

            // 2. Buscar usuario real y sumar puntos
            Usuarios ganador = sistemaCuentas.buscarUsuario(nombreGanador);
            if (ganador != null) {
                ganador.setPuntos(ganador.getPuntos() + 3);
                // Si tiene un método para guardar el estado del usuario en su lógica:
                // sistemaCuentas.actualizarUsuario(ganador); 
            }

            // 3. Crear mensaje y guardar el evento en el log
            String logMensaje = String.format("RETIRO: %s se ha retirado. Victoria y 3 puntos para %s.",
                    nombrePerdedor, nombreGanador);

            // 🚨 Guardar log en el arraylist de InterfaceCuentas 🚨
            if (sistemaCuentas != null) {
                // Asumo que InterfaceCuentas tiene un método guardarLog(String)
                sistemaCuentas.getLogsDeUsuario(logMensaje);
            }

            // 4. Mostrar mensaje de fin de partida (con el formato solicitado)
            JOptionPane.showMessageDialog(this,
                    String.format("%s SE HA RETIRADO, FELICIDADES %s, HAS GANADO 3 PUNTOS",
                            nombrePerdedor.toUpperCase(), nombreGanador.toUpperCase()),
                    "Partida Terminada por Retiro",
                    JOptionPane.INFORMATION_MESSAGE);

            // 5. Volver al menú principal
            this.dispose(); // Cierra la ventana del Tablero

            // 🚨 CORRECCIÓN DEL RETORNO: Re-instanciar Menu_Principal 🚨
            // Usamos el campo de clase 'usuarioActual' que debe estar disponible en Tablero
            if (usuarioActual != null) {
                Menu_Principal mp = new Menu_Principal(sistemaCuentas, menuReferencia);
                mp.iniciarMenu(usuarioActual);
            } else {
                // Si por alguna razón el usuario actual es nulo, volvemos a la ventana de login.
                menuReferencia.showMenu();
            }
        }
    }

    // =======================================================================
    // --- MÉTODOS DE CONTROL DE TURNO ACCESIBLES EXTERNAMENTE (Menu_Principal) ---
    // =======================================================================
    /**
     * Requerido por Menu_Principal para iniciar la lógica de juego. Calcula los
     * giros posibles e inicia la ruleta.
     */
    public void iniciarTurno() {
        if (turnoEnCurso) {
            return;
        }

        // Calcula el número de giros que el jugador PUEDE hacer (1, 2 o 3)
        // Se usa -1 porque el primer giro se cuenta al iniciarlo.
        girosRestantes = calcularGirosBase() - 1;
        turnoEnCurso = true;
        piezaDelTurno = null;

        botonRuleta.setText("DETENER RULETA");
        actualizarEstadoRuleta();

        // 2. Llamada a iniciarGiroDeTurno
        ruletaPanel.iniciarGiroDeTurno();
    }

    /**
     * Requerido por Menu_Principal para verificar el estado del turno.
     */
    public boolean isTurnoEnCurso() {
        return turnoEnCurso;
    }

    // -----------------------------------------------------------------------
    // --- LÓGICA DE RULETA Y CONTROL DE TURNO ---
    // -----------------------------------------------------------------------
    /**
     * Calcula los giros base permitidos según las piezas perdidas.
     */
    private int calcularGirosBase() {
        // Asumimos 6 piezas principales iniciales (3 tipos x 2 de cada uno = 6)
        int piezasIniciales = 6;
        int piezasActuales = 0;

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                Pieza p = estadoTablero[i][j];
                // Solo cuenta las piezas principales (no Zombies) del jugador actual
                if (p != null && p.getColor().equals(jugadorActualColor) && !p.getNombre().equals("Zombie")) {
                    piezasActuales++;
                }
            }
        }

        int piezasPerdidas = piezasIniciales - piezasActuales;

        if (piezasPerdidas >= 4) {
            return 3; // 4+ piezas perdidas -> 3 giros
        } else if (piezasPerdidas >= 2) {
            return 2; // 2 o 3 piezas perdidas -> 2 giros
        } else {
            return 1; // 0 o 1 pieza perdida -> 1 giro
        }
    }

    /**
     * Maneja el resultado de la ruleta y la lógica de reintento.
     */
    public void manejarResultadoRuleta(String resultadoPieza) {
        if (!turnoEnCurso) {
            return;
        }

        if (jugadorTienePieza(resultadoPieza)) {
            // Éxito: El jugador tiene la pieza y puede moverla.
            piezaDelTurno = resultadoPieza;
            botonRuleta.setText("Mueve: " + piezaDelTurno);
            JOptionPane.showMessageDialog(this, "¡Puedes mover cualquier pieza: " + piezaDelTurno + "!", "Pieza Seleccionada", JOptionPane.INFORMATION_MESSAGE);
            girosRestantes = 0;
            actualizarEstadoRuleta();
        } else {
            // Fracaso: El jugador no tiene la pieza seleccionada.
            if (girosRestantes > 0) {
                // Reintento: Gasta un giro extra.
                girosRestantes--;
                JOptionPane.showMessageDialog(this, "No tienes la pieza '" + resultadoPieza + "'. Reintentando... Giros restantes: " + (girosRestantes + 1), "Reintento", JOptionPane.WARNING_MESSAGE);
                botonRuleta.setText("GIRAR RULETA");
                actualizarEstadoRuleta();
                ruletaPanel.iniciarGiroDeTurno(); // Nuevo giro
            } else {
                // Fracaso total: Pierde el turno.
                JOptionPane.showMessageDialog(this, "No tienes la pieza '" + resultadoPieza + "'. Giros agotados. Pierdes el turno.", "Turno Perdido", JOptionPane.ERROR_MESSAGE);
                finalizarTurno();
            }
        }
    }

    private boolean jugadorTienePieza(String nombrePieza) {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                Pieza p = estadoTablero[i][j];
                if (p != null && p.getColor().equals(jugadorActualColor) && p.getNombre().equals(nombrePieza)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void finalizarTurno() {
        resetBorders();
        cambiarTurno(true); // Se fuerza el cambio de turno
        piezaDelTurno = null;
        turnoEnCurso = false;
        girosRestantes = 0;
        botonRuleta.setText("GIRAR RULETA");
        actualizarEstadoRuleta();
        actualizarTableroVisual();
    }

    private void actualizarEstadoRuleta() {
        girosRestantesLabel.setText("Giros restantes: " + (girosRestantes + 1));

        // Controla el botón de la ruleta:
        if (ruletaPanel.isGirando()) {
            botonRuleta.setEnabled(true); // Permite Detener
        } else if (piezaDelTurno != null && turnoEnCurso) {
            botonRuleta.setEnabled(false); // Esperando el movimiento de pieza, no girar
        } else {
            botonRuleta.setEnabled(true); // Permite Iniciar el giro o reintentar
        }
    }

    // -----------------------------------------------------------------------
    // --- MANEJO DE CLIC DE TABLERO Y PIEZA SELECCIONADA ---
    // -----------------------------------------------------------------------
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (!turnoEnCurso || piezaDelTurno == null) {
                JOptionPane.showMessageDialog(this, "Debes girar la ruleta y obtener una pieza para iniciar tu turno.", "Girar Ruleta", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String comando = e.getActionCommand();
            String[] coordenadas = comando.split(",");
            int r = Integer.parseInt(coordenadas[0]);
            int c = Integer.parseInt(coordenadas[1]);

            ManejoClick(r, c);
        } catch (Exception ex) {
            // Manejo de excepción más robusto
            System.err.println("Error al manejar el clic: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado al manejar el clic: " + ex.getMessage(), "Error de Juego", JOptionPane.ERROR_MESSAGE);
            piezaSeleccionada = null;
            selectedRow = -1;
            selectedCol = -1;
            actualizarTableroVisual();
        }
    }

    private void ManejoClick(int Fila, int Columna) {

        resetBorders();
        boolean accionEjecutada = false;

        if (piezaSeleccionada == null) {
            // FASE 1: Selección de Pieza Propia

            Pieza piezaEnCasilla = estadoTablero[Fila][Columna];

            if (piezaEnCasilla != null
                    && piezaEnCasilla.getColor().equals(jugadorActualColor)
                    && piezaEnCasilla.getNombre().equals(piezaDelTurno) // <-- VALIDACIÓN CLAVE
                    ) {
                if (piezaEnCasilla.getNombre().equals("Zombie")) {
                    JOptionPane.showMessageDialog(this, "El Zombie no puede ser movido ni atacado directamente.", "Reglas del Zombie", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                piezaSeleccionada = piezaEnCasilla;
                selectedRow = Fila;
                selectedCol = Columna;

                // ... [Lógica de Resaltado de Movimientos] ...
                int maxDistanciaMovimiento = piezaSeleccionada.getNombre().equals("HombreLobo") ? 2 : 1;
                int maxDistanciaAtaque = piezaSeleccionada.getNombre().equals("Necromancer") ? 2 : 1;

                for (int FilaF = 0; FilaF < FILAS; FilaF++) {
                    for (int ColumnaF = 0; ColumnaF < COLUMNAS; ColumnaF++) {

                        JButton targetButton = botonesTablero[FilaF][ColumnaF];
                        Pieza destino = estadoTablero[FilaF][ColumnaF];

                        if (destino == null) {
                            if (isPathValid(selectedRow, selectedCol, FilaF, ColumnaF, maxDistanciaMovimiento)) {
                                targetButton.setIcon(null);
                                targetButton.setBackground(HIGHLIGHT_COLOR);
                            }
                        } else if (!destino.getColor().equals(jugadorActualColor)) {
                            if (isPathValid(selectedRow, selectedCol, FilaF, ColumnaF, maxDistanciaAtaque)
                                    || (piezaSeleccionada.getNombre().equals("Necromancer") && isAdjacentToFriendlyZombie(FilaF, ColumnaF, jugadorActualColor))) {
                                targetButton.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
                            }
                        }
                    }
                }

            } else {
                if (piezaEnCasilla != null && piezaEnCasilla.getColor().equals(jugadorActualColor)) {
                    JOptionPane.showMessageDialog(this, "Solo puedes mover piezas del tipo: " + piezaDelTurno, "Pieza Incorrecta", JOptionPane.WARNING_MESSAGE);
                }
                selectedRow = -1;
                selectedCol = -1;
            }
        } else {
            // FASE 2: Movimiento o Ataque a Casilla Destino
            if (Fila == selectedRow && Columna == selectedCol) {
                accionEjecutada = true;
            } else {
                Pieza destino = estadoTablero[Fila][Columna];

                final int distR = Math.abs(selectedRow - Fila);
                final int distC = Math.abs(selectedCol - Columna);
                final int distanciaTotal = Math.max(distR, distC);

                int maxDistanciaMovimiento = piezaSeleccionada.getNombre().equals("HombreLobo") ? 2 : 1;
                int maxDistanciaAtaque = piezaSeleccionada.getNombre().equals("Necromancer") ? 2 : 1;
                String nombrePieza = piezaSeleccionada.getNombre();

                // Intento de Movimiento o Conjuro (Casilla vacía)
                if (destino == null) {

                    if (nombrePieza.equals("Necromancer")) {
                        Necromancer muerte = (Necromancer) piezaSeleccionada;
                        int choiceIndex = -1;

                        if (distanciaTotal == 1 && isPathValid(selectedRow, selectedCol, Fila, Columna, 1)) {
                            Object[] options = {"Mover a casilla", "Conjurar Zombie"};
                            choiceIndex = JOptionPane.showOptionDialog(this, "¿Qué desea hacer con la Necromancer?", "Acción de Necromancer", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        } else {
                            Object[] options = {"Conjurar Zombie", "Cancelar"};
                            int distantChoice = JOptionPane.showOptionDialog(this, "Esta casilla está fuera del rango de movimiento.\n¿Desea Conjurar un Zombie aquí?", "Conjurar a Distancia", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                            if (distantChoice == 0) {
                                choiceIndex = 1;
                            } else {
                                choiceIndex = -1;
                            }
                        }

                        if (choiceIndex == 0) {
                            accionEjecutada = moverPieza(selectedRow, selectedCol, Fila, Columna);
                        } else if (choiceIndex == 1) {
                            // Se asume que Necromancer.conjurarZombie actualiza el estadoTablero
                            muerte.conjurarZombie(this, Fila, Columna);
                            cambiarTurno(false); // No cambiará el turno
                            accionEjecutada = true;
                        }

                    } else if (isPathValid(selectedRow, selectedCol, Fila, Columna, maxDistanciaMovimiento)) {
                        accionEjecutada = moverPieza(selectedRow, selectedCol, Fila, Columna);
                    } else {
                        JOptionPane.showMessageDialog(this, "Movimiento no válido para esta pieza o distancia.", "Error de Reglas", JOptionPane.WARNING_MESSAGE);
                    }
                } // Intento de Ataque (Casilla ocupada)
                else {
                    if (!destino.getColor().equals(piezaSeleccionada.getColor())) {

                        boolean isStandardAttackPossible = isPathValid(selectedRow, selectedCol, Fila, Columna, maxDistanciaAtaque);
                        boolean isZombieAttackPossible = nombrePieza.equals("Necromancer") && isAdjacentToFriendlyZombie(Fila, Columna, jugadorActualColor);

                        if (isStandardAttackPossible || isZombieAttackPossible) {

                            String ataqueTipo = "0";
                            boolean isLongRangeZombieAttack = isZombieAttackPossible && !isStandardAttackPossible;

                            if (nombrePieza.equals("Vampiro")) {
                                Object[] options = {"Ataque Normal", "Ataque Especial"};
                                int choice = JOptionPane.showOptionDialog(this, "¿Qué ataque desea usar?", "Selección de Ataque", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                                if (choice == 0) {
                                    ataqueTipo = "1";
                                } else if (choice == 1) {
                                    ataqueTipo = "2";
                                }
                            } else if (nombrePieza.equals("Necromancer")) {
                                if (isLongRangeZombieAttack) {
                                    ataqueTipo = "2";
                                    JOptionPane.showMessageDialog(this, "El objetivo está fuera de rango. Se forzará el Ataque Zombie (Especial).", "Ataque Zombie a Distancia", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    Object[] options = {"Ataque Normal", "Ataque Especial (Lanza/Zombie)"};
                                    int choice = JOptionPane.showOptionDialog(this, "¿Qué ataque desea usar?", "Selección de Ataque", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                                    if (choice == 0) {
                                        ataqueTipo = "1";
                                    } else if (choice == 1) {
                                        ataqueTipo = "2";
                                    }
                                }
                            } else {
                                ataqueTipo = "1";
                            }

                            if ("1".equals(ataqueTipo)) {
                                if (distanciaTotal > 1) {
                                    JOptionPane.showMessageDialog(this, "Ataque Normal: Solo se puede hacer a piezas adyacentes.", "Error de Rango", JOptionPane.WARNING_MESSAGE);
                                } else {
                                    piezaSeleccionada.atacar(destino);
                                    if (destino.getVida() <= 0) {
                                        estadoTablero[Fila][Columna] = null;
                                    }
                                    cambiarTurno(false);
                                    accionEjecutada = true;
                                }

                            } else if ("2".equals(ataqueTipo) && (nombrePieza.equals("Vampiro") || nombrePieza.equals("Necromancer"))) {

                                if (nombrePieza.equals("Necromancer")) {
                                    // Se requiere casting a Necromancer para el método especial
                                    accionEjecutada = manejarAtaqueEspecialNecromancer((Necromancer) piezaSeleccionada, destino, Fila, Columna, selectedRow, selectedCol, isLongRangeZombieAttack);
                                } else {
                                    // Se requiere casting a Vampiro
                                    ((Vampiro) piezaSeleccionada).ataqueEspecial(destino, Fila, Columna, this);
                                    accionEjecutada = true;
                                }

                                if (accionEjecutada && destino.getVida() <= 0) {
                                    estadoTablero[Fila][Columna] = null;
                                }

                                if (accionEjecutada) {
                                    cambiarTurno(false);
                                }

                            } else if (!"0".equals(ataqueTipo)) {
                                JOptionPane.showMessageDialog(this, "Ataque cancelado o inválido.", "Ataque Inválido", JOptionPane.WARNING_MESSAGE);
                            }

                        } else {
                            JOptionPane.showMessageDialog(this, "Ataque no válido para esta pieza o distancia.", "Error de Reglas", JOptionPane.WARNING_MESSAGE);
                        }

                    } else if (destino.getColor().equals(piezaSeleccionada.getColor())) {
                        JOptionPane.showMessageDialog(this, "No puedes atacar a una pieza de tu mismo color.", "Ataque Inválido", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }

        if (accionEjecutada) {
            // El movimiento o ataque fue exitoso, finaliza el turno
            piezaSeleccionada = null;
            selectedRow = -1;
            selectedCol = -1;
            finalizarTurno();
        } else if (piezaSeleccionada != null) {
            botonesTablero[selectedRow][selectedCol].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        }

        actualizarTableroVisual();
    }

    // Este método permite que ciertas acciones (como Conjurar) no cambien el turno
    private void cambiarTurno(boolean forzarCambio) {
        if (forzarCambio) {
            jugadorActualColor = jugadorActualColor.equals("Blanco") ? "Negro" : "Blanco";
            if (turnoLabel != null) {
                turnoLabel.setText("Turno: " + jugadorActualColor + " | Controles: Ruleta y Mensajes");
            }
        }
    }

    // Sobreescribir moverPieza para no cambiar el turno, ya que se hace en finalizarTurno()
    private boolean moverPieza(int InicioF, int InicioC, int FilaF, int ColumnaF) {
        if (estadoTablero[FilaF][ColumnaF] != null) {
            return false;
        }

        Pieza pieza = estadoTablero[InicioF][InicioC];
        estadoTablero[FilaF][ColumnaF] = pieza;
        estadoTablero[InicioF][InicioC] = null;

        return true;
    }

    // -----------------------------------------------------------------------
    // --- MÉTODOS AUXILIARES DE INICIALIZACIÓN Y VISUALIZACIÓN ---
    // -----------------------------------------------------------------------
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
        // Debes asegurarte de que las clases HombreLobo, Vampiro, Necromancer y Zombie
        // existen y extienden (o implementan) la clase Pieza.
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

    private void cargarIconos() {
        final int iconSize = 125;

        try {
            // Asegúrate de que las imágenes HombreLobo.jpg, Vampiro.jpg, etc. existen en tu classpath
            iconHombreLoboNegro = crearIconoEscalado("HombreLobo.jpg", iconSize);
            iconVampiroNegro = crearIconoEscalado("Vampiro.jpg", iconSize);
            iconNecromancerNegro = crearIconoEscalado("Nercromancer.jpg", iconSize); // NOTA: Posible error tipográfico ('Nercromancer' en lugar de 'Necromancer')
            iconZombieNegro = crearIconoEscalado("Zombie.jpg", iconSize);

            iconHombreLoboBlanco = crearIconoEscalado("HombreLoboB.jpg", iconSize);
            iconVampiroBlanco = crearIconoEscalado("VampiroB.jpg", iconSize);
            iconNecromancerBlanco = crearIconoEscalado("NecromancerB.jpg", iconSize);
            iconZombieBlanco = crearIconoEscalado("ZombieB.jpg", iconSize);

        } catch (Exception e) {
            // Si hay un error, el juego puede funcionar sin iconos, o fallar si son requeridos
            System.err.println("Advertencia: Fallo al cargar los iconos del tablero. Asegúrate de que los archivos estén en la carpeta /Imagenes/: " + e.getMessage());
        }
    }

    private ImageIcon crearIconoEscalado(String path, int size) {
        // Asumiendo que las imágenes del tablero están en la carpeta /Imagenes/
        String resourcePath = "/Imagenes/" + path;

        try {
            URL imageUrl = getClass().getResource(resourcePath);

            if (imageUrl == null) {
                // Si la ruta no funciona, intenta la raíz del classpath
                imageUrl = getClass().getResource("/" + path);
            }

            if (imageUrl == null) {
                throw new RuntimeException("Recurso de imagen no encontrado: " + path);
            }

            ImageIcon originalIcon = new ImageIcon(imageUrl);
            Image image = originalIcon.getImage();
            Image scaledImage = image.getScaledInstance(size, size, Image.SCALE_SMOOTH);

            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            throw new RuntimeException("Fallo al crear icono escalado para: " + path + " - " + e.getMessage());
        }
    }

    public void actualizarTableroVisual() {
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                Pieza pieza = estadoTablero[i][j];

                if (pieza == null) {
                    botonesTablero[i][j].setIcon(null);
                    botonesTablero[i][j].setText(null);
                } else {
                    setPiezaVisual(i, j, pieza.getNombre(), pieza.getColor());
                }
            }
        }

        this.revalidate();
        this.repaint();
    }

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

    public void setPieza(Pieza pieza, int fila, int columna) {
        if (fila >= 0 && fila < FILAS && columna >= 0 && columna < COLUMNAS) {
            estadoTablero[fila][columna] = pieza;
        }
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

    // [MovimientoValido y isPathValid - Lógica de Movimiento/Ruta]
    private boolean MovimientoValido(int FilaA, int ColumnaA, int FilaF, int ColumnaF, int maxDistancia, int drFixed, int dcFixed, int currentDistancia) {
        if (FilaA == FilaF && ColumnaA == ColumnaF) {
            return true;
        }

        if (currentDistancia >= maxDistancia) {
            return false;
        }

        int nextR = FilaA + drFixed;
        int nextC = ColumnaA + dcFixed;

        if (nextR >= 0 && nextR < FILAS && nextC >= 0 && nextC < COLUMNAS) {

            if (nextR == FilaF && nextC == ColumnaF) {
                return true;
            }

            if (estadoTablero[nextR][nextC] == null) {
                return MovimientoValido(nextR, nextC, FilaF, ColumnaF, maxDistancia, drFixed, dcFixed, currentDistancia + 1);
            }
        }

        return false;
    }

    private boolean isPathValid(int InicioF, int InicioC, int FilaF, int ColumnaF, int maxDistancia) {
        int distR = Math.abs(InicioF - FilaF);
        int distC = Math.abs(InicioC - ColumnaF);
        int distanciaTotal = Math.max(distR, distC);

        if (distanciaTotal == 0 || distanciaTotal > maxDistancia) {
            return false;
        }

        boolean isStraight = (distR == 0) || (distC == 0) || (distR == distC);

        if (!isStraight) {
            return false;
        }

        if (maxDistancia == 1 || distanciaTotal == 1) {
            return true;
        }

        int drFixed = 0;
        if (FilaF > InicioF) {
            drFixed = 1;
        } else if (FilaF < InicioF) {
            drFixed = -1;
        }

        int dcFixed = 0;
        if (ColumnaF > InicioC) {
            dcFixed = 1;
        } else if (ColumnaF < InicioC) {
            dcFixed = -1;
        }

        int firstStepR = InicioF + drFixed;
        int firstStepC = InicioC + dcFixed;

        // Se verifica que la casilla del primer paso no esté bloqueada, si no es el destino final.
        if (firstStepR == FilaF && firstStepC == ColumnaF) {
            return true; // El destino es el primer paso
        }

        if (estadoTablero[firstStepR][firstStepC] == null) {
            // Llama a MovimientoValido para verificar el resto del camino
            return MovimientoValido(firstStepR, firstStepC, FilaF, ColumnaF, maxDistancia, drFixed, dcFixed, 1);
        }

        return false; // El primer paso está bloqueado por otra pieza
    }

    private boolean manejarAtaqueEspecialNecromancer(Necromancer muerte, Pieza destino, int r, int c, int necR, int necC, boolean forzarZombieAttack) {
        String tipoAtaque = "0";

        if (forzarZombieAttack) {
            tipoAtaque = "2";
        } else {
            Object[] options = {"Lanza (Rango 2, ignora escudo)", "Zombie Attack (Adyacente a Zombie, Daño 1)"};
            int choice = JOptionPane.showOptionDialog(this, "Seleccione ataque especial de Necromancer:", "Ataque Especial", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (choice == 0) {
                tipoAtaque = "1";
            } else if (choice == 1) {
                tipoAtaque = "2";
            }
        }

        if ("1".equals(tipoAtaque)) {
            int dist = Math.max(Math.abs(necR - r), Math.abs(necC - c));
            if (dist == 2 && isPathValid(necR, necC, r, c, 2)) {
                muerte.lanzarLanza(destino);
                if (destino.getVida() <= 0) {
                    estadoTablero[r][c] = null;
                }
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Lanza: Rango debe ser exactamente 2 casillas con camino libre en línea recta.", "Error de Rango", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } else if ("2".equals(tipoAtaque)) {
            if (isAdjacentToFriendlyZombie(r, c, muerte.getColor())) {

                muerte.ataqueZombie(destino);

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

    private boolean isAdjacentToFriendlyZombie(int r, int c, String color) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }
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
}
