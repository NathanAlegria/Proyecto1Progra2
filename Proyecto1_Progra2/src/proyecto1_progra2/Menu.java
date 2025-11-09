/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_progra2;

import Logica.Cuentas;
import Logica.Usuarios;
import Logica.InterfaceCuentas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Arrays;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import Logica.InterfaceCuentas;
import java.net.URL;

/**
 *
 * @author Nathan
 */
/**
 *
 * @author Nathan
 */
public class Menu extends JFrame {

    private Image backgroundImage;
    private Image buttonImage;
    private Image buttonHoverImage;
    private Image subTitleBackgroundImage;

    private JTextField loginUserField;
    private JPasswordField loginPassField;
    private JTextField registerUserField;
    private JPasswordField registerPassField;
    private JPasswordField registerConfPassField;
    private InterfaceCuentas sistemaCuentas;

    // MÉTODO AGREGADO: Permite que la ventana Menu_Principal vuelva a mostrar esta ventana al cerrar sesión.
    public void showMenu() {
        this.setVisible(true);
        // Opcional: limpiar los campos de login al regresar.
        if (loginUserField != null) {
            loginUserField.setText("");
        }
        if (loginPassField != null) {
            loginPassField.setText("");
        }
    }

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

    private class SubTitlePanel extends JPanel {

