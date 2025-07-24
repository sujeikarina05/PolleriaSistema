package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.controlador.UsuarioController;
import com.mycompany.polloloco.modelo.Usuario;
import com.mycompany.polloloco.util.Sesion;
import com.mycompany.polloloco.util.ValidadorCampos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/** Pantalla de ingreso al sistema Pollería Pollo Loco. */
public class LoginFrame extends JFrame {

    /* --------‑ Paleta e íconos ‑-------- */
    private static final Color BG_MAIN   = new Color(0xFFF8E1);
    private static final Color BG_FIELD  = Color.WHITE;
    private static final Color BG_BTN    = new Color(0xFFD54F);
    private static final Color FG_BTN    = Color.DARK_GRAY;
    private static final String LOGO_URL =
            "https://i.ibb.co/FXKrzbM/logo-pollo-loco.png"; // cualquier PNG 120×120 transparente

    /* --------‑ Componentes ‑-------- */
    private final JTextField     txtUsuario = new JTextField(15);
    private final JPasswordField txtClave   = new JPasswordField(15);
    private final JButton        btnIngresar    = new JButton("Ingresar");
    private final JToggleButton  btnToggleClave = new JToggleButton("Mostrar");
    private final UsuarioController controller   = new UsuarioController();

    public LoginFrame() {
        super("Ingreso al Sistema – Pollería Pollo Loco");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        construirUI();
        pack();
        setLocationRelativeTo(null);
        txtUsuario.requestFocusInWindow();
    }

    /* ------------------------------------------------- */
    private void construirUI() {
        /* ===== Contenedor ===== */
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_MAIN);
        root.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(root);

        /* ===== Logo ===== */
        JLabel logo = new JLabel(new ImageIcon(
                new ImageIcon(LOGO_URL).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        root.add(logo, BorderLayout.NORTH);

        /* ===== Formulario ===== */
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        // Estilo campos redondeados
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(6, 8, 6, 8)));
        txtClave.setBorder(txtUsuario.getBorder());
        txtUsuario.setBackground(BG_FIELD);
        txtClave.setBackground(BG_FIELD);

        /* Usuario */
        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        form.add(txtUsuario, gbc);

        /* Contraseña + toggle */
        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        form.add(txtClave, gbc);
        btnToggleClave.setFocusable(false);
        btnToggleClave.setMargin(new Insets(2, 8, 2, 8));
        gbc.gridx = 2;
        form.add(btnToggleClave, gbc);

        /* Botón ingresar */
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        btnIngresar.setBackground(BG_BTN);
        btnIngresar.setForeground(FG_BTN);
        btnIngresar.setFocusPainted(false);
        btnIngresar.setPreferredSize(new Dimension(150, 32));
        form.add(btnIngresar, gbc);

        root.add(form, BorderLayout.CENTER);

        /* ===== Listeners ===== */
        btnIngresar.addActionListener(this::accionIngresar);
        btnToggleClave.addActionListener(e -> toggleClave());

        // Enter = ingresar
        getRootPane().setDefaultButton(btnIngresar);
        // Esc = salir
        KeyStroke esc = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(esc, "SALIR");
        getRootPane().getActionMap().put("SALIR",
                new AbstractAction() { public void actionPerformed(ActionEvent e) { dispose(); } });
    }

    /* ------------------------------------------------- */
    private void accionIngresar(ActionEvent e) {
        String usuario = txtUsuario.getText().trim();
        String clave   = new String(txtClave.getPassword()).trim();

        if (ValidadorCampos.esVacio(usuario) || ValidadorCampos.esVacio(clave)) {
            JOptionPane.showMessageDialog(this, "Ingrese usuario y contraseña.",
                    "Campos vacíos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (controller.login(usuario, clave)) {
            Usuario u   = Sesion.getUsuarioActual();
            String rolL = u.getRol().toLowerCase();

            JOptionPane.showMessageDialog(this,
                    "Bienvenido "+u.getNombre()+" ("+u.getRol()+")");

            switch (rolL) {
                case "administrador" -> new AdminFrame().setVisible(true);
                case "cajero"       -> new CajeroFrame().setVisible(true);
                case "mozo"         -> new MozoFrame().setVisible(true);
                default -> JOptionPane.showMessageDialog(this,
                        "Rol no reconocido: "+u.getRol());
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos",
                    "Autenticación", JOptionPane.ERROR_MESSAGE);
            txtClave.setText("");
        }
    }

    /* Cambia entre mostrar / ocultar contraseña */
    private void toggleClave() {
        boolean mostrar = btnToggleClave.isSelected();
        txtClave.setEchoChar(mostrar ? 0 : '\u2022');
        btnToggleClave.setText(mostrar ? "Ocultar" : "Mostrar");
    }
}
