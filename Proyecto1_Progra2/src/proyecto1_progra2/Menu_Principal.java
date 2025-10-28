/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_progra2;

import Logica.Usuarios;
import Logica.Cuentas;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu_Principal extends JFrame {

    private Image backgroundImage;
    private Image buttonImage;
    private Image buttonHoverImage;
    private JLabel currentUserNameLabel;

    private CardLayout cards;
    private JPanel cardPanel;
    private Logica.Usuarios usuarioActual;
    private Logica.Cuentas sistemaCuentas;

    private JPasswordField actualPassField;
    private JPasswordField nuevaPassField;
    private JPasswordField confirmarNuevaPassField;
    private JPasswordField cerrarCuentaPassField;
    
    private JLabel infoUsuario;
    private JLabel infoPuntos;
    private JLabel infoFecha;
    private JLabel infoEstado;

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

    private class RankingBackgroundPanel extends JPanel {

        public RankingBackgroundPanel(JComponent content) {
            setLayout(new GridBagLayout());
            setOpaque(true);
            add(content);
        }

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

    private class ThemedButton extends JButton {
        protected boolean isHovered = false;
        private final Font buttonFont = new Font("Serif", Font.BOLD, 18);

        public ThemedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(new Color(255, 255, 200));
            setFont(buttonFont);
            setPreferredSize(new Dimension(350, 50));
            setMaximumSize(new Dimension(350, 50));
            setMinimumSize(new Dimension(350, 50));
            setToolTipText(null);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent evt) {
                    isHovered = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent evt) {
                    isHovered = false;
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Image imgToDraw = isHovered ? buttonHoverImage : buttonImage;
            
            if (imgToDraw != null) {
                g2.drawImage(imgToDraw, 0, 0, getWidth(), getHeight(), this);
            } else {
                Color baseColor = new Color(80, 40, 0, 180);
                Color hoverColor = new Color(130, 60, 0, 200);
                g2.setColor(isHovered ? hoverColor : baseColor);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(160, 100, 0));
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
            
            if (isHovered) {
                setForeground(Color.WHITE);
            } else {
                setForeground(new Color(255, 255, 200));
            }

            super.paintComponent(g2);
            g2.dispose();
        }
    }
    
    private class SubOptionButton extends ThemedButton {
        public SubOptionButton(String text) {
            super(text);
            setFont(new Font("Serif", Font.PLAIN, 16));
            setPreferredSize(new Dimension(300, 40));
            setMaximumSize(new Dimension(300, 40));
            setMinimumSize(new Dimension(300, 40));
            
            setForeground(new Color(200, 200, 200));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color baseColor = new Color(30, 15, 60, 150); 
            Color hoverColor = new Color(50, 25, 90, 180);
            
            g2.setColor(isHovered ? hoverColor : baseColor);
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            if (isHovered) {
                setForeground(Color.WHITE);
            } else {
                setForeground(new Color(200, 200, 200));
            }
            
            super.paintComponent(g2);
            g2.dispose();
        }
    }

    public Menu_Principal(Logica.Cuentas sistemaCuentas) {
        this.sistemaCuentas = sistemaCuentas;

        setTitle("🏰 Vampire Wargame 📜");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLocationRelativeTo(null);
        setResizable(false);

        cards = new CardLayout();
        cardPanel = new JPanel(cards);

        try {
            backgroundImage = ImageIO.read(getClass().getResource("/Imagenes/fondom.jpg"));
            buttonImage = ImageIO.read(getClass().getResource("/Imagenes/botones.jpg"));
            buttonHoverImage = ImageIO.read(getClass().getResource("/Imagenes/botones.jpg"));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error al cargar la imagen de fondo o botones: " + e.getMessage());
        }

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());
        backgroundPanel.setOpaque(true); 

        cardPanel.setOpaque(false);

        cardPanel.add(buildMainMenu(), "MainMenu");
        cardPanel.add(buildMiCuentaSubMenu(), "MiCuentaSubMenu");
        cardPanel.add(buildVerMiInformacionPanel(), "VerInfo");
        cardPanel.add(buildReportesSubMenu(), "ReportesSubMenu");
        cardPanel.add(buildCambiarPassPanel(), "CambiarPass");
        cardPanel.add(buildCerrarCuentaPanel(), "CerrarCuenta");
        cardPanel.add(buildRankingPanel(), "Ranking");
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
        subTitleLabel.setForeground(new Color(200, 200, 200)); 
        subTitleLabel.setFont(new Font("Serif", Font.ITALIC, 20));
        gbc.gridy = 1;
        backgroundPanel.add(subTitleLabel, gbc);

        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 0);
        backgroundPanel.add(cardPanel, gbc);

        setContentPane(backgroundPanel);
    }

    public void iniciarMenu(Logica.Usuarios usuarioLogeado) {
        this.usuarioActual = usuarioLogeado;
        if (currentUserNameLabel != null && usuarioLogeado != null) {
            currentUserNameLabel.setText("Bienvenido, Jugador " + usuarioActual.getUsuario());
        }
        actualizarInfoUsuario();
        cards.show(cardPanel, "MainMenu");
        this.setVisible(true);
    }

    private void setTextFieldSize(JComponent field) {
        Dimension fixedSize = new Dimension(250, 30);
        field.setMinimumSize(fixedSize);
        field.setPreferredSize(fixedSize);
        field.setMaximumSize(fixedSize);
    }

    private void returnToLogin() {
        this.dispose(); 
        SwingUtilities.invokeLater(() -> new Menu().setVisible(true));
    }

    private void actualizarInfoUsuario() {
        if (usuarioActual != null) {
            String estadoStr = usuarioActual.getEstado() ? "Activo" : "Inactivo";
            infoUsuario.setText(usuarioActual.getUsuario());
            infoPuntos.setText(String.valueOf(usuarioActual.getPuntos()));
            infoFecha.setText(usuarioActual.getFechaCreacion());
            infoEstado.setText(estadoStr);
        } else {
            infoUsuario.setText("N/D");
            infoPuntos.setText("N/D");
            infoFecha.setText("N/D");
            infoEstado.setText("N/D");
        }
    }

    private JPanel buildVerMiInformacionPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(true);
        panel.setBackground(new Color(0, 0, 0, 150)); 
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 15, 8, 15);
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("MI INFORMACIÓN DE CUENTA");
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(0, 0, 25, 0);
        panel.add(titleLabel, c);
        c.gridwidth = 1;
        c.insets = new Insets(8, 15, 8, 15);

        infoUsuario = new JLabel();
        infoPuntos = new JLabel();
        infoFecha = new JLabel();
        infoEstado = new JLabel();

        int y = 1;

        y = addInfoRow(panel, c, y, "Nombre de Usuario:", infoUsuario);
        y = addInfoRow(panel, c, y, "Puntos Acumulados:", infoPuntos);
        y = addInfoRow(panel, c, y, "Fecha de Creación:", infoFecha);
        y = addInfoRow(panel, c, y, "Estado de la Cuenta:", infoEstado);
        
        c.gridy = y;
        c.gridwidth = 2;
        c.insets = new Insets(30, 15, 8, 15);
        JButton backBtn = new ThemedButton("Volver al Menú Anterior");
        backBtn.addActionListener(e -> cards.show(cardPanel, "MiCuentaSubMenu"));
        panel.add(backBtn, c);

        return panel;
    }
    
    private int addInfoRow(JPanel panel, GridBagConstraints c, int y, String labelText, JLabel valueLabel) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Serif", Font.BOLD, 16));
        label.setForeground(new Color(200, 200, 200));
        c.gridy = y;
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        panel.add(label, c);

        valueLabel.setFont(new Font("Serif", Font.PLAIN, 16));
        valueLabel.setForeground(Color.WHITE);
        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        panel.add(valueLabel, c);
        
        return y + 1;
    }

    private JPanel buildEmptyPanel(String title, String backCard) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(true);
        panel.setBackground(new Color(0, 0, 0, 150));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridy = 0;

        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
        panel.add(titleLabel, c);

        c.gridy++;
        JLabel tempLabel = new JLabel("Funcionalidad de " + title + " pendiente de implementación.");
        tempLabel.setForeground(Color.WHITE);
        panel.add(tempLabel, c);

        c.gridy++;
        JButton backBtn = new ThemedButton("Volver al Menú Anterior");
        backBtn.addActionListener(e -> cards.show(cardPanel, backCard));
        panel.add(backBtn, c);

        return panel;
    }

    private JPanel buildMainMenu() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(true);
        mainPanel.setBackground(new Color(0, 0, 0, 150));
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
        JButton jugarBtn = new ThemedButton("JUGAR VAMPIRE WARGAME");
        jugarBtn.addActionListener(e -> JOptionPane.showMessageDialog(Menu_Principal.this, "Iniciando juego...", "Jugar", JOptionPane.INFORMATION_MESSAGE));
        mainPanel.add(jugarBtn, gbc);

        gbc.gridy = 2;
        JButton cuentaBtn = new ThemedButton("MI CUENTA ❯");
        cuentaBtn.addActionListener(e -> cards.show(cardPanel, "MiCuentaSubMenu"));
        mainPanel.add(cuentaBtn, gbc);

        gbc.gridy = 3;
        JButton reportesBtn = new ThemedButton("REPORTES ❯");
        reportesBtn.addActionListener(e -> cards.show(cardPanel, "ReportesSubMenu"));
        mainPanel.add(reportesBtn, gbc);

        gbc.gridy = 4;
        JButton logOutBtn = new ThemedButton("LOG OUT");
        logOutBtn.addActionListener(e -> {
            returnToLogin();
        });
        gbc.insets = new Insets(20, 10, 10, 10);
        mainPanel.add(logOutBtn, gbc);

        return mainPanel;
    }

    private JPanel buildMiCuentaSubMenu() {
        JPanel subPanel = new JPanel(new GridBagLayout());
        subPanel.setOpaque(true);
        subPanel.setBackground(new Color(0, 0, 0, 150));
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
        JButton verInfoBtn = new SubOptionButton("Ver Mi Información");
        verInfoBtn.addActionListener(e -> {
            actualizarInfoUsuario();
            cards.show(cardPanel, "VerInfo");
        });
        subPanel.add(verInfoBtn, c);

        c.gridy++;
        JButton cambiarPassBtn = new SubOptionButton("Cambiar Contraseña");
        cambiarPassBtn.addActionListener(e -> cards.show(cardPanel, "CambiarPass"));
        subPanel.add(cambiarPassBtn, c);

        c.gridy++;
        JButton cerrarCuentaBtn = new SubOptionButton("Cerrar mi Cuenta");
        cerrarCuentaBtn.addActionListener(e -> cards.show(cardPanel, "CerrarCuenta"));
        subPanel.add(cerrarCuentaBtn, c);

        c.gridy++;
        c.insets = new Insets(20, 10, 10, 10);
        JButton backBtn = new ThemedButton("Volver");
        backBtn.addActionListener(e -> cards.show(cardPanel, "MainMenu"));
        subPanel.add(backBtn, c);

        return subPanel;
    }

    private JPanel buildReportesSubMenu() {
        JPanel subPanel = new JPanel(new GridBagLayout());
        subPanel.setOpaque(true);
        subPanel.setBackground(new Color(0, 0, 0, 150));
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
        JButton rankingBtn = new SubOptionButton("Ranking Jugadores");
        rankingBtn.addActionListener(e -> cards.show(cardPanel, "Ranking"));
        subPanel.add(rankingBtn, c);

        c.gridy++;
        JButton logsBtn = new SubOptionButton("Logs de mis últimos juegos");
        logsBtn.addActionListener(e -> cards.show(cardPanel, "LogsJuegos"));
        subPanel.add(logsBtn, c);

        c.gridy++;
        c.insets = new Insets(20, 10, 10, 10);
        JButton backBtn = new ThemedButton("Volver");
        backBtn.addActionListener(e -> cards.show(cardPanel, "MainMenu"));
        subPanel.add(backBtn, c);

        return subPanel;
    }

    private JPanel buildCambiarPassPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(true);
        panel.setBackground(new Color(0, 0, 0, 150));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridwidth = 2;

        JLabel titleLabel = new JLabel("CAMBIAR CONTRASEÑA");
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
        c.gridy = 0;
        panel.add(titleLabel, c);

        c.gridy++;
        JLabel actualLabel = new JLabel("Contraseña Actual:");
        actualLabel.setForeground(new Color(200, 200, 200));
        panel.add(actualLabel, c);

        c.gridy++;
        actualPassField = new JPasswordField(15);
        setTextFieldSize(actualPassField);
        panel.add(actualPassField, c);

        c.gridy++;
        JLabel nuevaLabel = new JLabel("Nueva Contraseña (5 caracteres):");
        nuevaLabel.setForeground(new Color(200, 200, 200));
        panel.add(nuevaLabel, c);

        c.gridy++;
        nuevaPassField = new JPasswordField(15);
        setTextFieldSize(nuevaPassField);
        panel.add(nuevaPassField, c);

        c.gridy++;
        JLabel confirmarLabel = new JLabel("Confirmar Nueva Contraseña:");
        confirmarLabel.setForeground(new Color(200, 200, 200));
        panel.add(confirmarLabel, c);

        c.gridy++;
        confirmarNuevaPassField = new JPasswordField(15);
        setTextFieldSize(confirmarNuevaPassField);
        panel.add(confirmarNuevaPassField, c);

        c.gridy++;
        c.gridwidth = 1;
        JButton cambiarBtn = new ThemedButton("Cambiar");
        cambiarBtn.addActionListener(e -> handlePasswordChange());
        panel.add(cambiarBtn, c);

        c.gridx = 1;
        JButton backBtn = new ThemedButton("Volver");
        backBtn.addActionListener(e -> {
            actualPassField.setText("");
            nuevaPassField.setText("");
            confirmarNuevaPassField.setText("");
            cards.show(cardPanel, "MiCuentaSubMenu");
        });
        panel.add(backBtn, c);

        return panel;
    }

    private void handlePasswordChange() {
        if (this.usuarioActual == null) {
            JOptionPane.showMessageDialog(Menu_Principal.this, "Error de Sesión: No se ha iniciado sesión correctamente. Vuelva a Log In.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        char[] oldPass = actualPassField.getPassword();
        char[] newPass = nuevaPassField.getPassword();
        char[] confPass = confirmarNuevaPassField.getPassword();

        if (oldPass.length == 0 || newPass.length == 0 || confPass.length == 0) {
            JOptionPane.showMessageDialog(Menu_Principal.this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            Logica.Usuarios.limpiarContrasena(oldPass);
            Logica.Usuarios.limpiarContrasena(newPass);
            Logica.Usuarios.limpiarContrasena(confPass);
            return;
        }

        if (!java.util.Arrays.equals(newPass, confPass)) {
            JOptionPane.showMessageDialog(Menu_Principal.this, "Las nuevas contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
            Logica.Usuarios.limpiarContrasena(oldPass);
            Logica.Usuarios.limpiarContrasena(newPass);
            Logica.Usuarios.limpiarContrasena(confPass);
            return;
        }

        if (sistemaCuentas.cambiarContrasena(this.usuarioActual.getUsuario(), oldPass, newPass)) {
            JOptionPane.showMessageDialog(Menu_Principal.this, "Contraseña cambiada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cards.show(cardPanel, "MiCuentaSubMenu");
        }

        actualPassField.setText("");
        nuevaPassField.setText("");
        confirmarNuevaPassField.setText("");
    }

    private JPanel buildCerrarCuentaPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(true);
        panel.setBackground(new Color(0, 0, 0, 150));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridwidth = 2;

        JLabel titleLabel = new JLabel("CERRAR MI CUENTA");
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setFont(new Font("Serif", Font.BOLD, 30));
        c.gridy = 0;
        panel.add(titleLabel, c);

        c.gridy++;
        JTextArea warningArea = new JTextArea(
                "¡ADVERTENCIA!\n"
                + "Esta acción es irreversible.\n"
                + "Tu nombre y puntos se guardarán en el ranking."
        );
        warningArea.setFont(new Font("Serif", Font.PLAIN, 14));
        warningArea.setForeground(new Color(255, 215, 0));
        warningArea.setBackground(new Color(0, 0, 0, 0));
        warningArea.setOpaque(false);
        warningArea.setEditable(false);
        warningArea.setWrapStyleWord(true);
        warningArea.setLineWrap(true);
        warningArea.setRows(3);
        warningArea.setColumns(20);
        JPanel warningPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        warningPanel.setOpaque(false);
        warningPanel.add(warningArea);

        c.insets = new Insets(15, 8, 15, 8);
        panel.add(warningPanel, c);
        c.insets = new Insets(8, 8, 8, 8);

        c.gridy++;
        JLabel passLabel = new JLabel("Ingresa tu contraseña para confirmar:");
        passLabel.setForeground(new Color(200, 200, 200));
        panel.add(passLabel, c);

        c.gridy++;
        cerrarCuentaPassField = new JPasswordField(15);
        setTextFieldSize(cerrarCuentaPassField);
        panel.add(cerrarCuentaPassField, c);

        c.gridy++;
        c.gridwidth = 1;
        JButton cerrarBtn = new ThemedButton("Cerrar Cuenta");
        cerrarBtn.setBackground(new Color(150, 0, 0, 180));
        cerrarBtn.addActionListener(e -> handleAccountDeletion());
        panel.add(cerrarBtn, c);

        c.gridx = 1;
        JButton backBtn = new ThemedButton("Cancelar");
        backBtn.addActionListener(e -> {
            cerrarCuentaPassField.setText("");
            cards.show(cardPanel, "MiCuentaSubMenu");
        });
        panel.add(backBtn, c);

        return panel;
    }

    private void handleAccountDeletion() {
        if (this.usuarioActual == null) {
            JOptionPane.showMessageDialog(Menu_Principal.this, "Error de Sesión: No se ha iniciado sesión correctamente. Vuelva a Log In.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        char[] pass = cerrarCuentaPassField.getPassword();

        if (pass.length == 0) {
            JOptionPane.showMessageDialog(Menu_Principal.this, "Debes ingresar tu contraseña para confirmar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                Menu_Principal.this,
                "¿Estás absolutamente seguro de que quieres eliminar tu cuenta? Esta acción no se puede deshacer.",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (sistemaCuentas.eliminarUsuario(this.usuarioActual.getUsuario(), pass)) {
                returnToLogin();
            }
        }

        cerrarCuentaPassField.setText("");
    }

    private JPanel buildRankingPanel() {
        JPanel rankingContent = new JPanel(new BorderLayout());
        rankingContent.setOpaque(true);
        rankingContent.setBackground(new Color(0, 0, 0, 150));

        rankingContent.setPreferredSize(new Dimension(650, 500));
        rankingContent.setMaximumSize(new Dimension(650, 500));


        ArrayList<Logica.Usuarios> todosLosJugadores = sistemaCuentas.getRankingData();

        todosLosJugadores.sort((u1, u2) -> Integer.compare(u2.getPuntos(), u1.getPuntos()));

        String[] columnNames = {"POSICIÓN", "NOMBRE DE JUGADOR", "PUNTOS"};
        Object[][] data = new Object[todosLosJugadores.size()][3];

        for (int i = 0; i < todosLosJugadores.size(); i++) {
            Logica.Usuarios user = todosLosJugadores.get(i);
            data[i][0] = i + 1;
            data[i][1] = user.getUsuario();
            data[i][2] = String.format("%,d", user.getPuntos());
        }

        JTable rankingTable = new JTable(data, columnNames);
        rankingTable.setFont(new Font("Monospaced", Font.PLAIN, 14));
        rankingTable.setForeground(Color.WHITE);
        rankingTable.setBackground(new Color(0, 0, 0, 180));
        rankingTable.setRowHeight(25);
        rankingTable.getTableHeader().setFont(new Font("Monospaced", Font.BOLD, 16));
        rankingTable.getTableHeader().setBackground(new Color(80, 40, 0, 200));
        rankingTable.getTableHeader().setForeground(new Color(255, 215, 0));
        rankingTable.setEnabled(false);

        rankingTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        rankingTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        rankingTable.getColumnModel().getColumn(2).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(rankingTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        rankingContent.add(scrollPane, BorderLayout.CENTER);

        JButton backBtn = new ThemedButton("Volver al Menú Anterior");
        backBtn.addActionListener(e -> cards.show(cardPanel, "ReportesSubMenu"));

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setOpaque(true);
        southPanel.setBackground(new Color(0, 0, 0, 150));
        southPanel.add(backBtn);
        rankingContent.add(southPanel, BorderLayout.SOUTH);

        return new RankingBackgroundPanel(rankingContent);
    }
}