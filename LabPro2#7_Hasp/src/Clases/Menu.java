/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author jerem
 */
public class Menu extends JFrame {

    private static final Color PS_BLUE_DARK = new Color(0, 48, 130);
    private static final Color PS_BLUE_LIGHT = new Color(0, 100, 200);
    private static final Color PS_ACCENT_CYAN = Color.CYAN;
    private static final Color CONSOLE_BG_LIGHT = new Color(220, 240, 255);
    private static final Color CONSOLE_FG_DARK = PS_BLUE_DARK;

    private JPanel cardPanel;
    private CardLayout cardLayout;

    private JTextField txtUsernameAdd;
    private JTextField txtUsernameDeactivate;
    private JTextField txtUsernameInfo;

    private JTextField txtUsernameTrophy;
    private JComboBox<String> cmbTrofeoTipo;
    private JTextField txtJuegoDescripcion;
    private JTextField txtRutaImagen;

    private JLabel lblResultadoTrophy;

    private PSNUsers psnUsers;

    public Menu() {
        super("PSN MANAGER");
        psnUsers = new PSNUsers();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        add(cardPanel);

        cardPanel.add(crearPanelMenuPrincipal(), "MENU");
        cardPanel.add(crearPanelUsuarioBase("AGREGAR USUARIO", "Username:", "CREAR",
                e -> probarAgregarUsuario(txtUsernameAdd.getText().trim()),
                txtUsernameAdd = new JTextField(20)), "ADD");

        cardPanel.add(crearPanelUsuarioBase("DESACTIVAR USUARIO", "Username:", "DESACTIVAR",
                e -> probarDesactivarUsuario(txtUsernameDeactivate.getText().trim()),
                txtUsernameDeactivate = new JTextField(20)), "DEL");

        cardPanel.add(crearPanelUsuarioBase("MOSTRAR INFORMACIÓN", "Username:", "MOSTRAR",
                e -> probarMostrarInfo(txtUsernameInfo.getText().trim()),
                txtUsernameInfo = new JTextField(20)), "INFO");

        cardPanel.add(crearPanelAgregarTrofeo(), "TROPHY");

        cardLayout.show(cardPanel, "MENU");
    }

