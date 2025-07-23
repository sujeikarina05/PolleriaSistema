package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.modelo.Usuario;
import com.mycompany.polloloco.util.Sesion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/** Panel principal del administrador del sistema. */
public class AdminFrame extends JFrame {

    private JLabel   lblBienvenida;
    private JButton  btnUsuarios;
    private JButton  btnProductos;
    private JButton  btnReportes;
    private JButton  btnCerrarSesion;

    public AdminFrame() {
        super("Panel de Administrador - Pollería Pollo Loco");
        setSize(520, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        actualizarBienvenida();
    }

    /* ---------- UI ---------- */
    private void initComponents() {
        /* Cabecera */
        lblBienvenida = new JLabel("", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        add(lblBienvenida, BorderLayout.NORTH);

        /* Botonera */
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.weightx = 1;

        btnUsuarios       = new JButton("Gestión de usuarios");
        btnProductos      = new JButton("Gestión de productos");
        btnReportes       = new JButton("Reportes y ventas");
        btnCerrarSesion   = new JButton("Cerrar sesión");

        /* Atajos de teclado */
        btnUsuarios.setMnemonic(KeyEvent.VK_U);     // Alt+U
        btnProductos.setMnemonic(KeyEvent.VK_P);    // Alt+P
        btnReportes.setMnemonic(KeyEvent.VK_R);     // Alt+R
        btnCerrarSesion.setMnemonic(KeyEvent.VK_Q); // Alt+Q

        /* Tooltips */
        btnUsuarios.setToolTipText("Ctrl+U – Mantener usuarios");
        btnProductos.setToolTipText("Ctrl+P – Mantener catálogo de productos");
        btnReportes.setToolTipText("Ctrl+R – Ver reportes y ventas");
        btnCerrarSesion.setToolTipText("Ctrl+Q – Cerrar sesión");

        /* Listeners */
        btnUsuarios.addActionListener(this::gestionarUsuarios);
        btnProductos.addActionListener(this::gestionarProductos);
        btnReportes.addActionListener(this::verReportes);
        btnCerrarSesion.addActionListener(this::cerrarSesion);

        /* Añadir botones */
        gbc.gridy = 0; centro.add(btnUsuarios,     gbc);
        gbc.gridy = 1; centro.add(btnProductos,    gbc);
        gbc.gridy = 2; centro.add(btnReportes,     gbc);
        gbc.gridy = 3; centro.add(btnCerrarSesion, gbc);

        add(centro, BorderLayout.CENTER);

        /* Foco inicial */
        SwingUtilities.invokeLater(btnUsuarios::requestFocusInWindow);

        /* Atajos globales */
        registrarAccesosDirectos();
    }

    private void actualizarBienvenida() {
        Usuario u = Sesion.getUsuarioActual();
        String nombre = (u != null && u.getNombre() != null) ? u.getNombre() : "Administrador";
        lblBienvenida.setText("Bienvenido, " + nombre);
    }

    /* ---------- Acciones ---------- */
    private void gestionarUsuarios(ActionEvent e) {
        JOptionPane.showMessageDialog(this,
                "Módulo de usuarios aún no implementado.",
                "En desarrollo", JOptionPane.INFORMATION_MESSAGE);
        // TODO: new UsuarioFrame().setVisible(true);
    }

    private void gestionarProductos(ActionEvent e) {
        JOptionPane.showMessageDialog(this,
                "Módulo de productos aún no implementado.",
                "En desarrollo", JOptionPane.INFORMATION_MESSAGE);
        // TODO: new ProductoFrame().setVisible(true);
    }

    private void verReportes(ActionEvent e) {
        // new ReporteFrame().setVisible(true);
        JOptionPane.showMessageDialog(this,
                "Módulo de reportes aún no implementado.",
                "En desarrollo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cerrarSesion(ActionEvent e) {
        int op = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas cerrar sesión?",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (op == JOptionPane.YES_OPTION) {
            Sesion.setUsuarioActual(null);
            dispose();
            new LoginFrame().setVisible(true);
        }
    }

    /* Atajos globales Ctrl+U/P/R/Q */
    private void registrarAccesosDirectos() {
        JRootPane root = getRootPane();
        InputMap  im   = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am   = root.getActionMap();

        im.put(KeyStroke.getKeyStroke("ctrl U"), "USUARIOS");
        im.put(KeyStroke.getKeyStroke("ctrl P"), "PRODUCTOS");
        im.put(KeyStroke.getKeyStroke("ctrl R"), "REPORTES");
        im.put(KeyStroke.getKeyStroke("ctrl Q"), "SALIR");

        am.put("USUARIOS",  new AbstractAction(){ public void actionPerformed(ActionEvent e){ gestionarUsuarios(e);} });
        am.put("PRODUCTOS", new AbstractAction(){ public void actionPerformed(ActionEvent e){ gestionarProductos(e);} });
        am.put("REPORTES",  new AbstractAction(){ public void actionPerformed(ActionEvent e){ verReportes(e);} });
        am.put("SALIR",     new AbstractAction(){ public void actionPerformed(ActionEvent e){ cerrarSesion(e);} });
    }
}
