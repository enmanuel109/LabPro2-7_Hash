/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import javax.imageio.ImageIO;

/**
 *
 * @author jerem
 */
public class Menu extends JFrame {

    private static final Color PS_BLUE_DARK = new Color(0, 48, 130);
    private static final Color PS_BLUE_LIGHT = new Color(0, 100, 200);
    private static final Color PS_ACCENT_CYAN = Color.CYAN;

    private static final Color STATUS_BG = new Color(20, 20, 30);
    private static final Color STATUS_SUCCESS = new Color(50, 205, 50);
    private static final Color STATUS_ERROR = new Color(255, 69, 0);

    private static final String PATH_PLATINO = "src/imgtrofeos/PLATINO.png";
    private static final String PATH_ORO = "src/imgtrofeos/ORO.png";
    private static final String PATH_PLATA = "src/imgtrofeos/PLATA.png";
    private static final String PATH_BRONCE = "src/imgtrofeos/BRONCE.png";

    private PSNUsers psnUsers;

    private JPanel cardPanel;
    private CardLayout cardLayout;

    private JTextField txtUsernameAdd;
    private JTextField txtUsernameDeactivate;

    private JTextField txtUserTrophy;
    private JTextField txtGameTrophy;
    private JComboBox<String> cmbTrofeoTipo;
    private JLabel lblPreviewTrophy;
    private JPanel previewPanel;
    private String selectedTrophyPath;

    private JTextField txtSearchUser;
    private JTextArea txtInfoJugador;
    private JPanel panelListaTrofeos;

    public Menu() {
        super("PSN MANAGER");

        psnUsers = new PSNUsers();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        add(cardPanel);

        cardPanel.add(crearPanelMenuPrincipal(), "MENU_PRINCIPAL");
        cardPanel.add(crearPanelUsuarioBase("AGREGAR NUEVO USUARIO", "Escribir nombre:", "AGREGAR",
                true,
                txtUsernameAdd = new JTextField(20)), "ADD_USER");
        cardPanel.add(crearPanelUsuarioBase("DESACTIVAR USUARIO", "Username a Desactivar:", "DESACTIVAR",
                false,
                txtUsernameDeactivate = new JTextField(20)), "DEACTIVATE_USER");
        cardPanel.add(crearPanelAgregarTrofeo(), "ADD_TROPHY");
        cardPanel.add(crearPanelMostrarInfoCompleta(), "SHOW_INFO");

        cardLayout.show(cardPanel, "MENU_PRINCIPAL");
    }

    private JPanel crearPanelMenuPrincipal() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(PS_BLUE_DARK);

