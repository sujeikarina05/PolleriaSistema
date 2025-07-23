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
    private final JButton        btnIngresar = new JButton("Ingresar");
    private final UsuarioController controller = new UsuarioController();

    public LoginFrame() {
        super("Ingreso al Sistema - Pollería Pollo Loco");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        construirUI();
        pack();
        setLocationRelativeTo(null);
        txtUsuario.requestFocusInWindow();
    }

    /* ---------- UI ---------- */
    private void construirUI() {
        JPanel cont = new JPanel(new GridBagLayout());
        cont.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.anchor = GridBagConstraints.WEST;

        // Usuario
        g.gridx = 0; g.gridy = 0; cont.add(new JLabel("Usuario:"), g);
        g.gridx = 1; cont.add(txtUsuario, g);

        // Clave
        g.gridx = 0; g.gridy = 1; cont.add(new JLabel("Contraseña:"), g);
        g.gridx = 1; cont.add(txtClave, g);

        // Botón ingresar
        g.gridx = 1; g.gridy = 2; g.anchor = GridBagConstraints.CENTER;
        btnIngresar.setPreferredSize(new Dimension(120,30));
        cont.add(btnIngresar, g);

        add(cont);

        /* Listeners */
        btnIngresar.addActionListener(this::accionIngresar);
        getRootPane().setDefaultButton(btnIngresar);           // Enter
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                     .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),"SALIR");
        getRootPane().getActionMap()
                     .put("SALIR", new AbstractAction(){ public void actionPerformed(ActionEvent e){ dispose(); }});
    }

    /* ---------- Acción de login ---------- */
    private void accionIngresar(ActionEvent e) {
        String usu = txtUsuario.getText().trim();
        String cla = new String(txtClave.getPassword()).trim();

        if (ValidadorCampos.esVacio(usu) || ValidadorCampos.esVacio(cla)) {
            JOptionPane.showMessageDialog(this, "Ingrese usuario y contraseña.",
                                          "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (controller.login(usu, cla)) {                 // <-- devuelve boolean
            Usuario u = Sesion.getUsuarioActual();        // <-- usuario actual
            JOptionPane.showMessageDialog(this,
                    "Bienvenido " + u.getNombre() + " (" + u.getRol() + ")");

            switch (u.getRol().toLowerCase()) {
                case "administrador" -> new AdminFrame().setVisible(true);
                case "cajero"        -> new CajeroFrame().setVisible(true);
                case "mozo"          -> new MozoFrame().setVisible(true);
                default -> JOptionPane.showMessageDialog(this,
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
}