    private JPanel crearPanelMenuPrincipal() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PS_BLUE_DARK);

        JLabel titulo = crearTituloPanel("PSN MANAGER", 32);
        panel.add(titulo, BorderLayout.NORTH);

        JPanel botones = new JPanel(new GridLayout(5, 1, 0, 15));
        botones.setBackground(PS_BLUE_DARK);
        botones.setBorder(BorderFactory.createEmptyBorder(40, 200, 40, 200));

        JButton b1 = crearBotonUI("AGREGAR USUARIO");
        JButton b2 = crearBotonUI("DESACTIVAR USUARIO");
        JButton b3 = crearBotonUI("AGREGAR TROFEO");
        JButton b4 = crearBotonUI("MOSTRAR INFO");
        JButton b5 = crearBotonUI("SALIR", new Color(180, 0, 0));

        b1.addActionListener(e -> cardLayout.show(cardPanel, "ADD"));
        b2.addActionListener(e -> cardLayout.show(cardPanel, "DEL"));
        b3.addActionListener(e -> cardLayout.show(cardPanel, "TROPHY"));
        b4.addActionListener(e -> cardLayout.show(cardPanel, "INFO"));
        b5.addActionListener(e -> System.exit(0));

        botones.add(b1);
        botones.add(b2);
        botones.add(b3);
        botones.add(b4);
        botones.add(b5);

        panel.add(botones, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelUsuarioBase(String titulo, String lblText, String btnText,
            java.awt.event.ActionListener action, JTextField txtInput) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PS_BLUE_DARK);

        panel.add(crearTituloPanel(titulo, 22), BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBackground(PS_BLUE_DARK);

        JLabel lbl = crearEtiquetaUI(lblText, Color.WHITE);

        JLabel lblResultado = new JLabel("Esperando acción...", SwingConstants.CENTER);
        lblResultado.setPreferredSize(new Dimension(500, 70));
        lblResultado.setOpaque(true);
        lblResultado.setBackground(CONSOLE_BG_LIGHT);
        lblResultado.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        lblResultado.setBorder(BorderFactory.createLineBorder(PS_ACCENT_CYAN, 2));

        JPanel fila = new JPanel();
        fila.setBackground(PS_BLUE_DARK);
        fila.add(lbl);
        fila.add(txtInput);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        centro.add(fila, gbc);
        gbc.gridy = 1;
        centro.add(lblResultado, gbc);

        panel.add(centro, BorderLayout.CENTER);

        JButton btn = crearBotonUI(btnText, new Color(0, 150, 50));
        btn.addActionListener(action);
        btn.addActionListener(e -> lblResultado.setText("✅ Operación completada"));

        JButton volver = crearBotonUI("VOLVER", new Color(150, 0, 0));
        volver.addActionListener(e -> {
            txtInput.setText("");
            lblResultado.setText("Esperando acción...");
            cardLayout.show(cardPanel, "MENU");
        });

        JPanel abajo = new JPanel();
        abajo.setBackground(PS_BLUE_DARK);
        abajo.add(btn);
        abajo.add(volver);

        panel.add(abajo, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearPanelAgregarTrofeo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(PS_BLUE_DARK);

        panel.add(crearTituloPanel("AGREGAR TROFEO", 22), BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 10));
        formulario.setBackground(PS_BLUE_DARK);
        formulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtUsernameTrophy = new JTextField();
        cmbTrofeoTipo = new JComboBox<>(new String[]{"PLATINO", "ORO", "PLATA", "BRONCE"});
        txtJuegoDescripcion = new JTextField();
        txtRutaImagen = new JTextField();

        formulario.add(crearEtiquetaUI("Username", Color.WHITE));
        formulario.add(txtUsernameTrophy);
        formulario.add(crearEtiquetaUI("Tipo", Color.WHITE));
        formulario.add(cmbTrofeoTipo);
        formulario.add(crearEtiquetaUI("Juego", Color.WHITE));
        formulario.add(txtJuegoDescripcion);
        formulario.add(crearEtiquetaUI("Ruta imagen", Color.WHITE));
        formulario.add(txtRutaImagen);

        lblResultadoTrophy = new JLabel("Resultado", SwingConstants.CENTER);
        lblResultadoTrophy.setOpaque(true);
        lblResultadoTrophy.setBackground(CONSOLE_BG_LIGHT);

        panel.add(formulario, BorderLayout.WEST);
        panel.add(lblResultadoTrophy, BorderLayout.CENTER);

        JButton agregar = crearBotonUI("AGREGAR", new Color(0, 150, 50));
        agregar.addActionListener(e -> probarAgregarTrofeo());

        JButton volver = crearBotonUI("VOLVER", new Color(150, 0, 0));
        volver.addActionListener(e -> cardLayout.show(cardPanel, "MENU"));

        JPanel abajo = new JPanel();
        abajo.setBackground(PS_BLUE_DARK);
        abajo.add(agregar);
        abajo.add(volver);

        panel.add(abajo, BorderLayout.SOUTH);
        return panel;
    }

    private void probarAgregarUsuario(String username) {
        try {
            psnUsers.addUser(username);
            showCustomDialog("Usuario agregado correctamente", "OK", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            showCustomDialog("Error al agregar usuario", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void probarDesactivarUsuario(String username) {
        try {
            psnUsers.deactivateUser(username);
            showCustomDialog("Usuario desactivado", "OK", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            showCustomDialog("Error al desactivar", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void probarMostrarInfo(String username) {
        try {
            String info = psnUsers.playerInfo(username);
            lblResultadoTrophy.setText("<html>" + info.replace("\n", "<br>") + "</html>");

            ImageIcon img = psnUsers.getLastTrophyImage();
            if (img != null) {
                Image scaled = img.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                lblResultadoTrophy.setIcon(new ImageIcon(scaled));
            }
            cardLayout.show(cardPanel, "TROPHY");

        } catch (IOException e) {
            showCustomDialog("Error al mostrar información", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void probarAgregarTrofeo() {
        try {
            String user = txtUsernameTrophy.getText();
            String game = txtJuegoDescripcion.getText();
            Trophy tipo = Trophy.valueOf((String) cmbTrofeoTipo.getSelectedItem());

            psnUsers.addTrophieTo(user, game, game, tipo);

            showCustomDialog("Trofeo agregado correctamente", "OK", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            showCustomDialog("Error al agregar trofeo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel crearTituloPanel(String texto, int size) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, size));
        lbl.setForeground(PS_ACCENT_CYAN);
        return lbl;
    }

    private JButton crearBotonUI(String texto) {
        return crearBotonUI(texto, PS_BLUE_LIGHT);
    }

    private JButton crearBotonUI(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        return btn;
    }

    private JLabel crearEtiquetaUI(String texto, Color color) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(color);
        return lbl;
    }

    private void showCustomDialog(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }
}