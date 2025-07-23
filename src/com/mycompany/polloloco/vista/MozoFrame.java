package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.modelo.Usuario;
import com.mycompany.polloloco.util.Sesion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Panel de Mozo - Pollería Pollo Loco
 */
public class MozoFrame extends JFrame {

    private JLabel lblBienvenida;
    private JButton btnNuevoPedido;
    private JButton btnVerMesas;
    private JButton btnCerrarSesion;

    public MozoFrame() {
        setTitle("Panel de Mozo - Pollería Pollo Loco");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        Usuario actual = Sesion.getUsuarioActual();

        // Panel superior con bienvenida
        lblBienvenida = new JLabel("Bienvenido, " + (actual != null ? actual.getNombre() : "Mozo"), SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblBienvenida, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelCentral = new JPanel(new GridLayout(3, 1, 15, 15));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        btnNuevoPedido = new JButton("Registrar nuevo pedido");
        btnVerMesas = new JButton("Ver mesas disponibles");
        btnCerrarSesion = new JButton("Cerrar sesión");

        btnNuevoPedido.addActionListener(this::abrirPedido);
        btnVerMesas.addActionListener(this::verMesas);
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        panelCentral.add(btnNuevoPedido);
        panelCentral.add(btnVerMesas);
        panelCentral.add(btnCerrarSesion);

        add(panelCentral, BorderLayout.CENTER);
    }

    // Métodos placeholder
    private void abrirPedido(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Funcionalidad de pedido aún no implementada.");
        // new PedidoFrame().setVisible(true);
    }

    private void verMesas(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Funcionalidad de mesas aún no implementada.");
        // new MesaFrame().setVisible(true);
    }
}
