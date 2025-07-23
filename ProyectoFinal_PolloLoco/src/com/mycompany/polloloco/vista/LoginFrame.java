package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.controlador.UsuarioController;
import com.mycompany.polloloco.modelo.Usuario;
import com.mycompany.polloloco.util.Sesion;
import com.mycompany.polloloco.util.ValidadorCampos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/** Pantalla de ingreso al sistema Pollería Pollo Loco. */
public class LoginFrame extends JFrame {

    private final JTextField     txtUsuario = new JTextField(15);
    private final JPasswordField txtClave   = new JPasswordField(15);
    private final JButton        btnIngresar     = new JButton("Ingresar");
    private final JButton        btnToggleClave  = new JButton("Mostrar");
    private final UsuarioController controller   = new UsuarioController();

    public LoginFrame() {
        super("Ingreso al Sistema - Pollería Pollo Loco");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initUI();
        pack();
        setLocationRelativeTo(null);
        txtUsuario.requestFocusInWindow();
    }

    /* ---------- Construcción de la UI ---------- */
    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        /* Etiquetas y campos */
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        panel.add(txtUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        panel.add(txtClave, gbc);

        /* Botón mostrar/ocultar clave */
        btnToggleClave.setFocusable(false);
        btnToggleClave.setMargin(new Insets(2,8,2,8));
        btnToggleClave.addActionListener(e -> toggleClave());
        gbc.gridx = 2;
        panel.add(btnToggleClave, gbc);

        /* Botón ingresar */
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        btnIngresar.setPreferredSize(new Dimension(120, 30));
        panel.add(btnIngresar, gbc);

        add(panel);

        /* Listeners */
        btnIngresar.addActionListener(this::accionIngresar);
        // Enter dispara ingreso
        getRootPane().setDefaultButton(btnIngresar);
        // Esc cierra ventana
        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(esc, "SALIR");
        getRootPane().getActionMap().put("SALIR", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { dispose(); }
        });
    }

    /* ---------- Acciones ---------- */
    private void accionIngresar(ActionEvent e) {
        String usuario = txtUsuario.getText().trim();
        String clave   = new String(txtClave.getPassword()).trim();

        if (ValidadorCampos.esVacio(usuario) || ValidadorCampos.esVacio(clave)) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese usuario y contraseña.",
                    "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (controller.login(usuario, clave)) {
            Usuario u = Sesion.getUsuarioActual();
            String rol = u.getRol().toLowerCase();

            JOptionPane.showMessageDialog(this,
                    "Bienvenido " + u.getNombre() + " (" + u.getRol() + ")");

            switch (rol) {
                case "administrador" -> new AdminFrame().setVisible(true);
                case "cajero"       -> new CajeroFrame().setVisible(true);
                case "mozo"         -> new MozoFrame().setVisible(true);
                default             -> JOptionPane.showMessageDialog(this,
                                        "Rol no reconocido: " + u.getRol());
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos",
                    "Autenticación", JOptionPane.ERROR_MESSAGE);
            txtClave.setText("");
        }
    }

    /* Mostrar u ocultar contraseña */
    private void toggleClave() {
        if (txtClave.getEchoChar() == 0) {
            txtClave.setEchoChar('\u2022');   // •
            btnToggleClave.setText("Mostrar");
        } else {
            txtClave.setEchoChar((char)0);
            btnToggleClave.setText("Ocultar");
        }
    }
}
