package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.controlador.UsuarioController;
import com.mycompany.polloloco.modelo.Usuario;
import com.mycompany.polloloco.util.Sesion;
import com.mycompany.polloloco.util.ValidadorCampos;

import javax.swing.*;
import java.awt.*;

/**
 * Pantalla de ingreso al sistema Pollería Pollo Loco.
 */
public class LoginFrame extends JFrame {

    private final JTextField txtUsuario = new JTextField();
    private final JPasswordField txtClave = new JPasswordField();
    private final JButton btnIngresar = new JButton("Ingresar");
    private final UsuarioController controller = new UsuarioController();

    public LoginFrame() {
        setTitle("Ingreso al Sistema - Pollería Pollo Loco");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        JLabel lblUsuario = new JLabel("Usuario:");
        JLabel lblClave = new JLabel("Contraseña:");

        lblUsuario.setBounds(40, 30, 100, 25);
        txtUsuario.setBounds(140, 30, 160, 25);
        lblClave.setBounds(40, 70, 100, 25);
        txtClave.setBounds(140, 70, 160, 25);
        btnIngresar.setBounds(120, 120, 100, 30);

        btnIngresar.addActionListener(e -> ingresar());

        add(lblUsuario);
        add(txtUsuario);
        add(lblClave);
        add(txtClave);
        add(btnIngresar);

        setSize(360, 220);
        setLocationRelativeTo(null);
    }

    private void ingresar() {
        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword()).trim();

        if (ValidadorCampos.esVacio(usuario) || ValidadorCampos.esVacio(clave)) {
            JOptionPane.showMessageDialog(this, "⚠️ Ingrese usuario y contraseña.", "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (controller.login(usuario, clave)) {
            Usuario u = Sesion.getUsuarioActual();
            String rol = u.getRol().toLowerCase();

            JOptionPane.showMessageDialog(this, "✅ Bienvenido " + u.getNombre() + " (" + u.getRol() + ")");

            switch (rol) {
                case "administrador" -> new AdminFrame().setVisible(true);
                case "cajero" -> new CajeroFrame().setVisible(true);
                case "mozo" -> new MozoFrame().setVisible(true); // si tienes esta clase
                default -> {
                    JOptionPane.showMessageDialog(this, "⚠️ Rol no reconocido: " + u.getRol());
                    return;
                }
            }

            dispose(); // cerrar login
        } else {
            JOptionPane.showMessageDialog(this, "❌ Usuario o contraseña incorrectos", "Error de autenticación", JOptionPane.ERROR_MESSAGE);
            txtClave.setText("");
        }
    }
}
