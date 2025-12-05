/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import javax.swing.*;
import java.awt.*;
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

    private PSNUsers psnUsers;

    public Menu() {
        super("PSN MANAGER");
        psnUsers = new PSNUsers();

        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(PS_BLUE_DARK);

        add(crearMenuPrincipal(), BorderLayout.CENTER);
    }

    private JPanel crearMenuPrincipal() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 0, 15));
        panel.setBackground(PS_BLUE_DARK);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 200, 40, 200));

        JButton b1 = crearBotonUI("AGREGAR USUARIO");
        JButton b2 = crearBotonUI("DESACTIVAR USUARIO");
        JButton b3 = crearBotonUI("AGREGAR TROFEO");
        JButton b4 = crearBotonUI("MOSTRAR INFO");
        JButton b5 = crearBotonUI("SALIR", new Color(180, 0, 0));

        b1.addActionListener(e -> frameAgregarUsuario());
        b2.addActionListener(e -> frameDesactivarUsuario());
        b3.addActionListener(e -> frameAgregarTrofeo());
        b4.addActionListener(e -> frameMostrarInfo());
        b5.addActionListener(e -> System.exit(0));

        panel.add(b1);
        panel.add(b2);
        panel.add(b3);
        panel.add(b4);
        panel.add(b5);

        return panel;
    }

    private void frameAgregarUsuario() {
        JFrame f = new JFrame("AGREGAR USUARIO");
        f.setSize(400, 200);
        f.setLocationRelativeTo(this);
        f.setLayout(new GridLayout(3, 1));

        JTextField txt = new JTextField();
        JButton btn = new JButton("AGREGAR");

        btn.addActionListener(e -> {
            try {
                psnUsers.addUser(txt.getText());
                JOptionPane.showMessageDialog(f, "Usuario agregado");
                f.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });

        f.add(new JLabel("Username", SwingConstants.CENTER));
        f.add(txt);
        f.add(btn);
        f.setVisible(true);
    }

    private void frameDesactivarUsuario() {
        JFrame f = new JFrame("DESACTIVAR USUARIO");
        f.setSize(400, 200);
        f.setLocationRelativeTo(this);
        f.setLayout(new GridLayout(3, 1));

        JTextField txt = new JTextField();
        JButton btn = new JButton("DESACTIVAR");

        btn.addActionListener(e -> {
            try {
                psnUsers.deactivateUser(txt.getText());
                JOptionPane.showMessageDialog(f, "Usuario desactivado");
                f.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });

        f.add(new JLabel("Username", SwingConstants.CENTER));
        f.add(txt);
        f.add(btn);
        f.setVisible(true);
    }

    private void frameAgregarTrofeo() {
        JFrame f = new JFrame("AGREGAR TROFEO");
        f.setSize(450, 300);
        f.setLocationRelativeTo(this);
        f.setLayout(new GridLayout(4, 2));

        JTextField txtUser = new JTextField();
        JTextField txtGame = new JTextField();
        JComboBox<String> cmb = new JComboBox<>(new String[]{"PLATINO", "ORO", "PLATA", "BRONCE"});
        JButton btn = new JButton("AGREGAR");

        btn.addActionListener(e -> {

            String user = txtUser.getText().trim();
            String game = txtGame.getText().trim();

            if (user.isEmpty()) {
                JOptionPane.showMessageDialog(f,
                        "El username no puede estar vacío",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (game.isEmpty()) {
                JOptionPane.showMessageDialog(f,
                        "El nombre del juego NO puede estar vacío",
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Trophy tipo = Trophy.valueOf(cmb.getSelectedItem().toString());

                psnUsers.addTrophieTo(user, game, game, tipo);

                JOptionPane.showMessageDialog(f,
                        "Trofeo agregado correctamente");

                f.dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f,
                        " ERROR: " + ex.getMessage(),
                        "ERROR",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        f.add(new JLabel("Usuario"));
        f.add(txtUser);
        f.add(new JLabel("Juego"));
        f.add(txtGame);
        f.add(new JLabel("Tipo"));
        f.add(cmb);
        f.add(new JLabel(""));
        f.add(btn);

        f.setVisible(true);
    }

    private void frameMostrarInfo() {

        String username = JOptionPane.showInputDialog(this, "Ingrese el username");

        if (username == null || username.trim().isEmpty()) {
            return;
        }

        try {
            String info = psnUsers.playerInfo(username);

            if (info.equals("Usuario no existe")) {
                JOptionPane.showMessageDialog(this, "Usuario no existe");
                return;
            }

            JFrame f = new JFrame("INFORMACIoN DEL JUGADOR");
            f.setSize(800, 550);
            f.setLocationRelativeTo(this);
            f.setLayout(new BorderLayout());

            JTextArea areaUsuario = new JTextArea(info.split("Trofeos obtenidos:")[0]);
            areaUsuario.setEditable(false);
            areaUsuario.setFont(new Font("Monospaced", Font.BOLD, 14));
            f.add(areaUsuario, BorderLayout.NORTH);

            JPanel panelTrofeos = new JPanel();
            panelTrofeos.setLayout(new BoxLayout(panelTrofeos, BoxLayout.Y_AXIS));
            JScrollPane scroll = new JScrollPane(panelTrofeos);
            f.add(scroll, BorderLayout.CENTER);

            RandomAccessFile trophies = new RandomAccessFile("trophies.psn", "r");

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

                    JPanel panelTrofeo = new JPanel();
                    panelTrofeo.setLayout(new BoxLayout(panelTrofeo, BoxLayout.Y_AXIS));
                    panelTrofeo.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    panelTrofeo.setBackground(Color.WHITE);

                    JLabel txt = new JLabel(
                            fecha + " - " + tipo + " - " + game + " - " + desc
                    );

                    txt.setFont(new Font("Arial", Font.BOLD, 14));
                    txt.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                    JLabel imgLabel = new JLabel();
                    imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    imgLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));

                    if (img.length > 0) {
                        ImageIcon icon = new ImageIcon(img);
                        Image esc = icon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
                        imgLabel.setIcon(new ImageIcon(esc));
                    } else {
                        imgLabel.setText("Sin imagen");
                    }

                    panelTrofeo.add(txt);
                    panelTrofeo.add(imgLabel);

                    panelTrofeos.add(panelTrofeo);
                    panelTrofeos.add(Box.createVerticalStrut(15));
                }
            }

            trophies.close();
            f.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al mostrar info");
        }
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
}