        JLabel lblTitulo = crearTituloPanel("PSN MANAGER", 36);
        panel.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(5, 1, 0, 15));
        panelBotones.setBackground(PS_BLUE_DARK);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 150, 30, 150));

        JButton btn1 = crearBotonUI("AGREGAR USUARIO");
        JButton btn2 = crearBotonUI("DESACTIVAR USUARIO");
        JButton btn3 = crearBotonUI("AGREGAR TROFEO");
        JButton btn4 = crearBotonUI("MOSTRAR INFO");
        JButton btn5 = crearBotonUI("SALIR", new Color(180, 0, 0));

        btn1.addActionListener(e -> cardLayout.show(cardPanel, "ADD_USER"));
        btn2.addActionListener(e -> cardLayout.show(cardPanel, "DEACTIVATE_USER"));
        btn3.addActionListener(e -> {
            cardLayout.show(cardPanel, "ADD_TROPHY");
            actualizarImagenPorDefecto();
        });
        btn4.addActionListener(e -> cardLayout.show(cardPanel, "SHOW_INFO"));
        btn5.addActionListener(e -> System.exit(0));

        panelBotones.add(btn1);
        panelBotones.add(btn2);
        panelBotones.add(btn3);
        panelBotones.add(btn4);
        panelBotones.add(btn5);

        panel.add(panelBotones, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelUsuarioBase(String titulo, String lblText, String btnText,
            boolean esAgregar, JTextField txtInput) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PS_BLUE_DARK);

        panel.add(crearTituloPanel(titulo, 24), BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(PS_BLUE_DARK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCentral.add(crearEtiquetaUI(lblText, Color.WHITE), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panelCentral.add(txtInput, gbc);

        panel.add(panelCentral, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout(10, 10));
        panelInferior.setBackground(PS_BLUE_DARK);
        panelInferior.setBorder(BorderFactory.createEmptyBorder(10, 50, 30, 50));

        JLabel lblStatus = new JLabel("Esperando accion", SwingConstants.CENTER);
        lblStatus.setOpaque(true);
        lblStatus.setBackground(STATUS_BG);
        lblStatus.setForeground(Color.GRAY);
        lblStatus.setFont(new Font("Monospaced", Font.BOLD, 14));
        lblStatus.setPreferredSize(new Dimension(100, 45));
        lblStatus.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setBackground(PS_BLUE_DARK);

        JButton btnAccion = crearBotonUI(btnText, new Color(0, 150, 50));
        JButton btnVolver = crearBotonUI("VOLVER", new Color(150, 0, 0));

        btnAccion.addActionListener(e -> {
            String username = txtInput.getText().trim();

            if (username.isEmpty()) {
                lblStatus.setText("Error: El campo esta¡ vaci­o");
                lblStatus.setForeground(STATUS_ERROR);
                lblStatus.setBorder(BorderFactory.createLineBorder(STATUS_ERROR));
                return;
            }

            try {
                if (esAgregar) {
                    psnUsers.addUser(username);
                    lblStatus.setText("Exito: Usuario '" + username + "' agregado");
                    lblStatus.setForeground(STATUS_SUCCESS);
                    lblStatus.setBorder(BorderFactory.createLineBorder(STATUS_SUCCESS));
                    txtInput.setText("");
                } else {
                    psnUsers.deactivateUser(username);
                    lblStatus.setText("Exito: Usuario '" + username + "' desactivado");
                    lblStatus.setForeground(STATUS_SUCCESS);
                    lblStatus.setBorder(BorderFactory.createLineBorder(STATUS_SUCCESS));
                    txtInput.setText("");
                }
            } catch (Exception ex) {
                lblStatus.setText("Error: " + ex.getMessage());
                lblStatus.setForeground(STATUS_ERROR);
                lblStatus.setBorder(BorderFactory.createLineBorder(STATUS_ERROR));
            }
        });

        btnVolver.addActionListener(e -> {
            limpiarCampos();
            lblStatus.setText("Esperando accion");
            lblStatus.setForeground(Color.GRAY);
            lblStatus.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            cardLayout.show(cardPanel, "MENU_PRINCIPAL");
        });

        panelBotones.add(btnAccion);
        panelBotones.add(btnVolver);

        panelInferior.add(lblStatus, BorderLayout.NORTH);
        panelInferior.add(panelBotones, BorderLayout.CENTER);

        panel.add(panelInferior, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelAgregarTrofeo() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PS_BLUE_DARK);
        panel.add(crearTituloPanel("AGREGAR TROFEO", 24), BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBackground(PS_BLUE_DARK);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(PS_BLUE_DARK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtUserTrophy = new JTextField(15);
        txtGameTrophy = new JTextField(15);

        cmbTrofeoTipo = new JComboBox<>(new String[]{"PLATINO", "ORO", "PLATA", "BRONCE"});
        cmbTrofeoTipo.addActionListener(e -> actualizarImagenPorDefecto());

        agregarFilaForm(formPanel, "Usuario:", txtUserTrophy, gbc, 0);
        agregarFilaForm(formPanel, "Juego:", txtGameTrophy, gbc, 1);
        agregarFilaForm(formPanel, "Tipo:", cmbTrofeoTipo, gbc, 2);

        previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBackground(PS_BLUE_DARK);
        previewPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(PS_ACCENT_CYAN), "VISTA PREVIA", 0, 0, new Font("Arial", Font.BOLD, 12), Color.WHITE));

        lblPreviewTrophy = new JLabel();
        lblPreviewTrophy.setHorizontalAlignment(SwingConstants.CENTER);
        lblPreviewTrophy.setPreferredSize(new Dimension(150, 150));
        previewPanel.add(lblPreviewTrophy, BorderLayout.CENTER);

        mainPanel.add(formPanel);
        mainPanel.add(previewPanel);
        panel.add(mainPanel, BorderLayout.CENTER);

        JPanel panelAccion = new JPanel(new FlowLayout());
        panelAccion.setBackground(PS_BLUE_DARK);

        JButton btnAdd = crearBotonUI("AGREGAR", new Color(0, 150, 50));
        btnAdd.addActionListener(e -> logicAgregarTrofeo());

        JButton btnVolver = crearBotonUI("VOLVER", new Color(150, 0, 0));
        btnVolver.addActionListener(e -> {
            limpiarCampos();
            cardLayout.show(cardPanel, "MENU_PRINCIPAL");
        });

        panelAccion.add(btnAdd);
        panelAccion.add(btnVolver);
        panel.add(panelAccion, BorderLayout.SOUTH);

        return panel;
    }

    private void actualizarImagenPorDefecto() {
        String user = txtUserTrophy.getText().trim();
        String game = txtGameTrophy.getText().trim();

        String tipo = (String) cmbTrofeoTipo.getSelectedItem();
        String pathImagen = "";

        switch (tipo) {
            case "PLATINO":
                pathImagen = PATH_PLATINO;
                break;
            case "ORO":
                pathImagen = PATH_ORO;
                break;
            case "PLATA":
                pathImagen = PATH_PLATA;
                break;
            case "BRONCE":
                pathImagen = PATH_BRONCE;
                break;
            default:
                pathImagen = null;
        }

        selectedTrophyPath = pathImagen;

        previewPanel.removeAll();
        previewPanel.setLayout(new BorderLayout());

        lblPreviewTrophy.setIcon(null);
        lblPreviewTrophy.setText("");

        if (selectedTrophyPath != null) {
            try {
                BufferedImage img = ImageIO.read(new File(selectedTrophyPath));
                if (img != null) {
                    Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    lblPreviewTrophy.setIcon(new ImageIcon(scaledImg));
                    lblPreviewTrophy.setText("");
                } else {
                    lblPreviewTrophy.setText("No se encontrÃ³ imagen");
                }
            } catch (IOException e) {
                lblPreviewTrophy.setText("Error al cargar imagen");
            }
        }

        previewPanel.add(lblPreviewTrophy, BorderLayout.CENTER);

        JLabel lblDatos = new JLabel(
                "<html><center>VISTA PREVIA</center><br>"
                + "Usuario: " + user + "<br>"
                + "Juego: " + game + "<br>"
                + "Tipo: <b><font color='" + getColorHTML(tipo) + "'>" + tipo + "</font></b></html>"
        );
        lblDatos.setHorizontalAlignment(SwingConstants.CENTER);
        lblDatos.setForeground(Color.WHITE);
        lblDatos.setFont(new Font("Arial", Font.PLAIN, 14));
        previewPanel.add(lblDatos, BorderLayout.SOUTH);

        previewPanel.revalidate();
        previewPanel.repaint();
    }

    private void agregarFilaForm(JPanel p, String lbl, JComponent cmp, GridBagConstraints gbc, int y) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.weightx = 0.3;
        p.add(crearEtiquetaUI(lbl, Color.WHITE), gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        p.add(cmp, gbc);
    }

    private JPanel crearPanelMostrarInfoCompleta() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PS_BLUE_DARK);

        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(PS_BLUE_DARK);

        txtSearchUser = new JTextField(15);
        JButton btnBuscar = crearBotonUI("BUSCAR JUGADOR");
        btnBuscar.addActionListener(e -> logicMostrarInfo());

        topPanel.add(crearEtiquetaUI("Username:", Color.WHITE));
        topPanel.add(txtSearchUser);
        topPanel.add(btnBuscar);

        panel.add(topPanel, BorderLayout.NORTH);

        JPanel resultContainer = new JPanel(new BorderLayout());

        txtInfoJugador = new JTextArea(3, 40);
        txtInfoJugador.setEditable(false);
        txtInfoJugador.setFont(new Font("Monospaced", Font.BOLD, 14));
        txtInfoJugador.setBackground(new Color(220, 240, 255));
        txtInfoJugador.setForeground(PS_BLUE_DARK);
        resultContainer.add(txtInfoJugador, BorderLayout.NORTH);

        panelListaTrofeos = new JPanel();
        panelListaTrofeos.setLayout(new BoxLayout(panelListaTrofeos, BoxLayout.Y_AXIS));
        panelListaTrofeos.setBackground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(panelListaTrofeos);
        scroll.setBorder(BorderFactory.createTitledBorder("TROFEOS OBTENIDOS"));
        resultContainer.add(scroll, BorderLayout.CENTER);

        panel.add(resultContainer, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(PS_BLUE_DARK);
        JButton btnVolver = crearBotonUI("VOLVER AL MENU", new Color(150, 0, 0));
        btnVolver.addActionListener(e -> {
            limpiarCampos();
            cardLayout.show(cardPanel, "MENU_PRINCIPAL");
        });
        bottomPanel.add(btnVolver);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void logicAgregarTrofeo() {
        String user = txtUserTrophy.getText().trim();
        String game = txtGameTrophy.getText().trim();
        String tipoStr = cmbTrofeoTipo.getSelectedItem().toString();

        if (user.isEmpty() || game.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Usuario y Juego son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Trophy tipo = Trophy.valueOf(tipoStr);

            ImageIcon icon = null;
            if (selectedTrophyPath != null) {
                try {
                    BufferedImage img = ImageIO.read(new File(selectedTrophyPath));
                    if (img != null) {
                        Image scaledImg = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                        icon = new ImageIcon(scaledImg);
                    }
                } catch (Exception e) {
                }
            }

            psnUsers.addTrophieTo(user, game, game, tipo);

            JOptionPane.showMessageDialog(this, "TROFEO AGREGADO!");
            
            JPanel panelResumen = new JPanel(new BorderLayout(10, 5));
            panelResumen.setBackground(PS_BLUE_DARK);
            panelResumen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel titulo = new JLabel("<html><center>TROFEO AGREGADO!</center></html>", SwingConstants.CENTER);
            titulo.setFont(new Font("Arial", Font.BOLD, 16));
            panelResumen.add(titulo, BorderLayout.NORTH);

            JLabel lblDetalles = new JLabel(
                    "<html><b>Usuario:</b> " + user + "<br>"
                    + "<b>Tipo:</b> <font color='" + getColorHTML(tipoStr) + "'>" + tipoStr + "</font><br>"
                    + "<b>Juego:</b> " + game + "</html>"
            );
            lblDetalles.setFont(new Font("Arial", Font.PLAIN, 14));
            panelResumen.add(lblDetalles, BorderLayout.CENTER);

            JLabel lblImgResumen = new JLabel();
            lblImgResumen.setHorizontalAlignment(SwingConstants.CENTER);
            if (icon != null) {
                lblImgResumen.setIcon(icon);
            } else {
                lblImgResumen.setText("Sin Img");
            }
            panelResumen.add(lblImgResumen, BorderLayout.WEST);

            previewPanel.removeAll();
            previewPanel.setLayout(new BorderLayout());
            previewPanel.setBackground(PS_BLUE_DARK);

            previewPanel.add(panelResumen, BorderLayout.CENTER);
            previewPanel.revalidate();
            previewPanel.repaint();

            txtUserTrophy.setText("");
            txtGameTrophy.setText("");
            cmbTrofeoTipo.setSelectedIndex(0);

            Timer timer = new Timer(3000, e -> {
                actualizarImagenPorDefecto();
                ((Timer) e.getSource()).stop();
            });
            timer.setRepeats(false);
            timer.start();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "ERROR: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            actualizarImagenPorDefecto();
        }
    }

    private String getColorHTML(String tipo) {
        switch (tipo) {
            case "PLATINO":
                return "#E5E4E2";
            case "ORO":
                return "#FFD700";
            case "PLATA":
                return "#C0C0C0";
            case "BRONCE":
                return "#CD7F32";
            default:
                return "#FFFFFF";
        }
    }

    private void logicMostrarInfo() {
        String username = txtSearchUser.getText().trim();
        if (username.isEmpty()) {
            return;
        }

        txtInfoJugador.setText("");
        panelListaTrofeos.removeAll();
        panelListaTrofeos.revalidate();
        panelListaTrofeos.repaint();

        try {
            String info = psnUsers.playerInfo(username);

            if (info == null || info.equalsIgnoreCase("Usuario no existe") || info.contains("no existe")) {
                JOptionPane.showMessageDialog(this, "Usuario no existe", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            txtInfoJugador.setText(info.split("Trofeos obtenidos:")[0]);

            RandomAccessFile trophies = new RandomAccessFile("trophies.psn", "r");
            boolean tieneTrofeos = false;

            while (trophies.getFilePointer() < trophies.length()) {
                String user = trophies.readUTF();
                String tipo = trophies.readUTF();
                String game = trophies.readUTF();
                String desc = trophies.readUTF();
                String fecha = trophies.readUTF();
                int size = trophies.readInt();
                byte[] img = new byte[size];
                trophies.readFully(img);

                if (user.equalsIgnoreCase(username)) {
                    tieneTrofeos = true;
                    JPanel itemPanel = new JPanel(new BorderLayout(10, 10));
                    itemPanel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                            BorderFactory.createEmptyBorder(10, 10, 10, 10)
                    ));
                    itemPanel.setBackground(Color.WHITE);
                    itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

                    JLabel lblText = new JLabel("<html><b>" + game + "</b> (" + tipo + ")<br><i>" + desc + "</i><br>" + fecha + "</html>");
                    lblText.setFont(new Font("Arial", Font.PLAIN, 14));

                    JLabel lblImg = new JLabel();
                    if (img.length > 0) {
                        ImageIcon icon = new ImageIcon(img);
                        Image esc = icon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                        lblImg.setIcon(new ImageIcon(esc));
                    } else {
                        lblImg.setText("Sin Img");
                    }

                    itemPanel.add(lblImg, BorderLayout.WEST);
                    itemPanel.add(lblText, BorderLayout.CENTER);

                    panelListaTrofeos.add(itemPanel);
                }
            }
            trophies.close();

            if (!tieneTrofeos) {
                JLabel lblVacio = new JLabel("Este jugador no tiene trofeos aÃºn.");
                lblVacio.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                panelListaTrofeos.add(lblVacio);
            }

            panelListaTrofeos.revalidate();
            panelListaTrofeos.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al leer datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtUsernameAdd.setText("");
        txtUsernameDeactivate.setText("");
        txtUserTrophy.setText("");
        txtGameTrophy.setText("");
        txtSearchUser.setText("");
        txtInfoJugador.setText("");
        if (panelListaTrofeos != null) {
            panelListaTrofeos.removeAll();
        }
        if (cmbTrofeoTipo != null) {
            cmbTrofeoTipo.setSelectedIndex(0);
        }
    }

    private JLabel crearTituloPanel(String texto, int size) {
        JLabel lbl = new JLabel(texto, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, size));
        lbl.setForeground(PS_ACCENT_CYAN);
        lbl.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        return lbl;
    }

    private JButton crearBotonUI(String texto) {
        return crearBotonUI(texto, PS_BLUE_LIGHT);
    }

    private JButton crearBotonUI(String texto, Color bg) {
        JButton btn = new JButton(texto);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PS_ACCENT_CYAN),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        return btn;
    }

    private JLabel crearEtiquetaUI(String txt, Color c) {
        JLabel l = new JLabel(txt);
        l.setForeground(c);
        l.setFont(new Font("Arial", Font.BOLD, 14));
        return l;
    }
}