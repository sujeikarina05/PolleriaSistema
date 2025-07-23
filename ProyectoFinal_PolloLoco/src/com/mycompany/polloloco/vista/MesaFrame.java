package com.mycompany.polloloco.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** Ventana simple para mostrar mesas disponibles. */
public class MesaFrame extends JFrame {

    private final DefaultTableModel modelo = new DefaultTableModel(new Object[]{"Mesa", "Capacidad", "Estado"}, 0);
    private final JTable tabla = new JTable(modelo);

    public MesaFrame() {
        super("Mesas Disponibles");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        construirUI();
        cargarDatosDemo();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    /** Carga datos ficticios de mesas. */
    private void cargarDatosDemo() {
        modelo.setRowCount(0);
        modelo.addRow(new Object[]{"Mesa 1", 4, "Disponible"});
        modelo.addRow(new Object[]{"Mesa 2", 4, "Ocupada"});
        modelo.addRow(new Object[]{"Mesa 3", 6, "Reservada"});
    }
}
