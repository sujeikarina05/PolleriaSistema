package com.mycompany.polloloco.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Dashboard general del sistema.
 */
public class DashboardFrame extends JFrame {

    private JLabel lblTotalVentas;
    private JLabel lblPedidosHoy;
    private JLabel lblProductoTop;
    private JPanel panelGraficos;
    private JButton btnActualizar;

    public DashboardFrame() {
        super("Dashboard - Pollería Pollo Loco");
        setSize(650, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        initComponents();
        cargarDatos();          // 1ª carga
    }

    /* ---------- UI ---------- */
    private void initComponents() {

        /* ----- KPIs superiores ----- */
        JPanel panelResumen = new JPanel(new GridLayout(1, 3, 15, 15));
        panelResumen.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));
        panelResumen.setBackground(new Color(250, 252, 253));

        lblTotalVentas  = crearKPI("Ventas del día", "S/. 0.00");
        lblPedidosHoy   = crearKPI("Pedidos realizados", "0");
        lblProductoTop  = crearKPI("Más vendido", "Ninguno");

        panelResumen.add(lblTotalVentas);
        panelResumen.add(lblPedidosHoy);
        panelResumen.add(lblProductoTop);

        /* ----- Botón actualizar ----- */
        btnActualizar = new JButton("Actualizar (F5)");
        btnActualizar.addActionListener(this::accActualizar);
        btnActualizar.setMnemonic(KeyEvent.VK_F5);
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        barra.add(btnActualizar);

        /* ----- Panel central (gráficos) ----- */
        panelGraficos = new JPanel(new BorderLayout());
        panelGraficos.setBorder(BorderFactory.createTitledBorder("Resumen gráfico"));
        panelGraficos.add(new JLabel("Aquí irán los gráficos JFreeChart.", SwingConstants.CENTER),
                          BorderLayout.CENTER);

        /* ----- Añadir al frame ----- */
        add(panelResumen, BorderLayout.NORTH);
        add(barra,        BorderLayout.SOUTH);
        add(panelGraficos,BorderLayout.CENTER);

        /* Atajo global F5 */
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                     .put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "REFRESH");
        getRootPane().getActionMap()
                     .put("REFRESH", new AbstractAction() {
                         public void actionPerformed(ActionEvent e) { accActualizar(null); }
                     });
    }

    /* ---------- Lógica de datos ---------- */
    /** Obtiene KPIs y actualiza pantalla (simulado). */
    private void cargarDatos() {
        // TODO: Sustituir por DashboardController / DAO
        setVentasTotales("S/. 345.80");
        setPedidosHoy("27");
        setProductoTop("Pollo Broaster");
        pintarGraficoTortaDemo();   // demo comentable
    }

    private void accActualizar(ActionEvent e) {
        cargarDatos();
    }

    /* ---------- Helpers ---------- */
    private JLabel crearKPI(String titulo, String valor) {
        JLabel lbl = new JLabel();
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setOpaque(true);
        lbl.setBackground(new Color(243, 249, 255));
        lbl.setBorder(BorderFactory.createLineBorder(new Color(180, 200, 240)));
        lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN, 13));
        lbl.setText(formatoKPI(titulo, valor));
        return lbl;
    }

    private String formatoKPI(String titulo, String valor) {
        return "<html><center><b>" + titulo + "</b><br><span style='font-size:18pt;'>" +
               valor + "</span></center></html>";
    }

    /* ---------- Setters públicos para KPIs ---------- */
    public void setVentasTotales(String valor)  { lblTotalVentas.setText(formatoKPI("Ventas del día", valor)); }
    public void setPedidosHoy(String valor)     { lblPedidosHoy.setText(formatoKPI("Pedidos realizados", valor)); }
    public void setProductoTop(String nombre)   { lblProductoTop.setText(formatoKPI("Más vendido", nombre)); }

    /* ---------- Demo de gráfico (comentado) ---------- */
    private void pintarGraficoTortaDemo() {
        panelGraficos.removeAll();
        panelGraficos.add(new JLabel("Demo – integrar JFreeChart aquí.", SwingConstants.CENTER),
                          BorderLayout.CENTER);
        // Ejemplo:
        // DefaultPieDataset ds = new DefaultPieDataset<>();
        // ds.setValue("Delivery", 60);
        // ds.setValue("Mesón", 40);
        // JFreeChart chart = ChartFactory.createPieChart("Pedidos por tipo", ds, true, true, false);
        // ChartPanel cp = new ChartPanel(chart);
        // panelGraficos.add(cp, BorderLayout.CENTER);
        panelGraficos.revalidate();
        panelGraficos.repaint();
    }
}
