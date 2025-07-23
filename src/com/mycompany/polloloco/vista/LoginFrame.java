package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.controlador.UsuarioController;
import javax.swing.*;

public class LoginFrame extends JFrame {
    private final JTextField txtUsuario = new JTextField();
    private final JPasswordField txtClave = new JPasswordField();
    private final JButton btnIngresar = new JButton("Ingresar");
    private final UsuarioController controller = new UsuarioController();

    public LoginFrame() {
        setTitle("Ingreso al sistema - Pollería");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

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
        String usr = txtUsuario.getText();
        String pwd = new String(txtClave.getPassword());
        var usuario = controller.login(usr, pwd);
        if (usuario != null) {
            JOptionPane.showMessageDialog(this, "✅ Bienvenido " + usuario.getNombre());
            switch (usuario.getRolNombre()) {
                case "Administrador" -> new AdminFrame().setVisible(true);
                case "Cajero" -> new CajeroFrame().setVisible(true);
                case "Mozo" -> new MozoFrame().setVisible(true);
                default -> new DashboardFrame().setVisible(true);
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Credenciales incorrectas");
        }
    }
}
