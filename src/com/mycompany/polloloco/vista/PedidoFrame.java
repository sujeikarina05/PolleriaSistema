package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.controlador.PedidoController;
import com.mycompany.polloloco.dao.MesaDAO;
import com.mycompany.polloloco.dao.ProductoDAO;
import com.mycompany.polloloco.modelo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Ventana para que el mozo registre un nuevo pedido. */
public class PedidoFrame extends JFrame {

    private final MesaDAO        mesaDAO   = new MesaDAO();
    private final ProductoDAO    prodDAO   = new ProductoDAO();
    private final PedidoController pedidoCtrl = new PedidoController();

    private JComboBox<Mesa>     cboMesa;
    private JComboBox<Producto> cboProducto;
    private JSpinner            spCant;
    private JTable              tabla;
    private JLabel              lblTotal;

    private final DefaultTableModel modelo =
            new DefaultTableModel(new Object[]{"Producto", "Cant.", "P. uni.", "Subtotal"}, 0) {
                /* Hace que la tabla no sea editable */
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

    public PedidoFrame() {
        super("Registrar nuevo pedido");
        setSize(720, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        construirUI();
    }

    /* ---------- UI ---------- */
    private void construirUI() {
        /* --- Selección de mesa --- */
        JPanel sup = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        sup.add(new JLabel("Mesa:"));
        List<Mesa> mesasLibres = mesaDAO.listarDisponibles();
        if (mesasLibres.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay mesas libres en este momento.",
                    "Sin mesas", JOptionPane.WARNING_MESSAGE);
            dispose();
            return;
        }
        cboMesa = new JComboBox<>(mesasLibres.toArray(new Mesa[0]));
        sup.add(cboMesa);
        add(sup, BorderLayout.NORTH);

        /* --- Productos y detalle --- */
        JPanel centro = new JPanel(new BorderLayout(5, 5));
        JPanel barra  = new JPanel();

        List<Producto> productos = prodDAO.listar();
        cboProducto = new JComboBox<>(productos.toArray(new Producto[0]));
        if (cboProducto.getItemCount() > 0) cboProducto.setSelectedIndex(0);

        spCant = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        JButton btnAdd = new JButton("Añadir");
        btnAdd.addActionListener(e -> agregarLinea());

        barra.add(new JLabel("Producto:"));
        barra.add(cboProducto);
        barra.add(new JLabel("Cant.:"));
        barra.add(spCant);
        barra.add(btnAdd);

        tabla = new JTable(modelo);
        centro.add(barra, BorderLayout.NORTH);
        centro.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        /* --- Total y botones --- */
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        lblTotal = new JLabel("Total: S/. 0.00");
        JButton btnGuardar  = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.addActionListener(e -> guardarPedido());
        btnCancelar.addActionListener(e -> dispose());
        pie.add(lblTotal);
        pie.add(btnGuardar);
        pie.add(btnCancelar);
        add(pie, BorderLayout.SOUTH);
    }

    /* ---------- Lógica ---------- */
    private void agregarLinea() {
        Producto p = (Producto) cboProducto.getSelectedItem();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto válido.");
            return;
        }
        int cant = (Integer) spCant.getValue();
        double sub = cant * p.getPrecio();
        modelo.addRow(new Object[]{p, cant, p.getPrecio(), sub});
        actualizarTotal();
    }

    private void actualizarTotal() {
        double tot = 0;
        for (int i = 0; i < modelo.getRowCount(); i++)
            tot += (double) modelo.getValueAt(i, 3);
        lblTotal.setText(String.format("Total: S/. %.2f", tot));
    }

    private void guardarPedido() {
        if (modelo.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Añade al menos un producto.");
            return;
        }
        Mesa mesa = (Mesa) cboMesa.getSelectedItem();
        List<DetallePedido> detalle = new ArrayList<>();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            Producto pr = (Producto) modelo.getValueAt(i, 0);
            int cant    = (int) modelo.getValueAt(i, 1);
            detalle.add(new DetallePedido(pr, cant));
        }
        Pedido p = new Pedido();
        p.setIdMesa(mesa.getId());
        p.setIdUsuario(Sesion.getUsuarioActual().getId());
        p.setFechaPedido(LocalDateTime.now());
        p.setDetalle(detalle);
        p.calcularTotal();  // asumiendo que en el modelo recalcula el total

        if (pedidoCtrl.registrarPedido(p)) {
            new MesaDAO().cambiarEstado(mesa.getId(), "Ocupada");
            JOptionPane.showMessageDialog(this, "Pedido registrado.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el pedido.");
        }
    }
}