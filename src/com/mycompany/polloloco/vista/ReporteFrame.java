package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.util.ReporteExcelExporter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ReporteFrame extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private JButton btnExportar;
    private JButton btnActualizar;
    private JComboBox<String> comboFiltro;

    public ReporteFrame() {
        setTitle("Reportes - Pollería Pollo Loco");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponentes();
    }

    private void initComponentes() {
        // Norte - Filtros
        JPanel panelNorte = new JPanel();
        comboFiltro = new JComboBox<>(new String[]{"Hoy", "Esta semana", "Este mes"});
        btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarDatos());
        panelNorte.add(new JLabel("Filtrar por:"));
        panelNorte.add(comboFiltro);
        panelNorte.add(btnActualizar);
        add(panelNorte, BorderLayout.NORTH);

        // Centro - Tabla
        modelo = new DefaultTableModel(new Object[]{"Fecha", "Pedido", "Total", "Usuario"}, 0);
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // Sur - Exportar
        JPanel panelSur = new JPanel();
        btnExportar = new JButton("Exportar a Excel");
        btnExportar.addActionListener(e -> exportarExcel());
        panelSur.add(btnExportar);
        add(panelSur, BorderLayout.SOUTH);
    }

    // Simulación de carga de datos (reemplazar con DAO real)
    private void cargarDatos() {
        modelo.setRowCount(0); // limpiar tabla
        modelo.addRow(new Object[]{"2025-07-23", "#001", 48.00, "cajero1"});
        modelo.addRow(new Object[]{"2025-07-23", "#002", 32.50, "cajero2"});
        modelo.addRow(new Object[]{"2025-07-23", "#003", 78.90, "cajero1"});
        // Aquí puedes cargar desde PedidoDAO con filtro de fecha
    }

    private void exportarExcel() {
        List<String> encabezados = List.of("Fecha", "Pedido", "Total", "Usuario");
        List<List<Object>> filas = new java.util.ArrayList<>();

        for (int i = 0; i < modelo.getRowCount(); i++) {
            List<Object> fila = new java.util.ArrayList<>();
            for (int j = 0; j < modelo.getColumnCount(); j++) {
                fila.add(modelo.getValueAt(i, j));
            }
            filas.add(fila);
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Guardar reporte");
        int seleccion = chooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            if (!path.toLowerCase().endsWith(".xlsx")) {
                path += ".xlsx";
            }

            ReporteExcelExporter exporter = new ReporteExcelExporter();
            exporter.exportar(path, encabezados, filas);
        }
    }
}
