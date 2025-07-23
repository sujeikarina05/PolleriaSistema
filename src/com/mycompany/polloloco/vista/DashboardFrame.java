package com.mycompany.polloloco.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Dashboard general del sistema.
 */
public class DashboardFrame extends JFrame {

    private JLabel lblTotalVentas;
    private JLabel lblPedidosHoy;
    private JLabel lblProductoTop;
    private JPanel panelGraficos;

    public DashboardFrame() {
        setTitle("Dashboard - Pollería Pollo Loco");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        // Panel superior con KPIs
        JPanel panelResumen = new JPanel(new GridLayout(1, 3, 15, 15));
        panelResumen.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblTotalVentas = crearKPI("Ventas del día", "S/. 0.00");
        lblPedidosHoy = crearKPI("Pedidos realizados", "0");
        lblProductoTop = crearKPI("Más vendido", "Ninguno");

        panelResumen.add(lblTotalVentas);
        panelResumen.add(lblPedidosHoy);
        panelResumen.add(lblProductoTop);

        // Panel central para gráficos (en el futuro)
        panelGraficos = new JPanel();
        panelGraficos.setLayout(new BorderLayout());
        panelGraficos.setBorder(BorderFactory.createTitledBorder("Resumen gráfico (pendiente)"));
        panelGraficos.add(new JLabel("Aquí irán los gráficos con JFreeChart o JavaFX."), BorderLayout.CENTER);

        add(panelResumen, BorderLayout.NORTH);
        add(panelGraficos, BorderLayout.CENTER);
    }

    private JLabel crearKPI(String titulo, String valor) {
        JLabel label = new JLabel("<html><center><strong>" + titulo + "</strong><br><span style='font-size:18pt;'>" + valor + "</span></center></html>", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(240, 248, 255));
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return label;
    }

    // Métodos que puedes usar para actualizar los KPIs dinámicamente
    public void setVentasTotales(String valor) {
        lblTotalVentas.setText(formatoKPI("Ventas del día", valor));
    }

    public void setPedidosHoy(String valor) {
        lblPedidosHoy.setText(formatoKPI("Pedidos realizados", valor));
    }

    public void setProductoTop(String nombre) {
        lblProductoTop.setText(formatoKPI("Más vendido", nombre));
    }

    private String formatoKPI(String titulo, String valor) {
        return "<html><center><strong>" + titulo + "</strong><br><span style='font-size:18pt;'>" + valor + "</span></center></html>";
    }
}
