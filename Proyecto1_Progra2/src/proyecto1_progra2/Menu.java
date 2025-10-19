/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto1_progra2;

import Logica.Cuentas;
import Logica.Usuarios;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Arrays;

/**
 *
 * @author Nathan
 */
public class Menu extends JFrame {

    private Image backgroundImage;
    private JButton logInButton;
    private JButton createPlayerButton;
    private JButton exitButton;

    private JTextField loginUserField;
    private JPasswordField loginPassField;
    private JTextField registerUserField;
    private JPasswordField registerPassField;
    private JPasswordField registerConfPassField;
    private Cuentas sistemaCuentas;

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

    public Menu() {
        setTitle("🏰 Vampire Wargame 📜");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        sistemaCuentas = new Cuentas();

        try {
            backgroundImage = ImageIO.read(getClass().getResource("/Imagenes/fondo.jpeg"));

        } catch (IOException | IllegalArgumentException e) {

            System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());

            JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen de fondo. Asegúrate de que 'fondo.jpeg' esté en el paquete 'Imagenes'.", "Error de Recurso", JOptionPane.ERROR_MESSAGE);

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
        subTitleLabel.setForeground(new Color(255, 255, 255));
        subTitleLabel.setFont(new Font("Serif", Font.ITALIC, 20));
        gbc.gridy = 1;
        backgroundPanel.add(subTitleLabel, gbc);

        gbc.gridy = 2;
        backgroundPanel.add(cardPanel, gbc);

        setContentPane(backgroundPanel);
        setVisible(true);
    }

    private void setTextFieldSize(JComponent field) {
        Dimension fixedSize = new Dimension(field.getPreferredSize().width, 30);
        field.setMinimumSize(fixedSize);
        field.setPreferredSize(fixedSize);
        field.setMaximumSize(fixedSize);
    }

    private JPanel buildMenuPanel(CardLayout cards, JPanel cardPanel) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        logInButton = createThemedButton("Log In");
        createPlayerButton = createThemedButton("Crear Player");
        exitButton = createThemedButton("Salir");

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
        panel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;

        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setForeground(Color.WHITE);
        panel.add(userLabel, c);

        c.gridy++;
        loginUserField = new JTextField(15);
        setTextFieldSize(loginUserField);
        panel.add(loginUserField, c);

        c.gridy++;
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setForeground(Color.WHITE);
        panel.add(passLabel, c);

        c.gridy++;
        loginPassField = new JPasswordField(15);
        setTextFieldSize(loginPassField);
        panel.add(loginPassField, c);

        c.gridy++;
        c.gridwidth = 1;
        JButton loginBtn = createThemedButton("Iniciar Sesión");
        panel.add(loginBtn, c);

        c.gridx = 1;
        JButton backBtn = createThemedButton("Volver");
        backBtn.addActionListener(e -> cards.show(cardPanel, "MenuPrincipal"));
        panel.add(backBtn, c);

        loginBtn.addActionListener(e -> {
            String usuarioIngresado = loginUserField.getText().trim();
            char[] contraIngresada = loginPassField.getPassword();

            if (usuarioIngresado.isEmpty()) {
                JOptionPane.showMessageDialog(Menu.this, "Ingresa tu nombre de usuario.", "Faltan datos", JOptionPane.WARNING_MESSAGE);
                Usuarios.limpiarContrasena(contraIngresada);
                return;
            }

            if (contraIngresada.length == 0) {
                JOptionPane.showMessageDialog(Menu.this, "Ingresa tu contraseña.", "Faltan datos", JOptionPane.WARNING_MESSAGE);
                Usuarios.limpiarContrasena(contraIngresada);
                return;
            }

            if (sistemaCuentas.verificarCredenciales(usuarioIngresado, contraIngresada)) {
                JOptionPane.showMessageDialog(Menu.this, "¡Bienvenido, " + usuarioIngresado + "!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loginUserField.setText("");
                loginPassField.setText("");
                Menu.this.setVisible(false);
                new Menu_Principal().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(Menu.this, "Usuario o contraseña incorrectos.", "Error de Login", JOptionPane.ERROR_MESSAGE);
            }

            Usuarios.limpiarContrasena(contraIngresada);
            loginPassField.setText("");
        });

        return panel;
    }

    private JPanel buildRegisterPanel(CardLayout cards, JPanel cardPanel) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;

        JLabel userLabel = new JLabel("Crear Usuario (Será el nombre de Jugador):");
        userLabel.setForeground(Color.WHITE);
        panel.add(userLabel, c);

        c.gridy++;
        registerUserField = new JTextField(15);
        setTextFieldSize(registerUserField);
        panel.add(registerUserField, c);

        c.gridy++;
        JLabel passLabel = new JLabel("Contraseña (5 caracteres):");
        passLabel.setForeground(Color.WHITE);
        panel.add(passLabel, c);

        c.gridy++;
        registerPassField = new JPasswordField(15);
        setTextFieldSize(registerPassField);
        panel.add(registerPassField, c);

        c.gridy++;
        JLabel confPassLabel = new JLabel("Confirmar Contraseña:");
        confPassLabel.setForeground(Color.WHITE);
        panel.add(confPassLabel, c);

        c.gridy++;
        registerConfPassField = new JPasswordField(15);
        setTextFieldSize(registerConfPassField);
        panel.add(registerConfPassField, c);

        c.gridy++;
        c.gridwidth = 1;
        JButton registerBtn = createThemedButton("Registrar");
        panel.add(registerBtn, c);

        c.gridx = 1;
        JButton backBtn = createThemedButton("Volver");
        backBtn.addActionListener(e -> cards.show(cardPanel, "MenuPrincipal"));
        panel.add(backBtn, c);

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
                cards.show(cardPanel, "Login");
            }

            Usuarios.limpiarContrasena(contra);
            Usuarios.limpiarContrasena(confContra);
            registerPassField.setText("");
            registerConfPassField.setText("");
        });

        return panel;
    }

    private JButton createThemedButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Serif", Font.BOLD, 20));
        button.setForeground(new Color(255, 255, 200));
        button.setBackground(new Color(80, 40, 0, 180));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(160, 100, 0), 3),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        button.setMaximumSize(new Dimension(300, 60));
        button.setMinimumSize(new Dimension(300, 60));
        button.setPreferredSize(new Dimension(300, 60));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(130, 60, 0, 200));
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(80, 40, 0, 180));
                button.setForeground(new Color(255, 255, 200));
            }
        });
        return button;
    }
}
