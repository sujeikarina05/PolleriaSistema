package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.modelo.Usuario;
import com.mycompany.polloloco.util.Sesion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Ventana principal del administrador del sistema.
 */
public class AdminFrame extends JFrame {

    private JLabel lblBienvenida;
    private JButton btnUsuarios;
    private JButton btnProductos;
    private JButton btnReportes;
    private JButton btnCerrarSesion;

    public AdminFrame() {
        setTitle("Panel de Administrador - Pollería Pollo Loco");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        Usuario actual = Sesion.getUsuarioActual();

        // Panel superior de bienvenida
        lblBienvenida = new JLabel("Bienvenido, " + (actual != null ? actual.getNombre() : "Administrador"), SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 16));
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblBienvenida, BorderLayout.NORTH);

        // Panel central con botones
        JPanel panelCentro = new JPanel(new GridLayout(4, 1, 10, 10));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        btnUsuarios = new JButton("Gestión de Usuarios");
        btnProductos = new JButton("Gestión de Productos");
        btnReportes = new JButton("Reportes y Ventas");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        btnUsuarios.addActionListener(this::gestionarUsuarios);
        btnProductos.addActionListener(this::gestionarProductos);
        btnReportes.addActionListener(this::verReportes);
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        panelCentro.add(btnUsuarios);
        panelCentro.add(btnProductos);
        panelCentro.add(btnReportes);
        panelCentro.add(btnCerrarSesion);

        add(panelCentro, BorderLayout.CENTER);
    }

    private void gestionarUsuarios(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Abrir gestión de usuarios (pendiente).");
        // new UsuarioFrame().setVisible(true);
    }

    private void gestionarProductos(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Abrir gestión de productos (pendiente).");
        // new ProductoFrame().setVisible(true);
    }

    private void verReportes(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Abrir módulo de reportes (pendiente).");
        // new ReporteFrame().setVisible(true);
    }
}
