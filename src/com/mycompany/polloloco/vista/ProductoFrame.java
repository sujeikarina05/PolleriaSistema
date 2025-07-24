package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.controlador.ProductoController;
import com.mycompany.polloloco.modelo.Producto;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/** Ventana para administrar productos con estilo Pollería Loco. */
public class ProductoFrame extends JFrame {

    /* ───── ICONOS ───── */
    private static final String ICO_NEW    = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQMOg2TMzxNa0M6DC2q6pQuESQfgu7tOgWFgg&s";
    private static final String ICO_EDIT   = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSZQ5NJ07sTnqP-Pe8TANi-lvKZ6Eec5gITxw&s";
    private static final String ICO_DELETE = "https://cdn-icons-png.flaticon.com/512/3687/3687412.png";

    /* ───── MVC ───── */
    private final ProductoController controller = new ProductoController();

    /* ───── Swing ───── */
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Precio", "Stock"}, 0);
    private final JTable tabla = new JTable(modelo);

    public ProductoFrame() {
        super("Gestión de Productos");
        setSize(640, 430);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        construirUI();
        cargarDatos();
    }

    /* ---------- UI ---------- */
    private void construirUI() {
        Color colFondo  = new Color(0xFFF8E1);   // crema suave
        Color colZebra  = new Color(0xFFF3CE);   // zebra
        Color colHover  = new Color(0xFFE082);   // hover botón

        getContentPane().setBackground(colFondo);
        setLayout(new BorderLayout(0,10));

        /* Tabla con zebra */
        tabla.setRowHeight(24);
        tabla.setAutoCreateRowSorter(true);
        DefaultTableCellRenderer zebra = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t,Object v,boolean sel,boolean foc,int r,int c){
                super.getTableCellRendererComponent(t,v,sel,foc,r,c);
                setBackground(sel?colHover:(r%2==0?Color.WHITE:colZebra));
                return this;
            }
        };
        for(int i=0;i<tabla.getColumnCount();i++)
            tabla.getColumnModel().getColumn(i).setCellRenderer(zebra);

        JScrollPane sp = new JScrollPane(tabla);
        sp.setBorder(BorderFactory.createLineBorder(new Color(0xFFC107),2));
        add(sp, BorderLayout.CENTER);

        /* Botonera */
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        botones.setOpaque(false);
        botones.setBorder(new EmptyBorder(0,0,10,10));

        JButton btnNuevo   = crearBoton("Nuevo",   ICO_NEW);
        JButton btnEditar  = crearBoton("Editar",  ICO_EDIT);
        JButton btnEliminar= crearBoton("Eliminar",ICO_DELETE);

        botones.add(btnNuevo);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        add(botones, BorderLayout.SOUTH);

        /* Acciones demo */
        btnNuevo.addActionListener(e ->
                JOptionPane.showMessageDialog(this,"Funcionalidad en desarrollo"));
        btnEditar.addActionListener(e ->
                JOptionPane.showMessageDialog(this,"Funcionalidad en desarrollo"));
        btnEliminar.addActionListener(e ->
                JOptionPane.showMessageDialog(this,"Funcionalidad en desarrollo"));
    }

    /* ---------- Datos ---------- */
    private void cargarDatos() {
        modelo.setRowCount(0);
        List<Producto> lista = controller.listarTodos();
        for (Producto p : lista) {
            modelo.addRow(new Object[]{
                    p.getId(), p.getNombre(),
                    String.format("S/. %.2f", p.getPrecio()),
                    p.getStock()
            });
        }
    }

    /* ---------- Util ---------- */
    private JButton crearBoton(String texto,String url){
        ImageIcon ico = new ImageIcon(new ImageIcon(url)
                .getImage().getScaledInstance(22,22,Image.SCALE_SMOOTH));
        JButton b = new JButton(texto, ico);
        b.setFocusPainted(false);
        b.setBackground(new Color(0xFFE082));
        b.setForeground(Color.DARK_GRAY);
        b.setBorder(BorderFactory.createEmptyBorder(6,12,6,12));
        b.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override public void mouseEntered(java.awt.event.MouseEvent e){ b.setBackground(new Color(0xFFD54F)); }
            @Override public void mouseExited (java.awt.event.MouseEvent e){ b.setBackground(new Color(0xFFE082)); }
        });
        return b;
    }
}
