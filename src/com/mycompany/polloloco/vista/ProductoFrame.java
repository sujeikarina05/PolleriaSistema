package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.controlador.ProductoController;
import com.mycompany.polloloco.modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/** Ventana básica para administrar productos. */
public class ProductoFrame extends JFrame {

    private final ProductoController controller = new ProductoController();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Precio", "Stock"}, 0);
    private final JTable tabla = new JTable(modelo);

    public ProductoFrame() {
        super("Gestión de Productos");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        construirUI();
        cargarDatos();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton btnNuevo = new JButton("Nuevo");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        botones.add(btnNuevo);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        add(botones, BorderLayout.SOUTH);

        btnNuevo.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Funcionalidad en desarrollo"));
        btnEditar.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Funcionalidad en desarrollo"));
        btnEliminar.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Funcionalidad en desarrollo"));
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        List<Producto> lista = controller.listarProductos();
        for (Producto p : lista) {
            modelo.addRow(new Object[]{p.getId(), p.getNombre(), p.getPrecio(), p.getStock()});
        }
    }
}
