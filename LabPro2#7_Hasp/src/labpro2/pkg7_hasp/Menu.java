/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package labpro2.pkg7_hasp;

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

    // --- Constantes y Colores de Ambientación PlayStation ---
    private static final Color PS_BLUE_DARK = new Color(0, 48, 130);
    private static final Color PS_BLUE_LIGHT = new Color(0, 100, 200);
    private static final Color PS_ACCENT_CYAN = Color.CYAN;
    private static final Color CONSOLE_BG_LIGHT = new Color(220, 240, 255); 
    private static final Color CONSOLE_FG_DARK = PS_BLUE_DARK; 
    
    // --- Componentes para CardLayout ---
    private JPanel cardPanel;
    private CardLayout cardLayout;

    // --- Componentes de Estado y Entrada ---
    private JTextField txtUsernameAdd;
    private JTextField txtUsernameDeactivate;
    private JTextField txtUsernameInfo;
    
    private JTextField txtUsernameTrophy;
    private JComboBox<String> cmbTrofeoTipo;
    private JTextField txtJuegoDescripcion;
    private JTextField txtRutaImagen;
    
    private JLabel lblResultadoTrophy; // Label para Mostrar Trofeo/Info en el panel de Trofeos
    
    // Aquí puedes descomentar y usar tu lógica:
    // private PSNUsers psnUsers;

    public Menu() {
        super("PSN MANAGER");

        // --- Configuración Inicial de la Ventana ---
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 600); 
        setLocationRelativeTo(null); 
        
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        add(cardPanel);

        // Inicialización de Paneles (Cartas)
        cardPanel.add(crearPanelMenuPrincipal(), "MENU_PRINCIPAL");
        
        cardPanel.add(crearPanelUsuarioBase("AGREGAR NUEVO USUARIO", "Username Único:", "CREAR USUARIO", 
                                            e -> probarAgregarUsuario(txtUsernameAdd.getText().trim()), 
                                            txtUsernameAdd = new JTextField(20)), "ADD_USER");
        
        cardPanel.add(crearPanelUsuarioBase("DESACTIVAR USUARIO", "Username a Desactivar:", "DESACTIVAR", 
                                            e -> probarDesactivarUsuario(txtUsernameDeactivate.getText().trim()), 
                                            txtUsernameDeactivate = new JTextField(20)), "DEACTIVATE_USER");
        
        cardPanel.add(crearPanelUsuarioBase("MOSTRAR INFORMACIÓN COMPLETA", "Username a Buscar:", "BUSCAR INFO", 
                                            e -> probarMostrarInfo(txtUsernameInfo.getText().trim()), 
                                            txtUsernameInfo = new JTextField(20)), "SHOW_INFO");
        
        cardPanel.add(crearPanelAgregarTrofeo(), "ADD_TROPHY");
        
        cardLayout.show(cardPanel, "MENU_PRINCIPAL");
    }

    // ====================================================================
    // 1. Panel: MENÚ PRINCIPAL
    // ====================================================================
    private JPanel crearPanelMenuPrincipal() {
        JPanel panel = new JPanel();
        panel.setBackground(PS_BLUE_DARK);
        panel.setLayout(new BorderLayout(15, 15));

        // Título
        JLabel lblTitulo = crearTituloPanel("PSN MANAGER", 30);
        panel.add(lblTitulo, BorderLayout.NORTH);

        // Panel de Botones (Central)
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(5, 1, 0, 15)); 
        panelBotones.setBackground(PS_BLUE_DARK);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 100, 30, 100)); 

        // Botones del Menú
        JButton btnAddUser = crearBotonUI("AGREGAR USUARIO");
        JButton btnDeactivate = crearBotonUI("DESACTIVAR USUARIO");
        JButton btnAddTrophy = crearBotonUI("AGREGAR TROFEO");
        JButton btnShowInfo = crearBotonUI("MOSTRAR INFO COMPLETA");
        JButton btnExit = crearBotonUI("SALIR", new Color(180, 0, 0)); 

        // Eventos para cambiar de "carta"
        btnAddUser.addActionListener(e -> cardLayout.show(cardPanel, "ADD_USER"));
        btnDeactivate.addActionListener(e -> cardLayout.show(cardPanel, "DEACTIVATE_USER"));
        btnAddTrophy.addActionListener(e -> cardLayout.show(cardPanel, "ADD_TROPHY"));
        btnShowInfo.addActionListener(e -> cardLayout.show(cardPanel, "SHOW_INFO"));
        btnExit.addActionListener(e -> System.exit(0));

        panelBotones.add(btnAddUser);
        panelBotones.add(btnDeactivate);
        panelBotones.add(btnAddTrophy);
        panelBotones.add(btnShowInfo);
        panelBotones.add(btnExit);

        panel.add(panelBotones, BorderLayout.CENTER);
        return panel;
    }

    // ====================================================================
    // 2. Método Base para Paneles de 1 Campo (ADD, DEACTIVATE, INFO)
    // ====================================================================
    private JPanel crearPanelUsuarioBase(String titulo, String lblText, String btnText, 
                                        java.awt.event.ActionListener action, JTextField txtInput) {
        
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PS_BLUE_DARK);

        // Título con margen
        panel.add(crearTituloPanel(titulo, 22), BorderLayout.NORTH);

        // Panel Central (Input y Label de Resultado)
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(PS_BLUE_DARK);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Input de Username (Arriba Centrado)
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        JPanel inputWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        inputWrapper.setBackground(PS_BLUE_DARK);
        
        JLabel lblInput = crearEtiquetaUI(lblText, Color.WHITE);
        
        inputWrapper.add(lblInput);
        inputWrapper.add(txtInput);
        panelCentral.add(inputWrapper, gbc);

        // --- VALORES INICIALES DEL LABEL DE RESULTADO (CONSOLA) ---
        final String initialText = "Escribe algo arriba o Kratos te encontrara";
        final Color initialColor = CONSOLE_FG_DARK;
        
        // Label de Resultado (Centro Abajo - Ajuste de Estilo)
        JLabel lblResultado = new JLabel(initialText, SwingConstants.CENTER);
        lblResultado.setPreferredSize(new Dimension(500, 70));
        lblResultado.setOpaque(true);
        lblResultado.setBackground(CONSOLE_BG_LIGHT); 
        lblResultado.setForeground(initialColor); 
        lblResultado.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        lblResultado.setBorder(BorderFactory.createLineBorder(PS_ACCENT_CYAN, 3));
        
        gbc.gridy = 1; gbc.insets = new Insets(30, 10, 30, 10);
        panelCentral.add(lblResultado, gbc);

        // Botones de Acción
        JButton btnAccion = crearBotonUI(btnText, new Color(0, 150, 50));
        btnAccion.addActionListener(action);
        
        // Listener para actualizar el JLabel (Solo la parte de UI)
        btnAccion.addActionListener(e -> {
            lblResultado.setText("Jajaja te asustaste... Operacion Exitosa");
            lblResultado.setForeground(Color.GREEN); // Texto de éxito en verde
        });

        panel.add(panelCentral, BorderLayout.CENTER);
        
        // --- CREACIÓN DEL PANEL DE ACCIÓN Y RESETEO INTERNO (SIN USAR crearPanelAccion) ---
        JPanel panelAccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelAccion.setBackground(PS_BLUE_DARK);

        JButton btnVolver = crearBotonUI("VOLVER AL MENU", new Color(150, 0, 0));
        
        // El ActionListener que limpia el campo de texto y el JLabel de resultado (Reset)
        btnVolver.addActionListener(e -> {
            // 1. Limpia el campo de texto
            txtInput.setText("");
            // 2. Resetea el JLabel de Resultado al estado inicial (VUELVE A LA NORMALIDAD)
            lblResultado.setText(initialText);
            lblResultado.setForeground(initialColor); 
            
            // 3. Vuelve al menú principal
            cardLayout.show(cardPanel, "MENU_PRINCIPAL");
        });

        panelAccion.add(btnAccion);
        panelAccion.add(btnVolver);
        
        panel.add(panelAccion, BorderLayout.SOUTH);
        // --- FIN DE LA CREACIÓN DEL PANEL DE ACCIÓN ---

        return panel;
    }

    // ====================================================================
    // 3. Panel: AGREGAR TROFEO
    // ====================================================================
    private JPanel crearPanelAgregarTrofeo() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(PS_BLUE_DARK);
        
        // Título
        JLabel lblTitulo = crearTituloPanel("AGREGAR TROFEO", 22);
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        // --- Panel Principal (Centro) ---
        JPanel mainContentPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainContentPanel.setBackground(PS_BLUE_DARK);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        // Panel de Formulario (Izquierda)
        JPanel formPanel = crearFormularioTrofeo();
        mainContentPanel.add(formPanel);
        
        // Panel de Salida / Resultado (Derecha - Label con Imagen)
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(PS_BLUE_DARK);
        resultPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PS_ACCENT_CYAN), "INFORMACION", 0, 0, new Font("Arial", Font.BOLD, 14), PS_ACCENT_CYAN));
        
        lblResultadoTrophy = new JLabel("<html><div style='text-align: center; color: "+ (CONSOLE_FG_DARK.getRGB() & 0xFFFFFF) +";'>Esperando la adición de un trofeo.</div></html>", 
                                        SwingConstants.CENTER);
        lblResultadoTrophy.setPreferredSize(new Dimension(300, 300));
        lblResultadoTrophy.setOpaque(true);
        lblResultadoTrophy.setBackground(CONSOLE_BG_LIGHT);
        lblResultadoTrophy.setForeground(CONSOLE_FG_DARK);
        lblResultadoTrophy.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(PS_BLUE_DARK);
        wrapper.add(lblResultadoTrophy);
        
        resultPanel.add(wrapper, BorderLayout.CENTER);
        mainContentPanel.add(resultPanel);
        
        panel.add(mainContentPanel, BorderLayout.CENTER);
        
        JButton btnAdd = crearBotonUI("AGREGAR TROFEO", new Color(0, 150, 50)); 
        btnAdd.addActionListener(e -> probarAgregarTrofeo());
        
        // AHORA LLAMAMOS AL MÉTODO crearPanelAccion QUE ES EL QUE TIENE EL BOTÓN DE VOLVER CENTRALIZADO
        panel.add(crearPanelAccion(btnAdd), BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel crearFormularioTrofeo() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(PS_BLUE_DARK);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        
        // Inicialización de componentes
        txtUsernameTrophy = new JTextField(15);
        cmbTrofeoTipo = new JComboBox<>(new String[]{"PLATINO", "ORO", "PLATA", "BRONCE"});
        txtJuegoDescripcion = new JTextField(15);
        txtRutaImagen = new JTextField(15);
        txtRutaImagen.setEditable(false);
        JButton btnSeleccionarImagen = crearBotonUI("...", PS_BLUE_LIGHT);
        btnSeleccionarImagen.setFont(new Font("Arial", Font.BOLD, 12));
        btnSeleccionarImagen.setPreferredSize(new Dimension(30, 25));

        // Campos: Username, Tipo Trofeo, Juego/Descripción, Ruta de Imagen
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3; formPanel.add(crearEtiquetaUI("Username:", Color.WHITE), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7; formPanel.add(txtUsernameTrophy, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(crearEtiquetaUI("Tipo Trofeo:", Color.WHITE), gbc);
        gbc.gridx = 1; gbc.gridy = 1; formPanel.add(cmbTrofeoTipo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(crearEtiquetaUI("Juego/Descripción:", Color.WHITE), gbc);
        gbc.gridx = 1; gbc.gridy = 2; formPanel.add(txtJuegoDescripcion, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(crearEtiquetaUI("Ruta Imagen:", Color.WHITE), gbc);
        JPanel panelRuta = new JPanel(new BorderLayout(5, 0));
        panelRuta.add(txtRutaImagen, BorderLayout.CENTER);
        panelRuta.add(btnSeleccionarImagen, BorderLayout.EAST);
        panelRuta.setBackground(PS_BLUE_DARK);
        gbc.gridx = 1; gbc.gridy = 3; formPanel.add(panelRuta, gbc);
        
        btnSeleccionarImagen.addActionListener(e -> seleccionarArchivoImagen(txtRutaImagen));
        
        return formPanel;
    }

    // ====================================================================
    // 4. Métodos de Utilidad Comunes
    // ====================================================================
    
    // MÉTODO MODIFICADO PARA INCLUIR LA LIMPIEZA DE CAMPOS ANTES DE VOLVER
    private JPanel crearPanelAccion(JButton btnAccion) {
        JPanel panelAccion = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelAccion.setBackground(PS_BLUE_DARK);

        JButton btnVolver = crearBotonUI("VOLVER AL MENÚ", new Color(150, 0, 0));
        
        btnVolver.addActionListener(e -> {
            limpiarCampos(); // <--- LLAMA A LA FUNCIÓN DE LIMPIEZA PARA EL PANEL DE TROFEOS
            cardLayout.show(cardPanel, "MENU_PRINCIPAL");
        });

        panelAccion.add(btnAccion);
        panelAccion.add(btnVolver);
        
        return panelAccion;
    }
    
    // ====================================================================
    // MÉTODOS DE LIMPIEZA
    // ====================================================================
    private void limpiarCampos() {
        // NOTA: Los campos en ADD, DEACTIVATE, INFO se limpian en el propio ActionListener
        // del botón "Volver" en crearPanelUsuarioBase. 
        // Aquí solo limpiamos los campos que no están ligados a ese método base (Trofeos).
        
        if (txtUsernameTrophy != null) txtUsernameTrophy.setText("");
        if (txtJuegoDescripcion != null) txtJuegoDescripcion.setText("");
        if (txtRutaImagen != null) txtRutaImagen.setText("");
        if (cmbTrofeoTipo != null) cmbTrofeoTipo.setSelectedIndex(0);
        
        // Limpiar el Label de resultados de Trofeos (Variable de Clase)
        if (lblResultadoTrophy != null) {
            lblResultadoTrophy.setIcon(null);
            lblResultadoTrophy.setText("<html><center>Esperando la adición de un trofeo.</center></html>");
            // Resetear la alineación para el estado inicial
            lblResultadoTrophy.setHorizontalTextPosition(SwingConstants.CENTER);
            lblResultadoTrophy.setVerticalTextPosition(SwingConstants.CENTER);
            lblResultadoTrophy.setHorizontalAlignment(SwingConstants.CENTER);
            lblResultadoTrophy.setVerticalAlignment(SwingConstants.CENTER);
            lblResultadoTrophy.setForeground(CONSOLE_FG_DARK);
        }
    }
    
    // (Resto de métodos de utilidad como showCustomDialog, crearTituloPanel, crearBotonUI, etc., sin cambios)

    private void showCustomDialog(String message, String title, int messageType) {
        JDialog dialog = new JDialog(this, title, true); 
        dialog.getContentPane().setBackground(PS_BLUE_DARK);
        dialog.setLayout(new BorderLayout(10, 10));

        JLabel iconLabel;
        
        if (messageType == JOptionPane.ERROR_MESSAGE) {
            iconLabel = new JLabel("SI", SwingConstants.CENTER);
        } else {
            iconLabel = new JLabel("NO", SwingConstants.CENTER);
        }
        iconLabel.setFont(new Font("Arial", Font.BOLD, 30));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JLabel messageLabel = new JLabel("<html><p style='color: white; font-size: 14px;'>"+ message +"</p></html>", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JButton okButton = new JButton("ACEPTAR");
        okButton.setBackground(PS_BLUE_LIGHT);
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(e -> dialog.dispose());

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(PS_BLUE_DARK);
        southPanel.add(okButton);
        
        dialog.add(iconLabel, BorderLayout.WEST);
        dialog.add(messageLabel, BorderLayout.CENTER);
        dialog.add(southPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private JLabel crearTituloPanel(String texto, int fontSize) {
        JLabel lblTitulo = new JLabel(texto, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, fontSize));
        lblTitulo.setForeground(PS_ACCENT_CYAN);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0)); 
        return lblTitulo;
    }

    private JButton crearBotonUI(String texto) {
        return crearBotonUI(texto, PS_BLUE_LIGHT);
    }
    
    private JButton crearBotonUI(String texto, Color bgColor) {
        JButton btn = new JButton(texto);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PS_ACCENT_CYAN, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        return btn;
    }
    
    private JLabel crearEtiquetaUI(String texto, Color fgColor) {
        JLabel lbl = new JLabel(texto);
        lbl.setForeground(fgColor);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        return lbl;
    }

    private void seleccionarArchivoImagen(JTextField targetField) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Imagen de Trofeo (.png/.jpg)");
        
        int userSelection = fileChooser.showOpenDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            targetField.setText(fileToSave.getAbsolutePath());
        }
    }


    // ====================================================================
    // 5. Métodos de Prueba 
    // ====================================================================

    private void probarAgregarUsuario(String username) {
        if (username.isEmpty()) {
            showCustomDialog("El campo de Username no puede estar vacío.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        showCustomDialog("Se ha agregado el usuario (" + username + ") con exito.", "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
    }

    private void probarDesactivarUsuario(String username) {
        if (username.isEmpty()) {
            showCustomDialog("El campo de Username no puede estar vacío.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        showCustomDialog("Se ha desactivado el usuario (" + username + ") con exito.", "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
    }

    private void probarMostrarInfo(String username) {
        if (username.isEmpty()) {
            showCustomDialog("El campo de Username no puede estar vacío.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Muestra de prueba: Se muestra la información simulada en el Label de Trofeo.
        String infoSimulada = "<html><center><b>INFO JUGADOR: " + username + "</b></center><hr>" +
                              "Puntos: 15 / Trofeos: 5<br>" +
                              "FECHA: 2025-01-15 – ORO – Spider-Man 2 – Web Master<br>" +
                              "FECHA: 2024-12-01 – BRONCE – Elden Ring – Pot Boi</html>";
                              
        lblResultadoTrophy.setIcon(null); 
        lblResultadoTrophy.setText(infoSimulada);
        // Ajustamos la alineación para que el texto se vea bien en el cuadrado.
        lblResultadoTrophy.setHorizontalTextPosition(SwingConstants.CENTER);
        lblResultadoTrophy.setVerticalTextPosition(SwingConstants.CENTER);
        lblResultadoTrophy.setHorizontalAlignment(SwingConstants.CENTER);
        lblResultadoTrophy.setVerticalAlignment(SwingConstants.CENTER);

        cardLayout.show(cardPanel, "ADD_TROPHY"); 
    }
    
    private void probarAgregarTrofeo() {
        String username = txtUsernameTrophy.getText().trim();
        String tipoStr = (String) cmbTrofeoTipo.getSelectedItem();
        String juegoDesc = txtJuegoDescripcion.getText().trim();
        String rutaImagen = txtRutaImagen.getText().trim();
        
        if (username.isEmpty() || rutaImagen.isEmpty()) {
            showCustomDialog("Faltan datos (Username o Ruta de Imagen).", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            File file = new File(rutaImagen);
            if (!file.exists()) {
                 throw new IOException("El archivo no existe en la ruta especificada.");
            }
            
            Image image = ImageIO.read(file);
            
            // **ESCALADO DE IMAGEN POR DEFECTO (100x100)**
            Image scaledImage = image.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            lblResultadoTrophy.setIcon(new ImageIcon(scaledImage));
            
            // Texto informativo debajo de la imagen
            String trofeoInfo = "<html><center>✅ TROFEO AGREGADO</center><br>Usuario: <b>" + username + "</b><br>Trofeo: <b>" + tipoStr + "</b><br>Juego: <b>" + juegoDesc + "</html>";
            lblResultadoTrophy.setText(trofeoInfo);
            
            // Posiciona el texto debajo de la imagen y centra el conjunto
            lblResultadoTrophy.setHorizontalTextPosition(SwingConstants.CENTER);
            lblResultadoTrophy.setVerticalTextPosition(SwingConstants.BOTTOM);
            lblResultadoTrophy.setHorizontalAlignment(SwingConstants.CENTER);
            lblResultadoTrophy.setVerticalAlignment(SwingConstants.CENTER);
            
            showCustomDialog("Llamada a addTrophieTo(). Simulación de Éxito.", "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            lblResultadoTrophy.setIcon(null);
            lblResultadoTrophy.setText("<html><center> ERROR AL CARGAR IMAGEN:<br>Ruta inválida (" + e.getMessage() + ")</center></html>");
            lblResultadoTrophy.setHorizontalTextPosition(SwingConstants.CENTER);
            lblResultadoTrophy.setVerticalTextPosition(SwingConstants.CENTER);
            showCustomDialog("ERROR: No se pudo cargar la imagen. " + e.getMessage(), "Operación Fallida", JOptionPane.ERROR_MESSAGE);
        }
    }
}