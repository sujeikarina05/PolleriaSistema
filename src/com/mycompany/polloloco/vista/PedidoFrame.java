package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.controlador.PedidoController;
import com.mycompany.polloloco.dao.MesaDAO;
import com.mycompany.polloloco.dao.ProductoDAO;
import com.mycompany.polloloco.modelo.DetallePedido;
import com.mycompany.polloloco.modelo.Mesa;
import com.mycompany.polloloco.modelo.Mesa.Estado;
import com.mycompany.polloloco.modelo.Pedido;
import com.mycompany.polloloco.modelo.Producto;
import com.mycompany.polloloco.util.ScreenshotUtil;
import com.mycompany.polloloco.util.Sesion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Ventana para que el mozo registre un nuevo pedido, con estilo Pollería Loco. */
public class PedidoFrame extends JFrame {

    /* ───── ICONOS ───── */
    private static final String ICO_ADD =
            "https://cdn-icons-png.flaticon.com/512/992/992651.png";

    /* ───── DAOs / Controller ───── */
    private final MesaDAO          mesaDAO   = new MesaDAO();
    private final ProductoDAO      prodDAO   = new ProductoDAO();
    private final PedidoController pedidoCtl = new PedidoController();

    /* ───── Swing ───── */
    private JComboBox<Mesa>     cboMesa;
    private JComboBox<Producto> cboProducto;
    private JSpinner            spCant;
    private JTable              tabla;
    private JLabel              lblTotal;

    private final DefaultTableModel modelo =
            new DefaultTableModel(new Object[]{"Producto","Cant.","P. uni.","Subtotal"},0){
                @Override public boolean isCellEditable(int r,int c){ return false; }
            };

    public PedidoFrame() {
        super("Registrar nuevo pedido");
        setSize(760, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(0,10));
        construirUI();
    }

