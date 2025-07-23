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

public class PedidoFrame extends JFrame {

    private final MesaDAO mesaDAO = new MesaDAO();
    private final ProductoDAO prodDAO = new ProductoDAO();
    private final PedidoController pedidoCtrl = new PedidoController();

    private JComboBox<Mesa> cboMesa;
    private JComboBox<Producto> cboProducto;
    private JSpinner spCant;
    private JTable tabla;
    private JLabel lblTotal;
    private final DefaultTableModel modelo =
            new DefaultTableModel(new Object[]{"Producto","Cant.","P. uni.","Subtotal"},0);

    public PedidoFrame() {
        super("Registrar nuevo pedido");
        setSize(720, 480);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        construirUI();
    }

    private void construirUI() {
        /* -------- Mesa -------- */
        JPanel sup = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10));
        sup.add(new JLabel("Mesa:"));
        cboMesa = new JComboBox<>(mesaDAO.listarDisponibles().toArray(new Mesa[0]));
        sup.add(cboMesa);
        add(sup, BorderLayout.NORTH);

        /* -------- Productos y detalle -------- */
        JPanel centro = new JPanel(new BorderLayout(5,5));
        JPanel barra  = new JPanel();

        cboProducto = new JComboBox<>(prodDAO.listar().toArray(new Producto[0]));
        spCant      = new JSpinner(new SpinnerNumberModel(1,1,99,1));
        JButton btnAdd = new JButton("Añadir");
        btnAdd.addActionListener(e -> agregar());

        barra.add(new JLabel("Producto:"));
        barra.add(cboProducto);
        barra.add(new JLabel("Cant.:"));
        barra.add(spCant);
        barra.add(btnAdd);

        tabla = new JTable(modelo);
        centro.add(barra, BorderLayout.NORTH);
        centro.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(centro, BorderLayout.CENTER);

        /* -------- Total y botones -------- */
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        lblTotal = new JLabel("Total: S/. 0.00");
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");
        btnGuardar.addActionListener(e -> guardar());
        btnCancelar.addActionListener(e -> dispose());
        pie.add(lblTotal); pie.add(btnGuardar); pie.add(btnCancelar);
        add(pie, BorderLayout.SOUTH);
    }

    private void agregar() {
        Producto p = (Producto) cboProducto.getSelectedItem();
        int cant  = (Integer) spCant.getValue();
        double sub = cant * p.getPrecio();
        modelo.addRow(new Object[]{p, cant, p.getPrecio(), sub});
        actualizarTotal();
    }

    private void actualizarTotal() {
        double tot = 0;
        for (int i=0;i<modelo.getRowCount();i++)
            tot += (double) modelo.getValueAt(i,3);
        lblTotal.setText(String.format("Total: S/. %.2f", tot));
    }

    private void guardar() {
        if (modelo.getRowCount()==0) {
            JOptionPane.showMessageDialog(this,"Añade al menos un producto."); return;
        }
        Mesa mesa = (Mesa) cboMesa.getSelectedItem();
        List<DetallePedido> detalle = new ArrayList<>();
        for (int i=0;i<modelo.getRowCount();i++) {
            Producto pr = (Producto) modelo.getValueAt(i,0);
            int cant    = (int) modelo.getValueAt(i,1);
            detalle.add(new DetallePedido(pr,cant));
        }
        Pedido p = new Pedido();
        p.setIdMesa(mesa.getId());                 // ajusta si tu getter se llama distinto
        p.setFechaPedido(LocalDateTime.now());
        p.setDetalle(detalle);

        if (pedidoCtrl.registrarPedido(p)) {
            new MesaDAO().cambiarEstado(mesa.getId(),"Ocupada");
            JOptionPane.showMessageDialog(this,"Pedido registrado.");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,"No se pudo registrar el pedido.");
        }
    }
}
