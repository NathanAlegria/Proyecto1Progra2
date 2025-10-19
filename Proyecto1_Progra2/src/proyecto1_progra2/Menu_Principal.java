/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_progra2;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Nathan
 */

public class Menu_Principal extends JFrame {

    private Image backgroundImage;
    private JLabel currentUserNameLabel;
    
    private CardLayout cards;
    private JPanel cardPanel;
    private Logica.Usuarios usuarioActual; 

    private class BackgroundPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(new Color(10, 5, 20));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    public Menu_Principal() {
        
        setTitle("🏰 Vampire Wargame 📜");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
       
        cards = new CardLayout();
        cardPanel = new JPanel(cards);

        try {
            backgroundImage = ImageIO.read(getClass().getResource("/Imagenes/fondom.jpeg")); 

        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen de fondo. Asegúrate de que 'fondom.jpeg' esté en el paquete 'Imagenes'.", "Error de Recurso", JOptionPane.ERROR_MESSAGE);
        }

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        cardPanel.setOpaque(false);
        cardPanel.setMaximumSize(new Dimension(450, 500));
        cardPanel.setPreferredSize(new Dimension(450, 500));

        cardPanel.add(buildMainMenu(), "MainMenu"); 
        
        cardPanel.add(buildMiCuentaSubMenu(), "MiCuentaSubMenu");
        cardPanel.add(buildReportesSubMenu(), "ReportesSubMenu");
        
        cardPanel.add(buildEmptyPanel("Mi Información", "MiCuentaSubMenu"), "VerInfo");
        cardPanel.add(buildEmptyPanel("Cambiar Contraseña", "MiCuentaSubMenu"), "CambiarPass");
        cardPanel.add(buildEmptyPanel("Cerrar Cuenta", "MiCuentaSubMenu"), "CerrarCuenta");
        cardPanel.add(buildEmptyPanel("Ranking Jugadores", "ReportesSubMenu"), "Ranking");
        cardPanel.add(buildEmptyPanel("Logs de Juegos", "ReportesSubMenu"), "LogsJuegos");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel titleLabel = new JLabel("Vampire Wargame");
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setFont(new Font("Serif", Font.BOLD, 48));
        gbc.gridy = 0;
        backgroundPanel.add(titleLabel, gbc);

        JLabel subTitleLabel = new JLabel("— Lord of Shadows —");
        subTitleLabel.setForeground(new Color(255, 255, 255));
        subTitleLabel.setFont(new Font("Serif", Font.ITALIC, 20));
        gbc.gridy = 1;
        backgroundPanel.add(subTitleLabel, gbc);

        gbc.gridy = 2;
        backgroundPanel.add(cardPanel, gbc);

        setContentPane(backgroundPanel);
    }
    
    public void iniciarMenu(Logica.Usuarios usuarioLogeado) {
        this.usuarioActual = usuarioLogeado;
        currentUserNameLabel.setText("Bienvenido, Jugador " + usuarioActual.getUsuario()); 
        cards.show(cardPanel, "MainMenu");
        this.setVisible(true);
    }

    private JPanel buildEmptyPanel(String title, String backCard) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridy = 0;

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
        panel.add(titleLabel, c);

        c.gridy++;
        JLabel tempLabel = new JLabel("Funcionalidad pendiente de implementación.");
        tempLabel.setForeground(Color.WHITE);
        panel.add(tempLabel, c);

        c.gridy++;
        JButton backBtn = createThemedButton("Volver al Menú Anterior");
        backBtn.addActionListener(e -> cards.show(cardPanel, backCard));
        panel.add(backBtn, c);

