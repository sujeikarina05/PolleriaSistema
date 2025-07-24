package com.mycompany.polloloco.vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** Ventana simple para mostrar mesas disponibles. */
public class MesaFrame extends JFrame {

    /* ---------- Colores e icono ---------- */
    private static final Color BG_MAIN  = new Color(0xFFF8E1);
    private static final Color BG_HEAD  = new Color(0xFFD54F);
    private static final Color FG_HEAD  = Color.DARK_GRAY;
    private static final String ICO_MESA =
            "https://cdn-icons-png.flaticon.com/512/3595/3595452.png";

    private final DefaultTableModel modelo =
            new DefaultTableModel(new Object[]{"Mesa","Capacidad","Estado"}, 0);
    private final JTable tabla = new JTable(modelo);

    public MesaFrame() {
        super("Mesas disponibles");
        setSize(460, 320);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG_MAIN);
        construirUI();
        cargarDatosDemo();
    }

    /* ---------- UI ---------- */
    private void construirUI() {
        setLayout(new BorderLayout());

        /* Cabecera con icono */
        JLabel titulo = new JLabel("Listado de mesas", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(FG_HEAD);
        titulo.setIcon(new ImageIcon(
                new ImageIcon(ICO_MESA).getImage().getScaledInstance(26,26,Image.SCALE_SMOOTH)));
        titulo.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        titulo.setOpaque(true);
        titulo.setBackground(BG_HEAD);
        add(titulo, BorderLayout.NORTH);

        /* Tabla */
        tabla.setRowHeight(24);
        JScrollPane sp = new JScrollPane(tabla);
        sp.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
        add(sp, BorderLayout.CENTER);
    }

    /** Carga datos ficticios de mesas. */
    private void cargarDatosDemo() {
        modelo.setRowCount(0);
        modelo.addRow(new Object[]{"M‑01", 4,  "Libre"});
        modelo.addRow(new Object[]{"M‑02", 4,  "Ocupada"});
        modelo.addRow(new Object[]{"M‑03", 6,  "Reservada"});
        modelo.addRow(new Object[]{"M‑04", 2,  "Libre"});
        modelo.addRow(new Object[]{"M‑05", 4,  "Ocupada"});
    }
}
