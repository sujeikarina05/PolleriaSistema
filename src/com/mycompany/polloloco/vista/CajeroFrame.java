package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.modelo.Usuario;
import com.mycompany.polloloco.util.Sesion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Panel principal del cajero en el sistema Pollería Pollo Loco.
 */
public class CajeroFrame extends JFrame {

    private JLabel lblBienvenida;
    private JButton btnRegistrarPago;
    private JButton btnEmitirComprobante;
    private JButton btnExportarExcel;
    private JButton btnCerrarSesion;

    public CajeroFrame() {
        setTitle("Panel de Cajero - Pollería Pollo Loco");
        setSize(500, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        Usuario actual = Sesion.getUsuarioActual();

        // Bienvenida en la parte superior
        lblBienvenida = new JLabel("Bienvenido, " + (actual != null ? actual.getNombre() : "Cajero"), SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblBienvenida, BorderLayout.NORTH);

        // Panel con botones
        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 10, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        btnRegistrarPago = new JButton("Registrar Pago");
        btnEmitirComprobante = new JButton("Emitir Comprobante");
        btnExportarExcel = new JButton("Exportar Ventas a Excel");
        btnCerrarSesion = new JButton("Cerrar Sesión");

        // Acciones de botones
        btnRegistrarPago.addActionListener(this::registrarPago);
        btnEmitirComprobante.addActionListener(this::emitirComprobante);
        btnExportarExcel.addActionListener(this::exportarExcel);
        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        panelBotones.add(btnRegistrarPago);
        panelBotones.add(btnEmitirComprobante);
        panelBotones.add(btnExportarExcel);
        panelBotones.add(btnCerrarSesion);

        add(panelBotones, BorderLayout.CENTER);
    }

    // Acciones futuras (puedes vincular a clases reales)
    private void registrarPago(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Funcionalidad de registro de pago pendiente.");
        // new PagoFrame().setVisible(true);
    }

    private void emitirComprobante(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Funcionalidad de comprobante pendiente.");
        // new ComprobanteFrame().setVisible(true);
    }

    private void exportarExcel(ActionEvent e) {
        JOptionPane.showMessageDialog(this, "Exportación de ventas a Excel pendiente.");
        // Puedes usar ReporteExcelExporter aquí
    }
}
