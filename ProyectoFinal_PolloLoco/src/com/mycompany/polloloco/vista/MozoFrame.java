package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.modelo.Usuario;
import com.mycompany.polloloco.util.Sesion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/** Panel principal para mozos: creación de pedidos y gestión de mesas. */
public class MozoFrame extends JFrame {

    private JLabel lblBienvenida;
    private JButton btnNuevoPedido;
    private JButton btnVerMesas;
    private JButton btnCerrarSesion;

    public MozoFrame() {
        super("Panel de Mozo - Pollería Pollo Loco");
        setSize(520, 350);
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
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblBienvenida, BorderLayout.NORTH);

        /* Botonera */
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.weightx = 1;

        btnNuevoPedido   = new JButton("Registrar nuevo pedido");
        btnVerMesas      = new JButton("Ver mesas disponibles");
        btnCerrarSesion  = new JButton("Cerrar sesión");

        btnNuevoPedido.setMnemonic(KeyEvent.VK_N);    // Alt+N
        btnVerMesas.setMnemonic(KeyEvent.VK_M);       // Alt+M
        btnCerrarSesion.setMnemonic(KeyEvent.VK_Q);   // Alt+Q

        btnNuevoPedido.setToolTipText("Ctrl+N – Abrir registro de pedidos");
        btnVerMesas.setToolTipText("Ctrl+M – Mostrar estado de mesas");
        btnCerrarSesion.setToolTipText("Ctrl+Q – Cerrar sesión");

        btnNuevoPedido.addActionListener(this::abrirPedido);
        btnVerMesas.addActionListener(this::verMesas);
        btnCerrarSesion.addActionListener(this::cerrarSesion);

        gbc.gridy = 0; centro.add(btnNuevoPedido,  gbc);
        gbc.gridy = 1; centro.add(btnVerMesas,     gbc);
        gbc.gridy = 2; centro.add(btnCerrarSesion, gbc);

        add(centro, BorderLayout.CENTER);

        /* Foco inicial */
        SwingUtilities.invokeLater(btnNuevoPedido::requestFocusInWindow);

        /* Atajos globales Ctrl+N / Ctrl+M / Ctrl+Q */
        registrarAccesosDirectos();
    }

    private void actualizarBienvenida() {
        Usuario u = Sesion.getUsuarioActual();
        String nombre = (u != null && u.getNombre() != null) ? u.getNombre() : "Mozo";
        lblBienvenida.setText("Bienvenido, " + nombre);
    }

    /* ---------- Acciones ---------- */
    private void abrirPedido(ActionEvent e) {
        new PedidoFrame().setVisible(true);
    }

    private void verMesas(ActionEvent e) {
        new MesaFrame().setVisible(true);
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

    /* Atajos globales */
    private void registrarAccesosDirectos() {
        JRootPane root = getRootPane();
        InputMap  im   = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am   = root.getActionMap();

        im.put(KeyStroke.getKeyStroke("ctrl N"), "NUEVO_PEDIDO");
        im.put(KeyStroke.getKeyStroke("ctrl M"), "VER_MESAS");
        im.put(KeyStroke.getKeyStroke("ctrl Q"), "CERRAR_SESION");

        am.put("NUEVO_PEDIDO",  new AbstractAction() { public void actionPerformed(ActionEvent e){ abrirPedido(e);} });
        am.put("VER_MESAS",     new AbstractAction() { public void actionPerformed(ActionEvent e){ verMesas(e);} });
        am.put("CERRAR_SESION", new AbstractAction() { public void actionPerformed(ActionEvent e){ cerrarSesion(e);} });
    }
}