        public SubTitlePanel() {
            setOpaque(false);
            setLayout(new GridBagLayout());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (subTitleBackgroundImage != null) {
                g.drawImage(subTitleBackgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    private class ThemedButton extends JButton {

        protected boolean isHovered = false;
        private final Font buttonFont = new Font("Serif", Font.BOLD, 20);

        public ThemedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(new Color(255, 255, 200));
            setFont(buttonFont);
            setPreferredSize(new Dimension(300, 60));
            setMaximumSize(new Dimension(300, 60));
            setMinimumSize(new Dimension(300, 60));

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
                g2.setColor(isHovered ? new Color(130, 60, 0, 200) : new Color(80, 40, 0, 180));
                g2.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
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

    public Menu() {
        setTitle("🏰 Vampire Wargame 📜");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        this.sistemaCuentas = Cuentas.getInstance();

        try {
            // Asegúrate de que las rutas de los recursos sean correctas para tu proyecto
            backgroundImage = ImageIO.read(getClass().getResource("/Imagenes/fondo.jpg"));
            buttonImage = ImageIO.read(getClass().getResource("/Imagenes/botones.jpg"));
            buttonHoverImage = ImageIO.read(getClass().getResource("/Imagenes/botones.jpg"));
            subTitleBackgroundImage = ImageIO.read(getClass().getResource("/Imagenes/subT.jpg"));

        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen de fondo o botones. Asegúrate de que los archivos estén en el paquete 'Imagenes'.", "Error de Recurso", JOptionPane.ERROR_MESSAGE);
        }

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        CardLayout cards = new CardLayout();
        JPanel cardPanel = new JPanel(cards);
        cardPanel.setOpaque(false);
        cardPanel.setMaximumSize(new Dimension(400, 450));
        cardPanel.setPreferredSize(new Dimension(400, 450));

        cardPanel.add(buildMenuPanel(cards, cardPanel), "MenuPrincipal");
        cardPanel.add(buildLoginPanel(cards, cardPanel), "Login");
        cardPanel.add(buildRegisterPanel(cards, cardPanel), "Register");

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
        subTitleLabel.setForeground(Color.WHITE);
        subTitleLabel.setFont(new Font("Serif", Font.ITALIC, 20));
        subTitleLabel.setOpaque(false);

        SubTitlePanel subTitleBgPanel = new SubTitlePanel();
        subTitleBgPanel.setPreferredSize(new Dimension(300, 40));
        subTitleBgPanel.setMaximumSize(new Dimension(300, 40));
        subTitleBgPanel.add(subTitleLabel);

        gbc.gridy = 1;
        backgroundPanel.add(subTitleBgPanel, gbc);

        gbc.gridy = 2;
        backgroundPanel.add(cardPanel, gbc);

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private void setTextFieldSize(JComponent field) {
        Dimension fixedSize = new Dimension(250, 30);
        field.setMinimumSize(fixedSize);
        field.setPreferredSize(fixedSize);
        field.setMaximumSize(fixedSize);
    }

    private JPanel buildMenuPanel(CardLayout cards, JPanel cardPanel) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(true);
        buttonPanel.setBackground(new Color(0, 0, 0, 100));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        JButton logInButton = new ThemedButton("Log In");
        JButton createPlayerButton = new ThemedButton("Crear Player");
        JButton exitButton = new ThemedButton("Salir");

        logInButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createPlayerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        logInButton.addActionListener(e -> cards.show(cardPanel, "Login"));
        createPlayerButton.addActionListener(e -> cards.show(cardPanel, "Register"));

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(logInButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(createPlayerButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonPanel.add(exitButton);

        return buttonPanel;
    }

    private JPanel buildLoginPanel(CardLayout cards, JPanel cardPanel) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(true);
        panel.setBackground(new Color(0, 0, 0, 150));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;

        JLabel titleLabel = new JLabel("INICIAR SESIÓN");
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        c.insets = new Insets(0, 0, 20, 0);
        panel.add(titleLabel, c);
        c.insets = new Insets(10, 10, 10, 10);

        c.gridy++;
        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setForeground(new Color(200, 200, 200));
        panel.add(userLabel, c);

        c.gridy++;
        loginUserField = new JTextField(15);
        setTextFieldSize(loginUserField);
        panel.add(loginUserField, c);

        c.gridy++;
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setForeground(new Color(200, 200, 200));
        panel.add(passLabel, c);

        c.gridy++;
        loginPassField = new JPasswordField(15);
        setTextFieldSize(loginPassField);
        panel.add(loginPassField, c);

        c.gridy++;
        c.gridwidth = 1;
        JButton loginBtn = new ThemedButton("Iniciar Sesión");
        panel.add(loginBtn, c);

        c.gridx = 1;
        JButton backBtn = new ThemedButton("Volver");
        backBtn.addActionListener(e -> {
            loginUserField.setText("");
            loginPassField.setText("");
            cards.show(cardPanel, "MenuPrincipal");
        });
        panel.add(backBtn, c);

        loginBtn.addActionListener(e -> {
            String usuarioIngresado = loginUserField.getText().trim();
            char[] contraIngresada = loginPassField.getPassword();

            if (usuarioIngresado.isEmpty()) {
                JOptionPane.showMessageDialog(Menu.this, "Ingresa tu nombre de usuario.", "Faltan datos", JOptionPane.WARNING_MESSAGE);
                Usuarios.limpiarContrasena(contraIngresada);
                loginPassField.setText("");
                return;
            }

            if (contraIngresada.length == 0) {
                JOptionPane.showMessageDialog(Menu.this, "Ingresa tu contraseña.", "Faltan datos", JOptionPane.WARNING_MESSAGE);
                Usuarios.limpiarContrasena(contraIngresada);
                loginPassField.setText("");
                return;
            }
            if (sistemaCuentas.verificarCredenciales(usuarioIngresado, contraIngresada)) {

                JOptionPane.showMessageDialog(Menu.this, "¡Bienvenido, " + usuarioIngresado + "!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loginUserField.setText("");
                loginPassField.setText("");

                Menu.this.setVisible(false);

                Usuarios usuarioLogeado = sistemaCuentas.buscarUsuario(usuarioIngresado);

                // 🚩 CORRECCIÓN 1: Pasar el usuario al constructor para inicializarlo inmediatamente.
                Menu_Principal mp = new Menu_Principal(sistemaCuentas, Menu.this, usuarioLogeado);
                mp.setVisible(true); // Solo se necesita mostrar la nueva ventana
            } else {
                JOptionPane.showMessageDialog(Menu.this, "Usuario o contraseña incorrectos.", "Error de Login", JOptionPane.ERROR_MESSAGE);
                loginPassField.setText("");
            }
            Usuarios.limpiarContrasena(contraIngresada);
        });
        return panel;
    }

    private JPanel buildRegisterPanel(CardLayout cards, JPanel cardPanel) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(true);
        panel.setBackground(new Color(0, 0, 0, 150));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;

        JLabel titleLabel = new JLabel("CREAR NUEVO JUGADOR");
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        c.insets = new Insets(0, 0, 20, 0);
        panel.add(titleLabel, c);
        c.insets = new Insets(10, 10, 10, 10);

        c.gridy++;
        JLabel userLabel = new JLabel("Crear Usuario (Será el nombre de Jugador):");
        userLabel.setForeground(new Color(200, 200, 200));
        panel.add(userLabel, c);

        c.gridy++;
        registerUserField = new JTextField(15);
        setTextFieldSize(registerUserField);
        panel.add(registerUserField, c);

        c.gridy++;
        JLabel passLabel = new JLabel("Contraseña (5 caracteres):");
        passLabel.setForeground(new Color(200, 200, 200));
        panel.add(passLabel, c);

        c.gridy++;
        registerPassField = new JPasswordField(15);
        setTextFieldSize(registerPassField);
        panel.add(registerPassField, c);

        c.gridy++;
        JLabel confPassLabel = new JLabel("Confirmar Contraseña:");
        confPassLabel.setForeground(new Color(200, 200, 200));
        panel.add(confPassLabel, c);

        c.gridy++;
        registerConfPassField = new JPasswordField(15);
        setTextFieldSize(registerConfPassField);
        panel.add(registerConfPassField, c);

        c.gridy++;
        c.gridwidth = 1;
        JButton registerBtn = new ThemedButton("Registrar");
        panel.add(registerBtn, c);

        c.gridx = 1;
        JButton backBtn = new ThemedButton("Volver");
        backBtn.addActionListener(e -> {
            registerUserField.setText("");
            registerPassField.setText("");
            registerConfPassField.setText("");
            cards.show(cardPanel, "MenuPrincipal");
        });
        panel.add(backBtn, c);

        final Menu menuReferencia = this;

        registerBtn.addActionListener(e -> {
            String usuario = registerUserField.getText().trim();
            char[] contra = registerPassField.getPassword();
            char[] confContra = registerConfPassField.getPassword();

            if (usuario.isEmpty() || contra.length == 0 || confContra.length == 0) {
                JOptionPane.showMessageDialog(Menu.this, "Todos los campos son obligatorios.", "Faltan datos", JOptionPane.WARNING_MESSAGE);
                Usuarios.limpiarContrasena(contra);
                Usuarios.limpiarContrasena(confContra);
                return;
            }

            if (!Arrays.equals(contra, confContra)) {
                JOptionPane.showMessageDialog(Menu.this, "Las contraseñas no coinciden.", "Error de Contraseña", JOptionPane.WARNING_MESSAGE);
                registerPassField.setText("");
                registerConfPassField.setText("");
                Usuarios.limpiarContrasena(contra);
                Usuarios.limpiarContrasena(confContra);
                return;
            }

            if (sistemaCuentas.registrarUsuario(usuario, contra)) {

                registerUserField.setText("");
                registerPassField.setText("");
                registerConfPassField.setText("");

                Menu.this.setVisible(false);

                Usuarios usuarioLogeado = sistemaCuentas.buscarUsuario(usuario);

                // 🚩 CORRECCIÓN 2: Pasar el usuario al constructor para inicializarlo inmediatamente.
                Menu_Principal mp = new Menu_Principal(sistemaCuentas, menuReferencia, usuarioLogeado);
                mp.setVisible(true); // Solo se necesita mostrar la nueva ventana

            }

            Usuarios.limpiarContrasena(contra);
            Usuarios.limpiarContrasena(confContra);
        });

        return panel;
    }
}