        return panel;
    }

    private JPanel buildMainMenu() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        currentUserNameLabel = new JLabel("Bienvenido, Jugador..."); 
        currentUserNameLabel.setForeground(new Color(180, 180, 180));
        currentUserNameLabel.setFont(new Font("Serif", Font.BOLD, 18));
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0); 
        mainPanel.add(currentUserNameLabel, gbc);
        gbc.insets = new Insets(10, 10, 10, 10); 

        gbc.gridy = 1;
        JButton jugarBtn = createThemedButton("1- JUGAR VAMPIRE WARGAME");
        jugarBtn.addActionListener(e -> JOptionPane.showMessageDialog(Menu_Principal.this, "Iniciando juego...", "Jugar", JOptionPane.INFORMATION_MESSAGE));
        mainPanel.add(jugarBtn, gbc);

        gbc.gridy = 2;
        JButton cuentaBtn = createThemedButton("2- MI CUENTA ❯");
        cuentaBtn.addActionListener(e -> cards.show(cardPanel, "MiCuentaSubMenu"));
        mainPanel.add(cuentaBtn, gbc);

        gbc.gridy = 3;
        JButton reportesBtn = createThemedButton("3- REPORTES ❯");
        reportesBtn.addActionListener(e -> cards.show(cardPanel, "ReportesSubMenu"));
        mainPanel.add(reportesBtn, gbc);

        gbc.gridy = 4;
        JButton logOutBtn = createThemedButton("4- LOG OUT");
        logOutBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(Menu_Principal.this, "Cerrando sesión. La ventana de Login debería reaparecer ahora.", "Log Out", JOptionPane.INFORMATION_MESSAGE);
            
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(Menu_Principal.this);
            if (parentFrame != null) {
                parentFrame.dispose();
            }
        });
        gbc.insets = new Insets(20, 10, 10, 10); 
        mainPanel.add(logOutBtn, gbc);

        return mainPanel;
    }
    
    private JPanel buildMiCuentaSubMenu() {
        JPanel subPanel = new JPanel(new GridBagLayout());
        subPanel.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel title = new JLabel("MI CUENTA");
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(new Color(255, 215, 0));
        c.insets = new Insets(0, 0, 20, 0);
        subPanel.add(title, c);
        c.insets = new Insets(10, 10, 10, 10);

        c.gridy++;
        JButton verInfoBtn = createSubOptionButton("a. Ver Mi Información");
        verInfoBtn.addActionListener(e -> cards.show(cardPanel, "VerInfo"));
        subPanel.add(verInfoBtn, c);

        c.gridy++;
        JButton cambiarPassBtn = createSubOptionButton("b. Cambiar Contraseña");
        cambiarPassBtn.addActionListener(e -> cards.show(cardPanel, "CambiarPass"));
        subPanel.add(cambiarPassBtn, c);

        c.gridy++;
        JButton cerrarCuentaBtn = createSubOptionButton("c. Cerrar mi Cuenta");
        cerrarCuentaBtn.addActionListener(e -> cards.show(cardPanel, "CerrarCuenta"));
        subPanel.add(cerrarCuentaBtn, c);
        
        c.gridy++;
        c.insets = new Insets(20, 10, 10, 10);
        JButton backBtn = createThemedButton("Volver");
        backBtn.addActionListener(e -> cards.show(cardPanel, "MainMenu"));
        subPanel.add(backBtn, c);
        
        return subPanel;
    }
    
    private JPanel buildReportesSubMenu() {
        JPanel subPanel = new JPanel(new GridBagLayout());
        subPanel.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel title = new JLabel("REPORTES");
        title.setFont(new Font("Serif", Font.BOLD, 30));
        title.setForeground(new Color(255, 215, 0));
        c.insets = new Insets(0, 0, 20, 0);
        subPanel.add(title, c);
        c.insets = new Insets(10, 10, 10, 10);

        c.gridy++;
        JButton rankingBtn = createSubOptionButton("a. Ranking Jugadores");
        rankingBtn.addActionListener(e -> cards.show(cardPanel, "Ranking"));
        subPanel.add(rankingBtn, c);

        c.gridy++;
        JButton logsBtn = createSubOptionButton("b. Logs de mis últimos juegos");
        logsBtn.addActionListener(e -> cards.show(cardPanel, "LogsJuegos"));
        subPanel.add(logsBtn, c);
        
        c.gridy++;
        c.insets = new Insets(20, 10, 10, 10);
        JButton backBtn = createThemedButton("Volver");
        backBtn.addActionListener(e -> cards.show(cardPanel, "MainMenu"));
        subPanel.add(backBtn, c);
        
        return subPanel;
    }

    private JButton createThemedButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Serif", Font.BOLD, 18));
        button.setForeground(new Color(255, 255, 200));
        button.setBackground(new Color(80, 40, 0, 180));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(160, 100, 0), 3),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setMaximumSize(new Dimension(350, 50));
        button.setMinimumSize(new Dimension(350, 50));
        button.setPreferredSize(new Dimension(350, 50));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(130, 60, 0, 200));
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(80, 40, 0, 180));
                button.setForeground(new Color(255, 255, 200));
            }
        });
        return button;
    }

    private JButton createSubOptionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Serif", Font.PLAIN, 16));
        button.setForeground(new Color(200, 200, 200));
        button.setBackground(new Color(30, 15, 60, 150));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 40, 5, 40));
        button.setMaximumSize(new Dimension(300, 40));
        button.setMinimumSize(new Dimension(300, 40));
        button.setPreferredSize(new Dimension(300, 40));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(50, 25, 90, 180));
            }

            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(30, 15, 60, 150));
            }
        });
        return button;
    }
}