    /* ---------- UI ---------- */
    private void construirUI(){

        Color colFondo = new Color(0xFFF8E1);
        Color colZebra = new Color(0xFFF3CE);
        Color colHover = new Color(0xFFE082);
        getContentPane().setBackground(colFondo);

        /* ---- MESA ---- */
        JPanel sup = new JPanel(new FlowLayout(FlowLayout.LEFT,12,12));
        sup.setOpaque(false);
        sup.add(new JLabel("Mesa:"));

        List<Mesa> mesasLibres = mesaDAO.listarPorEstado(Estado.LIBRE);
        if(mesasLibres.isEmpty()){
            JOptionPane.showMessageDialog(this,
                    "No hay mesas libres en este momento.","Info",
                    JOptionPane.WARNING_MESSAGE);
            dispose(); return;
        }
        cboMesa = new JComboBox<>(mesasLibres.toArray(new Mesa[0]));
        sup.add(cboMesa);
        add(sup,BorderLayout.NORTH);

        /* ---- PRODUCTOS / DETALLE ---- */
        JPanel centro = new JPanel(new BorderLayout(5,5));
        centro.setOpaque(false);
        JPanel barra  = new JPanel();
        barra.setOpaque(false);
        barra.setBorder(new EmptyBorder(0,10,0,10));

        List<Producto> productos = prodDAO.listar();
        cboProducto = new JComboBox<>(productos.toArray(new Producto[0]));
        spCant      = new JSpinner(new SpinnerNumberModel(1,1,99,1));
        JButton btnAdd = crearBotonIco("Añadir", ICO_ADD);

        btnAdd.addActionListener(e -> agregarLinea());

        barra.add(new JLabel("Producto:")); barra.add(cboProducto);
        barra.add(new JLabel("Cant.:"));    barra.add(spCant);
        barra.add(btnAdd);

        tabla = new JTable(modelo);
        tabla.setRowHeight(24);
        /* zebra */
        DefaultTableCellRenderer zebra = new DefaultTableCellRenderer(){
            @Override public Component getTableCellRendererComponent(
                    JTable t,Object v,boolean sel,boolean foc,int r,int c){
                super.getTableCellRendererComponent(t,v,sel,foc,r,c);
                setBackground(sel?colHover:(r%2==0?Color.WHITE:colZebra));
                return this;
            }
        };
        for(int i=0;i<tabla.getColumnCount();i++)
            tabla.getColumnModel().getColumn(i).setCellRenderer(zebra);

        centro.add(barra,BorderLayout.NORTH);
        centro.add(new JScrollPane(tabla),BorderLayout.CENTER);
        add(centro,BorderLayout.CENTER);

        /* ---- TOTAL / BOTONES ---- */
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        pie.setOpaque(false);

        lblTotal = new JLabel("Total: S/. 0.00");
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD));

        JButton btnGuardar  = crearBotonPlano("Guardar", 0xFFD54F);
        JButton btnCancelar = crearBotonPlano("Cancelar",0xE57373);
        JButton btnCapturar = crearBotonPlano("Capturar",0x80CBC4);

        btnGuardar.addActionListener(e -> guardarPedido());
        btnCancelar.addActionListener(e -> dispose());
        btnCapturar.addActionListener(e -> ScreenshotUtil.capturarComponente(this));

        pie.add(lblTotal); pie.add(btnGuardar); pie.add(btnCancelar); pie.add(btnCapturar);
        add(pie,BorderLayout.SOUTH);
    }

    /* ---------- Lógica ---------- */
    private void agregarLinea(){
        Producto p = (Producto) cboProducto.getSelectedItem();
        if(p==null){ JOptionPane.showMessageDialog(this,"Selecciona un producto válido."); return; }
        int cant = (Integer) spCant.getValue();
        double sub = cant*p.getPrecio();
        modelo.addRow(new Object[]{p,cant,p.getPrecio(),sub});
        actualizarTotal();
    }

    private void actualizarTotal(){
        double tot=0;
        for(int i=0;i<modelo.getRowCount();i++)
            tot += (double) modelo.getValueAt(i,3);
        lblTotal.setText(String.format("Total: S/. %.2f",tot));
    }

    private void guardarPedido(){
        if(modelo.getRowCount()==0){
            JOptionPane.showMessageDialog(this,"Añade al menos un producto."); return;
        }
        Mesa mesa = (Mesa) cboMesa.getSelectedItem();

        /* ---- Detalle ---- */
        List<DetallePedido> detalle=new ArrayList<>();
        double total=0;
        for(int i=0;i<modelo.getRowCount();i++){
            Producto pr =(Producto) modelo.getValueAt(i,0);
            int cant   =(int)      modelo.getValueAt(i,1);
            double pu  =(double)   modelo.getValueAt(i,2);
            detalle.add(new DetallePedido(pr,cant,pu));
            total += cant*pu;
        }

        /* ---- Cabecera ---- */
        Pedido p = new Pedido();
        p.setIdMesa   (mesa.getId());
        p.setIdUsuario(Sesion.getUsuarioActual().getId());
        p.setFechaPedido(LocalDateTime.now());
        for (DetallePedido d : detalle) p.addDetalle(d);
        p.setTotal(total);

        if(pedidoCtl.registrarPedido(p)){
            mesaDAO.cambiarEstado(mesa.getId(), Estado.OCUPADA);
            JOptionPane.showMessageDialog(this,"Pedido registrado 🤙");
            dispose();
        }else{
            JOptionPane.showMessageDialog(this,"No se pudo registrar el pedido.");
        }
    }

    /* ---------- Util ---------- */
    private JButton crearBotonIco(String txt,String url){
        ImageIcon ico = new ImageIcon(new ImageIcon(url)
                .getImage().getScaledInstance(20,20,Image.SCALE_SMOOTH));
        JButton b = new JButton(txt,ico);
        estilizarPlano(b,0xFFD54F);
        return b;
    }
    private JButton crearBotonPlano(String txt,int rgb){
        JButton b = new JButton(txt);
        estilizarPlano(b,rgb);
        return b;
    }
    private void estilizarPlano(JButton b,int rgb){
        b.setFocusPainted(false);
        b.setBackground(new Color(rgb));
        b.setForeground(Color.DARK_GRAY);
        b.setBorder(BorderFactory.createEmptyBorder(6,12,6,12));
        b.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override public void mouseEntered(java.awt.event.MouseEvent e){ b.setBackground(b.getBackground().darker()); }
            @Override public void mouseExited (java.awt.event.MouseEvent e){ b.setBackground(new Color(rgb)); }
        });
    }
}
