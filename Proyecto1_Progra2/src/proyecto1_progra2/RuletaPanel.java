/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_progra2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Random;
import java.awt.Image;
import java.net.URL;

/**
 *
 * @author Nathan
 */

public class RuletaPanel extends JPanel implements ActionListener {

    private double anguloRotacion = 0;
    private final Timer timer;

    private int indiceFinalObjetivo = -1;
    private double anguloFinalObjetivo = 0;

    private final String[] nombresSecciones = {"HombreLobo", "Vampiro", "Necromancer", "HombreLobo", "Vampiro", "Necromancer"};
    private final String[] nombresArchivosBase = {"RH.png", "RV.png", "RN.png"};
    private final ImageIcon[] iconosSecciones = new ImageIcon[6];
    private final Image[] imagenesOriginales = new Image[6];
    
    // Variables para el marco
    private ImageIcon marcoRuletaIcon;
    private Image marcoRuletaImage;

    private boolean isGirando = false;
    private final Random random = new Random();

    private final Tablero tablero;

    // AJUSTES DE TAMAÑO (Ruleta pequeña dentro de un marco grande)
    private final int ICONO_SIZE = 50; 
    private final int RULETA_TOTAL_SIZE = 200; 
    private final int RULETA_RADIUS = RULETA_TOTAL_SIZE / 2;
    private final int ICONO_PLACEMENT_RADIUS = (int) (RULETA_TOTAL_SIZE * 0.35);
    
    private final double ANGULO_POR_SECCION = 60.0;
    private static final double PUNTERO_ANGULO_VISUAL = 270.0;
    
    private final int FRAME_SIZE = 300; // Tamaño del marco decorativo 

    private final Color COLOR_HOMBRE_LOBO = new Color(30, 30, 30);
    private final Color COLOR_VAMPIRO = new Color(150, 0, 0);
    private final Color COLOR_NECROMANCER = new Color(100, 100, 100);

    private final Color[] coloresSecciones = {
        COLOR_HOMBRE_LOBO, COLOR_VAMPIRO, COLOR_NECROMANCER,
        COLOR_HOMBRE_LOBO, COLOR_VAMPIRO, COLOR_NECROMANCER
    };

    public RuletaPanel(Tablero t) {
        this.tablero = t;
        // Tamaño ajustado para contener el marco de 300px y dejar espacio abajo.
        setPreferredSize(new Dimension(350, 500)); 
        setOpaque(false);
        setLayout(null); // Necesario para posicionar componentes externos (botón/etiqueta)

        timer = new Timer(20, this);
        cargarRecursos();
    }
    
