package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.util.ReporteExcelExporter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Ventana para mostrar reportes de pedidos y exportarlos a Excel. */
public class ReporteFrame extends JFrame {

    private final JTable tabla;
    private final DefaultTableModel modelo;
    private final JButton btnExportar  = new JButton("Exportar a Excel");
    private final JButton btnActualizar = new JButton("Actualizar (F5)");
    private final JComboBox<String> comboFiltro =
            new JComboBox<>(new String[]{"Hoy", "Esta semana", "Este mes"});

    public ReporteFrame() {
        super("Reportes - Pollería Pollo Loco");
        setSize(740, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        /* ---------- Filtros ---------- */
        JPanel barraFiltros = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        barraFiltros.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        gbc.gridx = 0; barraFiltros.add(new JLabel("Filtrar por:"), gbc);
        gbc.gridx = 1; barraFiltros.add(comboFiltro, gbc);
        gbc.gridx = 2; barraFiltros.add(btnActualizar, gbc);

        add(barraFiltros, BorderLayout.NORTH);

        /* ---------- Tabla ---------- */
        modelo = new DefaultTableModel(new Object[]{"Fecha", "Pedido", "Total", "Usuario"}, 0);
        tabla  = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        /* ---------- Pie ---------- */
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pie.add(btnExportar);
        add(pie, BorderLayout.SOUTH);

        /* ---------- Listeners ---------- */
        btnActualizar.addActionListener(e -> cargarDatos());
        btnExportar.addActionListener(e -> exportarExcel());

        /* Atajo global F5 */
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                     .put(KeyStroke.getKeyStroke("F5"), "REFRESH");
        getRootPane().getActionMap()
                     .put("REFRESH", new AbstractAction() {
                         public void actionPerformed(java.awt.event.ActionEvent e) { cargarDatos(); }
                     });

        cargarDatos(); // primera carga
    }

    /** Simula carga de datos. Sustituir por PedidoDAO + filtro combo. */
    private void cargarDatos() {
        modelo.setRowCount(0);
        modelo.addRow(new Object[]{"2025‑07‑23", "#001", 48.00, "cajero1"});
        modelo.addRow(new Object[]{"2025‑07‑23", "#002", 32.50, "cajero2"});
        modelo.addRow(new Object[]{"2025‑07‑23", "#003", 78.90, "cajero1"});
    }

    /** Exporta los datos visibles a un archivo Excel. */
    private void exportarExcel() {
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "⚠ No hay datos para exportar.");
            return;
        }

        List<String> encabezados = List.of("Fecha", "Pedido", "Total", "Usuario");
        List<List<Object>> filas = obtenerFilasTabla();

        JFileChooser ch = new JFileChooser();
        ch.setDialogTitle("Guardar reporte");
        if (ch.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = ch.getSelectedFile().getAbsolutePath();
            if (!path.toLowerCase().endsWith(".xlsx")) path += ".xlsx";

            new ReporteExcelExporter().exportar(path, encabezados, filas);
            JOptionPane.showMessageDialog(this,
                    "Reporte exportado con " + filas.size() + " filas.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /** Devuelve filas de la tabla como lista de listas. */
    private List<List<Object>> obtenerFilasTabla() {
        List<List<Object>> filas = new ArrayList<>();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            List<Object> fila = new ArrayList<>();
            for (int j = 0; j < modelo.getColumnCount(); j++) {
                fila.add(modelo.getValueAt(i, j));
            }
            filas.add(fila);
        }
        return filas;
    }
}
