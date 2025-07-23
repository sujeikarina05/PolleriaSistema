package polleria;

import javax.swing.*;

public class LoginFrame extends javax.swing.JFrame {

    public LoginFrame() {
        initComponents();
        setLocationRelativeTo(null); // Centrar ventana
        setTitle("Ingreso al sistema - Pollería");
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        JLabel lblUsuario = new JLabel("Usuario:");
        JLabel lblClave = new JLabel("Contraseña:");

        txtUsuario = new JTextField();
        txtClave = new JPasswordField();
        btnIngresar = new JButton("Ingresar");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnIngresar.addActionListener(evt -> btnIngresarActionPerformed(evt));

        // Layout manual (más adelante puedes usar GroupLayout)
        setLayout(null);

        lblUsuario.setBounds(40, 30, 100, 25);
        txtUsuario.setBounds(140, 30, 160, 25);
        lblClave.setBounds(40, 70, 100, 25);
        txtClave.setBounds(140, 70, 160, 25);
        btnIngresar.setBounds(120, 120, 100, 30);

        add(lblUsuario);
        add(txtUsuario);
        add(lblClave);
        add(txtClave);
        add(btnIngresar);

        setSize(360, 220);
    }

    private void btnIngresarActionPerformed(java.awt.event.ActionEvent evt) {
        String usuario = txtUsuario.getText();
        String clave = new String(txtClave.getPassword());

        if (usuario.equals("admin") && clave.equals("1234")) {
            JOptionPane.showMessageDialog(this, "✅ Bienvenido a la pollería 🍗");
            // Aquí puedes abrir AdminFrame u otra ventana
        } else {
            JOptionPane.showMessageDialog(this, "❌ Usuario o contraseña incorrectos");
        }
    }

    // Componentes
    private JTextField txtUsuario;
    private JPasswordField txtClave;
    private JButton btnIngresar;
}