    private void cargarRecursos() {
        ImageIcon[] iconosBase = new ImageIcon[3];
        try {
            // Carga de iconos de piezas
            for (int i = 0; i < nombresArchivosBase.length; i++) {
                String resourcePath = "/Imagenes/" + nombresArchivosBase[i];
                URL imageUrl = getClass().getResource(resourcePath);
                if (imageUrl == null) {
                    System.err.println("Error: No se encontró el recurso de imagen: " + nombresArchivosBase[i]);
                    iconosBase[i] = null;
                } else {
                    ImageIcon originalIcon = new ImageIcon(imageUrl);
                    Image scaledImage = originalIcon.getImage().getScaledInstance(ICONO_SIZE, ICONO_SIZE, Image.SCALE_SMOOTH);
                    iconosBase[i] = new ImageIcon(scaledImage);
                }
            }
            
            // Carga y escalado del marco
            String frameResourcePath = "/Imagenes/RuletaBase.png"; 
            URL frameImageUrl = getClass().getResource(frameResourcePath);
            if (frameImageUrl == null) {
                System.err.println("Error: No se encontró el recurso de imagen del marco: RuletaBase.png");
            } else {
                marcoRuletaIcon = new ImageIcon(frameImageUrl);
                marcoRuletaImage = marcoRuletaIcon.getImage().getScaledInstance(FRAME_SIZE, FRAME_SIZE, Image.SCALE_SMOOTH); 
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error grave al cargar iconos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Asignación de iconos a las 6 secciones
        iconosSecciones[0] = iconosBase[0]; 
        iconosSecciones[1] = iconosBase[1]; 
        iconosSecciones[2] = iconosBase[2]; 
        iconosSecciones[3] = iconosBase[0]; 
        iconosSecciones[4] = iconosBase[1]; 
        iconosSecciones[5] = iconosBase[2]; 

        for(int i = 0; i < 6; i++) {
            if(iconosSecciones[i] != null) {
                imagenesOriginales[i] = iconosSecciones[i].getImage();
            }
        }
    }
    
    public void iniciarGiroDeTurno() {
        if (!isGirando) {
            isGirando = true;
            indiceFinalObjetivo = random.nextInt(nombresSecciones.length);
            double centroSectorObjetivo = (indiceFinalObjetivo * ANGULO_POR_SECCION) + 300.0;
            centroSectorObjetivo %= 360.0;
            double rotacionNecesaria = (PUNTERO_ANGULO_VISUAL - centroSectorObjetivo);
            anguloFinalObjetivo = (rotacionNecesaria % 360.0 + 360.0) % 360.0;
            anguloRotacion = anguloFinalObjetivo + (random.nextInt(3) + 3) * 360.0;
            timer.start();
        }
    }

    public void detener() {
        if (isGirando) {
            timer.stop();
            isGirando = false;
            anguloRotacion = anguloFinalObjetivo;
            String piezaSeleccionada = nombresSecciones[indiceFinalObjetivo];
            indiceFinalObjetivo = -1;
            anguloFinalObjetivo = 0;
            repaint();
            JOptionPane.showMessageDialog(tablero, "¡La ruleta se detuvo en: " + piezaSeleccionada + "!", "Pieza Seleccionada", JOptionPane.INFORMATION_MESSAGE);
            tablero.manejarResultadoRuleta(piezaSeleccionada);
        }
    }

    public boolean isGirando() {
        return isGirando;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Centros y Coordenadas
        int panelCentroX = getWidth() / 2;
        int panelCentroY = (int) (getHeight() * 0.3); // Centro de la ruleta ubicado en el 30% del alto

        int ruletaDrawX = panelCentroX - RULETA_RADIUS;
        int ruletaDrawY = panelCentroY - RULETA_RADIUS;
        int ruletaCentroX = ruletaDrawX + RULETA_RADIUS;
        int ruletaCentroY = ruletaDrawY + RULETA_RADIUS;

        int frameX = panelCentroX - FRAME_SIZE / 2;
        int frameY = panelCentroY - FRAME_SIZE / 2;

        // GUARDAR LA TRANSFORMACIÓN ORIGINAL (SIN ROTACIÓN)
        AffineTransform originalTransform = g2d.getTransform();

        // 1. DIBUJAR FONDO DEL ÁREA DE LA RULETA (Fondo negro detrás del marco)
        g2d.setColor(new Color(30, 30, 30));
        g2d.fillRoundRect(frameX - 10, frameY - 10, FRAME_SIZE + 20, FRAME_SIZE + 40, 20, 20);

        // ----------------------------------------------------
        // INICIO DE LA ROTACIÓN (Solo aplica a Sectores e Iconos)
        g2d.rotate(Math.toRadians(anguloRotacion), ruletaCentroX, ruletaCentroY);
        
        // 2. DIBUJAR SECTORES DE COLOR (GIRAN)
        int anguloPorSeccion = 360 / nombresSecciones.length;
        for (int i = 0; i < nombresSecciones.length; i++) {
            g2d.setColor(coloresSecciones[i]);
            int startAngle = i * anguloPorSeccion - 90;
            g2d.fillArc(ruletaDrawX, ruletaDrawY, RULETA_TOTAL_SIZE, RULETA_TOTAL_SIZE, startAngle, anguloPorSeccion);
        }
        
        // 3. DIBUJAR ICONOS (GIRAN)
        for (int i = 0; i < nombresSecciones.length; i++) {
            Image icono = imagenesOriginales[i];
            if (icono != null) {
                double anguloCentralSector = (i * anguloPorSeccion) + (anguloPorSeccion / 2);
                double anguloRad = Math.toRadians(anguloCentralSector - 90);
                int x = (int) (ruletaCentroX + ICONO_PLACEMENT_RADIUS * Math.cos(anguloRad));
                int y = (int) (ruletaCentroY + ICONO_PLACEMENT_RADIUS * Math.sin(anguloRad));

                AffineTransform pieceTransform = g2d.getTransform();
                g2d.translate(x, y);
                g2d.drawImage(icono, -ICONO_SIZE / 2, -ICONO_SIZE / 2, ICONO_SIZE, ICONO_SIZE, this);
                g2d.setTransform(pieceTransform); // Restaurar después de dibujar el icono
            }
        }
        
        // RESTAURAR LA TRANSFORMACIÓN A LA ORIGINAL (QUITA LA ROTACIÓN)
        g2d.setTransform(originalTransform); 
        // ----------------------------------------------------
        // FIN DE LA ROTACIÓN

        // 4. DIBUJAR MARCO DECORATIVO (RuletaBase.png) - NO GIRA (Se dibuja encima de los colores)
        if (marcoRuletaImage != null) {
            g2d.drawImage(marcoRuletaImage, frameX, frameY, FRAME_SIZE, FRAME_SIZE, this);
        }
        
        // 5. DIBUJAR BORDE EXTERIOR (Se dibuja encima del marco)
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(8));
        g2d.drawOval(ruletaDrawX - 5, ruletaDrawY - 5, RULETA_TOTAL_SIZE + 10, RULETA_TOTAL_SIZE + 10);

        // 6. DIBUJAR PUNTERO FIJO (NO GIRA)
        int punteroBaseY = frameY;
        int[] xPoints = {panelCentroX - 15, panelCentroX + 15, panelCentroX};
        int[] yPoints = {punteroBaseY - 5, punteroBaseY - 5, punteroBaseY + 15};
        g2d.setColor(Color.YELLOW);
        g2d.fillPolygon(xPoints, yPoints, 3);
        g2d.setColor(Color.BLACK);
        g2d.drawPolygon(xPoints, yPoints, 3);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (isGirando) {
            double velocidadReduccion = 18.0;
            double distanciaAlObjetivo = (anguloRotacion - anguloFinalObjetivo + 360000) % 360000;

            if (distanciaAlObjetivo < 720.0) {
                velocidadReduccion = Math.max(0.5, distanciaAlObjetivo / 50.0);
            }

            anguloRotacion -= velocidadReduccion;

            if (anguloRotacion <= anguloFinalObjetivo + 0.1) {
                anguloRotacion = anguloFinalObjetivo;
                detener();
            }

            repaint();
        }
    }
}