/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_progra2;

import Logica.Usuarios;
import Logica.InterfaceCuentas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author Nathan
 */
/**
 * Tablero completo (con cambios solicitados): - constructor recibe la
 * referencia a Menu_Principal para poder volver a él - logs de eventos
 * relevantes se guardan en ambos jugadores - panel lateral de piezas eliminadas
 * ajustado - ruleta/turnos/ataques sin cambios funcionales significativos
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

    private int piezasBlancasRestantes = 0;
    private int piezasNegrasRestantes = 0;

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

    private InterfaceCuentas sistemaCuentas;
    // Referencia a Menu_Principal que inició la partida (para volver al menú principal con el mismo usuario)
    private Menu_Principal menuPrincipalReferencia;
    private Menu menuReferencia;

    // Piezas Eliminadas
    private JPanel piezasNegrasEliminadasPanel;
    private JPanel piezasBlancasEliminadasPanel;

    private ArrayList<Image> imagenesNegrasEliminadas = new ArrayList<>();
    private ArrayList<Image> imagenesBlancasEliminadas = new ArrayList<>();

    private static final int MARGEN_LATERAL = 20;

    public Tablero(String jugadorBlanco, String jugadorNegro, InterfaceCuentas sistemaCuentas, Menu_Principal menuPrincipalReferencia) {
        this.estadoTablero = new Pieza[FILAS][COLUMNAS];
        this.sistemaCuentas = sistemaCuentas;
        this.nombreJugadorBlanco = jugadorBlanco;
        this.nombreJugadorNegro = jugadorNegro;
        this.menuPrincipalReferencia = menuPrincipalReferencia;
        this.usuarioActual = sistemaCuentas.buscarUsuario(nombreJugadorBlanco);

        cargarIconos();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenHeight = screenSize.height;
        int windowHeight = screenHeight - 50;
        int windowWidth = windowHeight + 550;

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

        // ----------------------------------------------------------------
        // INICIO: AJUSTE DEL PANEL LATERAL IZQUIERDO (Piezas Eliminadas)
        // ----------------------------------------------------------------
        JPanel panelLateralIzquierdo = new JPanel();
        panelLateralIzquierdo.setLayout(new BoxLayout(panelLateralIzquierdo, BoxLayout.Y_AXIS));
        panelLateralIzquierdo.setPreferredSize(new Dimension(200, 0));
        panelLateralIzquierdo.setMinimumSize(new Dimension(150, 0));
        panelLateralIzquierdo.setBorder(BorderFactory.createEmptyBorder(MARGEN_LATERAL, MARGEN_LATERAL, MARGEN_LATERAL, MARGEN_LATERAL));

        piezasNegrasEliminadasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        piezasNegrasEliminadasPanel.setPreferredSize(new Dimension(180, 2500));
        piezasNegrasEliminadasPanel.setBorder(BorderFactory.createTitledBorder("Negras Eliminadas"));

        JScrollPane scrollNegras = new JScrollPane(piezasNegrasEliminadasPanel);
        scrollNegras.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollNegras.setMinimumSize(new Dimension(150, 100));

        piezasBlancasEliminadasPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        piezasBlancasEliminadasPanel.setPreferredSize(new Dimension(180, 2500));
        piezasBlancasEliminadasPanel.setBorder(BorderFactory.createTitledBorder("Blancas Eliminadas"));

        JScrollPane scrollBlancas = new JScrollPane(piezasBlancasEliminadasPanel);
        scrollBlancas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollBlancas.setMinimumSize(new Dimension(150, 100));

        JSplitPane splitEliminadas = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollNegras, scrollBlancas);
        splitEliminadas.setResizeWeight(0.5);
        splitEliminadas.setDividerSize(5);
        panelLateralIzquierdo.add(splitEliminadas);

        // ----------------------------------------------------------------
        // FIN: PANEL LATERAL IZQUIERDO
        // ----------------------------------------------------------------
        // Panel de controles inferior
        JPanel panelControles = new JPanel();
        turnoLabel = new JLabel("Turno: " + jugadorActualColor);
        panelControles.add(turnoLabel);

        // Panel lateral para ruleta y botones
        JPanel panelLateral = new JPanel();
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelLateral.setPreferredSize(new Dimension(265, windowHeight));

        JLabel ruletaTitulo = new JLabel("Ruleta de Piezas");
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

        // Añadir todos los paneles al BorderLayout
        add(panelPrincipal, BorderLayout.CENTER);
        add(panelControles, BorderLayout.SOUTH);
        add(panelLateral, BorderLayout.EAST);
        add(panelLateralIzquierdo, BorderLayout.WEST);

        setLocationRelativeTo(null);
        setVisible(true);

        inicializarPiezasEnTableroLogico();
        actualizarTableroVisual();
        actualizarEstadoRuleta();
    }

    private void rendirse() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Deseas rendirte? El contrincante obtendrá 3 puntos.",
                "Confirmar Rendirse",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String nombrePerdedor = jugadorActualColor.equals("Blanco") ? nombreJugadorBlanco : nombreJugadorNegro;
            String nombreGanador = jugadorActualColor.equals("Blanco") ? nombreJugadorNegro : nombreJugadorBlanco;

            Usuarios ganador = sistemaCuentas.buscarUsuario(nombreGanador);
            String logMensaje = String.format("RETIRO: %s se ha retirado. Victoria y 3 puntos para %s.",
                    nombrePerdedor, nombreGanador);

            if (ganador != null) {
                nombrePerdedor = jugadorActualColor.equals("Blanco") ? nombreJugadorBlanco : nombreJugadorNegro;
                sistemaCuentas.finalizarJuego(nombreGanador, nombrePerdedor, true);
            }

            JOptionPane.showMessageDialog(this,
                    String.format("%s SE HA RETIRADO, FELICIDADES %s, HAS GANADO 3 PUNTOS",
                            nombrePerdedor.toUpperCase(), nombreGanador.toUpperCase()),
                    "Partida Terminada por Retiro",
                    JOptionPane.INFORMATION_MESSAGE);

            this.dispose();
            if (menuReferencia != null) {
                menuReferencia.showMenu();
            }
        }
    }

    private int contarPiezas(String color) {
        int contador = 0;
        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                Pieza p = estadoTablero[i][j];
                if (p != null && color.equals(p.getColor()) && !"Zombie".equals(p.getNombre())) {
                    contador++;
                }
            }
        }
        return contador;
    }

    public void destruirPieza(Pieza piezaEliminada) {
        if (piezaEliminada == null) {
            return;
        }

        boolean encontradoYRemovido = false;
        for (int i = 0; i < FILAS && !encontradoYRemovido; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                if (estadoTablero[i][j] == piezaEliminada) {
                    estadoTablero[i][j] = null;
                    encontradoYRemovido = true;
                    break;
                }
            }
        }

        if (piezaEliminada.getColor().equals("Blanco") && !piezaEliminada.getNombre().equals("Zombie")) {
            piezasBlancasRestantes = Math.max(0, piezasBlancasRestantes - 1);
        } else if (piezaEliminada.getColor().equals("Negro") && !piezaEliminada.getNombre().equals("Zombie")) {
            piezasNegrasRestantes = Math.max(0, piezasNegrasRestantes - 1);
        }

        if (!"Zombie".equals(piezaEliminada.getNombre())) {
            if (piezaEliminada.getColor().equals("Blanco")) {
                if (piezaEliminada.getImagen() != null) {
                    imagenesBlancasEliminadas.add(piezaEliminada.getImagen());
                }
            } else if (piezaEliminada.getColor().equals("Negro")) {
                if (piezaEliminada.getImagen() != null) {
                    imagenesNegrasEliminadas.add(piezaEliminada.getImagen());
                }
            }
            actualizarPiezasEliminadas();
        }

        // Guardar evento en logs de ambos jugadores (garantiza que ambos vean lo sucedido)
        String msg = String.format("ELIMINACIÓN: %s (%s) fue destruida.", piezaEliminada.getNombre(), piezaEliminada.getColor());
        Usuarios u1 = sistemaCuentas.buscarUsuario(nombreJugadorBlanco);
        Usuarios u2 = sistemaCuentas.buscarUsuario(nombreJugadorNegro);
        if (u1 != null) {
            u1.agregarLog(msg);
        }
        if (u2 != null) {
            u2.agregarLog(msg);
        }

        verificarVictoria();
    }

    private void verificarVictoria() {
        int piezasBlancas = contarPiezas("Blanco");
        int piezasNegras = contarPiezas("Negro");

        String nombreGanador = null;
        String nombrePerdedor = null;

        if (piezasBlancas <= 0 && piezasNegras <= 0) {
            JOptionPane.showMessageDialog(this, "Ambos bandos han quedado sin piezas principales. Empate.", "Empate", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            if (menuPrincipalReferencia != null) {
                Usuarios retorno = sistemaCuentas.buscarUsuario(nombreJugadorBlanco);
                if (retorno != null) {
                    menuPrincipalReferencia.iniciarMenu(retorno);
                }
            }
            return;
        } else if (piezasBlancas <= 0) {
            nombreGanador = nombreJugadorNegro;
            nombrePerdedor = nombreJugadorBlanco;
        } else if (piezasNegras <= 0) {
            nombreGanador = nombreJugadorBlanco;
            nombrePerdedor = nombreJugadorNegro;
        }

        if (nombreGanador != null) {
            Usuarios ganador = sistemaCuentas != null ? sistemaCuentas.buscarUsuario(nombreGanador) : null;
            Usuarios perdedor = sistemaCuentas != null ? sistemaCuentas.buscarUsuario(nombrePerdedor) : null;
            String logMensaje = String.format("VICTORIA: %s venció a %s por eliminación de piezas principales. Ganó 3 puntos.", nombreGanador, nombrePerdedor);

            if (ganador != null) {
                ganador.sumarPuntos(3);
            }
            if (ganador != null) {
                ganador.agregarLog(logMensaje);
            }
            if (perdedor != null) {
                perdedor.agregarLog("DERROTA: Perdiste contra " + nombreGanador + " por eliminación.");
            }

            JOptionPane.showMessageDialog(this,
                    String.format("%s VENCIÓ A %s, FELICIDADES HAS GANADO 3 PUNTOS",
                            nombreGanador.toUpperCase(), nombrePerdedor.toUpperCase()),
                    "¡Victoria!",
                    JOptionPane.INFORMATION_MESSAGE);

            this.dispose();

            if (menuPrincipalReferencia != null) {
                Usuarios retorno = sistemaCuentas.buscarUsuario(nombreJugadorBlanco);
                if (retorno != null) {
                    menuPrincipalReferencia.iniciarMenu(retorno);
                }
            }
        }
    }

    private void actualizarPiezasEliminadas() {
        piezasNegrasEliminadasPanel.removeAll();
        piezasBlancasEliminadasPanel.removeAll();

        for (Image img : imagenesNegrasEliminadas) {
            if (img != null) {
                Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                piezasNegrasEliminadasPanel.add(new JLabel(new ImageIcon(scaledImg)));
            }
        }

        for (Image img : imagenesBlancasEliminadas) {
            if (img != null) {
                Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                piezasBlancasEliminadasPanel.add(new JLabel(new ImageIcon(scaledImg)));
            }
        }

        piezasNegrasEliminadasPanel.revalidate();
        piezasBlancasEliminadasPanel.revalidate();
        piezasNegrasEliminadasPanel.repaint();
        piezasBlancasEliminadasPanel.repaint();
    }

    private void finalizarPartidaPorVictoria(String nombreGanador, String nombrePerdedor) {

        Usuarios ganador = sistemaCuentas.buscarUsuario(nombreGanador);
        Usuarios perdedor = sistemaCuentas.buscarUsuario(nombrePerdedor);

        String logMensaje = String.format("VICTORIA: %s venció a %s por eliminación. Ganó 3 puntos.", nombreGanador, nombrePerdedor);

        if (ganador != null) {
            ganador.sumarPuntos(3);
            ganador.agregarLog(logMensaje);
        }
        if (perdedor != null) {
            perdedor.agregarLog("DERROTA: Perdiste contra " + nombreGanador);
        }

        // Guardar el log también en ambos por si requieres duplicarlo
        Usuarios u1 = sistemaCuentas.buscarUsuario(nombreJugadorBlanco);
        Usuarios u2 = sistemaCuentas.buscarUsuario(nombreJugadorNegro);
        if (u1 != null) {
            u1.agregarLog("PARTIDA: " + logMensaje);
        }
        if (u2 != null) {
            u2.agregarLog("PARTIDA: " + logMensaje);
        }

        JOptionPane.showMessageDialog(this,
                String.format("%s VENCIÓ A %s, FELICIDADES HAS GANADO 3 PUNTOS",
                        nombreGanador.toUpperCase(), nombrePerdedor.toUpperCase()),
                "¡Victoria!",
                JOptionPane.INFORMATION_MESSAGE);

        this.dispose();

        if (menuPrincipalReferencia != null) {
            Usuarios retorno = sistemaCuentas.buscarUsuario(nombreJugadorBlanco);
            if (retorno != null) {
                menuPrincipalReferencia.iniciarMenu(retorno);
            }
        }
    }

    // =======================================================================
    // --- MÉTODOS DE CONTROL DE TURNO ACCESIBLES EXTERNAMENTE (Menu_Principal) ---
    // =======================================================================
    public void iniciarTurno() {
        if (turnoEnCurso) {
            return;
        }

        girosRestantes = calcularGirosBase() - 1;
        turnoEnCurso = true;
        piezaDelTurno = null;

        botonRuleta.setText("DETENER RULETA");
        actualizarEstadoRuleta();

        ruletaPanel.iniciarGiroDeTurno();
    }

    public boolean isTurnoEnCurso() {
        return turnoEnCurso;
    }

    private int calcularGirosBase() {
        int piezasIniciales = 6;
        int piezasActuales = 0;

        for (int i = 0; i < FILAS; i++) {
            for (int j = 0; j < COLUMNAS; j++) {
                Pieza p = estadoTablero[i][j];
                if (p != null && p.getColor().equals(jugadorActualColor) && !p.getNombre().equals("Zombie")) {
                    piezasActuales++;
                }
            }
        }

        int piezasPerdidas = piezasIniciales - piezasActuales;

        if (piezasPerdidas >= 4) {
            return 3;
        } else if (piezasPerdidas >= 2) {
            return 2;
        } else {
            return 1;
        }
    }

    public void manejarResultadoRuleta(String resultadoPieza) {
        if (!turnoEnCurso) {
            return;
        }

        if (jugadorTienePieza(resultadoPieza)) {
            piezaDelTurno = resultadoPieza;
            botonRuleta.setText("Mueve: " + piezaDelTurno);
            JOptionPane.showMessageDialog(this, "¡Puedes mover cualquier pieza: " + piezaDelTurno + "!", "Pieza Seleccionada", JOptionPane.INFORMATION_MESSAGE);
            girosRestantes = 0;
            actualizarEstadoRuleta();
        } else {
            if (girosRestantes > 0) {
                girosRestantes--;
                JOptionPane.showMessageDialog(this, "No tienes la pieza '" + resultadoPieza + "'. Reintentando... Giros restantes: " + (girosRestantes + 1), "Reintento", JOptionPane.WARNING_MESSAGE);
                botonRuleta.setText("GIRAR RULETA");
                actualizarEstadoRuleta();
                ruletaPanel.iniciarGiroDeTurno();
            } else {
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
        cambiarTurno(true);
        piezaDelTurno = null;
        turnoEnCurso = false;
        girosRestantes = 0;
        botonRuleta.setText("GIRAR RULETA");
        actualizarEstadoRuleta();
        actualizarTableroVisual();
    }

    private void actualizarEstadoRuleta() {
        girosRestantesLabel.setText("Giros restantes: " + (girosRestantes + 1));

        if (ruletaPanel.isGirando()) {
            botonRuleta.setEnabled(true);
        } else if (piezaDelTurno != null && turnoEnCurso) {
            botonRuleta.setEnabled(false);
        } else {
            botonRuleta.setEnabled(true);
        }
    }

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

            Pieza piezaEnCasilla = estadoTablero[Fila][Columna];

            if (piezaEnCasilla != null
                    && piezaEnCasilla.getColor().equals(jugadorActualColor)
                    && piezaEnCasilla.getNombre().equals(piezaDelTurno)) {
                if (piezaEnCasilla.getNombre().equals("Zombie")) {
                    JOptionPane.showMessageDialog(this, "El Zombie no puede ser movido ni atacado directamente.", "Reglas del Zombie", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                piezaSeleccionada = piezaEnCasilla;
                selectedRow = Fila;
                selectedCol = Columna;

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
                            muerte.conjurarZombie(this, Fila, Columna);
                            cambiarTurno(false);
                            accionEjecutada = true;
                        }

                    } else if (isPathValid(selectedRow, selectedCol, Fila, Columna, maxDistanciaMovimiento)) {
                        accionEjecutada = moverPieza(selectedRow, selectedCol, Fila, Columna);
                    } else {
                        JOptionPane.showMessageDialog(this, "Movimiento no válido para esta pieza o distancia.", "Error de Reglas", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
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
                                        destruirPieza(destino);
                                        estadoTablero[Fila][Columna] = null;
                                    }
                                    cambiarTurno(false);
                                    accionEjecutada = true;
                                }

                            } else if ("2".equals(ataqueTipo) && (nombrePieza.equals("Vampiro") || nombrePieza.equals("Necromancer"))) {

                                if (nombrePieza.equals("Necromancer")) {
                                    accionEjecutada = manejarAtaqueEspecialNecromancer((Necromancer) piezaSeleccionada, destino, Fila, Columna, selectedRow, selectedCol, isLongRangeZombieAttack);
                                } else {
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
            piezaSeleccionada = null;
            selectedRow = -1;
            selectedCol = -1;
            finalizarTurno();
        } else if (piezaSeleccionada != null) {
            botonesTablero[selectedRow][selectedCol].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        }

        actualizarTableroVisual();
    }

    private void cambiarTurno(boolean forzarCambio) {
        if (forzarCambio) {
            jugadorActualColor = jugadorActualColor.equals("Blanco") ? "Negro" : "Blanco";
            if (turnoLabel != null) {
                turnoLabel.setText("Turno: " + jugadorActualColor + " | Controles: Ruleta y Mensajes");
            }
        }
    }

    private boolean moverPieza(int InicioF, int InicioC, int FilaF, int ColumnaF) {
        if (estadoTablero[FilaF][ColumnaF] != null) {
            return false;
        }

        Pieza pieza = estadoTablero[InicioF][InicioC];
        estadoTablero[FilaF][ColumnaF] = pieza;
        estadoTablero[InicioF][InicioC] = null;

        return true;
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

    private void cargarIconos() {
        final int iconSize = 125;

        try {
            iconHombreLoboNegro = crearIconoEscalado("HombreLoboN.jpg", iconSize);
            iconVampiroNegro = crearIconoEscalado("VampiroN.jpg", iconSize);
            iconNecromancerNegro = crearIconoEscalado("NecromancerN.jpg", iconSize);
            iconZombieNegro = crearIconoEscalado("ZombieN.jpg", iconSize);

            iconHombreLoboBlanco = crearIconoEscalado("HombreLoboB.jpg", iconSize);
            iconVampiroBlanco = crearIconoEscalado("VampiroB.jpg", iconSize);
            iconNecromancerBlanco = crearIconoEscalado("NecromancerB.jpg", iconSize);
            iconZombieBlanco = crearIconoEscalado("ZombieB.jpg", iconSize);

        } catch (Exception e) {
            System.err.println("Advertencia: Fallo al cargar los iconos del tablero. Asegúrate de que los archivos estén en la carpeta /Imagenes/: " + e.getMessage());
        }
    }

    private ImageIcon crearIconoEscalado(String path, int size) {
        String resourcePath = "/Imagenes/" + path;

        try {
            URL imageUrl = getClass().getResource(resourcePath);

            if (imageUrl == null) {
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

        if (firstStepR == FilaF && firstStepC == ColumnaF) {
            return true;
        }

        if (estadoTablero[firstStepR][firstStepC] == null) {
            return MovimientoValido(firstStepR, firstStepC, FilaF, ColumnaF, maxDistancia, drFixed, dcFixed, 1);
        }

        return false;
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
                    destruirPieza(destino);
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
                    destruirPieza(destino);
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
