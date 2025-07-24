package com.mycompany.polloloco.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/** Dashboard general del sistema. */
public class DashboardFrame extends JFrame {

    /* ------------ paleta ------------ */
    private static final Color BG_MAIN     = new Color(0xFFF8E1);
    private static final Color BG_CARD     = new Color(0xF3F9FF);
    private static final Color BD_CARD     = new Color(0xC4D5F5);
    private static final Color BG_BTN      = new Color(0xFFD54F);
    private static final Color FG_BTN      = new Color(60, 60, 60);

    private JLabel lblTotalVentas;
    private JLabel lblPedidosHoy;
    private JLabel lblProductoTop;
    private final JPanel panelGraficos = new JPanel(new BorderLayout());

    public DashboardFrame() {
        super("Dashboard – Pollería Pollo Loco");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(740, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        construirUI();
        cargarDatos(); // 1.ª carga
    }

    /* ---------------- UI ---------------- */
    private void construirUI() {

        /* ---------- KPIs ---------- */
        JPanel resumen = new JPanel(new GridLayout(1, 3, 18, 18));
        resumen.setBorder(new EmptyBorder(25, 25, 10, 25));
        resumen.setBackground(BG_MAIN);

        lblTotalVentas = crearCard("Ventas del día", "S/. 0.00");
        lblPedidosHoy  = crearCard("Pedidos realizados", "0");
        lblProductoTop = crearCard("Más vendido", "‑‑");

        resumen.add(lblTotalVentas);
        resumen.add(lblPedidosHoy);
        resumen.add(lblProductoTop);
        add(resumen, BorderLayout.NORTH);

        /* ---------- Botón actualizar ---------- */
        JButton btnActualizar = new JButton(" Actualizar (F5)");
        btnActualizar.setFocusPainted(false);
        btnActualizar.setIcon(new ImageIcon(
                new ImageIcon("https://i.ibb.co/vJBzjDx/reload.png")
                     .getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH)));
        btnActualizar.setBackground(BG_BTN);
        btnActualizar.setForeground(FG_BTN);
        btnActualizar.addActionListener(this::accActualizar);

        JPanel barra = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 8));
        barra.setBackground(BG_MAIN);
        barra.add(btnActualizar);
        add(barra, BorderLayout.SOUTH);

        /* ---------- Área de gráficos ---------- */
        panelGraficos.setBorder(BorderFactory.createTitledBorder("Resumen gráfico"));
        panelGraficos.setBackground(Color.WHITE);
        add(panelGraficos, BorderLayout.CENTER);

        /* F5 global */
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                     .put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), "REFRESH");
        getRootPane().getActionMap().put("REFRESH",
                new AbstractAction() { public void actionPerformed(ActionEvent e){ accActualizar(null);} });
    }

    /* ---------------- Datos demo ---------------- */
    private void cargarDatos() {
        setVentasTotales("S/. 345.80");
        setPedidosHoy("27");
        setProductoTop("Pollo Broaster");
        pintarGraficoPlaceholder();
    }

    private void accActualizar(ActionEvent e) {
        cargarDatos(); // en real → invocar DashboardController
    }

    /* ---------------- helpers ---------------- */
    private JLabel crearCard(String titulo, String valor) {
        JLabel card = new JLabel(formatoKPI(titulo, valor), SwingConstants.CENTER);
        card.setOpaque(true);
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BD_CARD),
                new EmptyBorder(14, 12, 14, 12)));
        return card;
    }

    private String formatoKPI(String titulo, String valor) {
        return "<html><center><b>"+titulo+"</b><br><span style='font-size:20pt;'>"+
               valor+"</span></center></html>";
    }

    /* ---------- setters públicos ---------- */
    public void setVentasTotales(String v){ lblTotalVentas.setText(formatoKPI("Ventas del día", v)); }
    public void setPedidosHoy   (String v){ lblPedidosHoy .setText(formatoKPI("Pedidos realizados", v)); }
    public void setProductoTop  (String v){ lblProductoTop.setText(formatoKPI("Más vendido", v)); }

    /* ---------- demo gráfico ---------- */
    private void pintarGraficoPlaceholder() {
        panelGraficos.removeAll();
        JLabel ph = new JLabel("Aquí irá tu gráfico (JFreeChart / JFreeCharts)", SwingConstants.CENTER);
        ph.setFont(ph.getFont().deriveFont(Font.ITALIC));
        ph.setBorder(new EmptyBorder(30, 0, 30, 0));
        panelGraficos.add(ph, BorderLayout.CENTER);
        panelGraficos.revalidate();
        panelGraficos.repaint();
    }
}
