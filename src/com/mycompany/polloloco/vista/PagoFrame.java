package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.util.ScreenshotUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Ventana colorida para registrar pagos de pedidos. */
public class PagoFrame extends JFrame {

    /* Icono (puedes cambiar la URL si lo deseas) */
    private static final String ICO_PAGAR =
            "https://cdn-icons-png.flaticon.com/512/945/945927.png";

    /* Paleta */
    private static final Color BG_MAIN  = new Color(0xFFF8E1); // crema
    private static final Color BG_BTN   = new Color(0xFFD54F); // amarillo‑pollo
    private static final Color BG_HOVER = BG_BTN.darker();

    /* Componentes */
    private JTextField txtPedido  = new JTextField(10);
    private JTextField txtMonto   = new JTextField(8);
    private JComboBox<String> cboMetodo =
            new JComboBox<>(new String[]{"Efectivo","Yape/Plin","Tarjeta"});

    public PagoFrame() {
        super("Registrar Pago");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG_MAIN);
        construirUI();
    }

    /* ---------- UI ---------- */
    private void construirUI() {
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.setBorder(new EmptyBorder(20, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 8, 8, 8);
        gbc.anchor  = GridBagConstraints.WEST;

        /* Fila 1: pedido */
        gbc.gridx=0; gbc.gridy=0; centro.add(new JLabel("N.º Pedido:"), gbc);
        gbc.gridx=1; centro.add(txtPedido, gbc);

        /* Fila 2: monto */
        gbc.gridx=0; gbc.gridy=1; centro.add(new JLabel("Monto (S/):"), gbc);
        gbc.gridx=1; centro.add(txtMonto, gbc);

        /* Fila 3: método */
        gbc.gridx=0; gbc.gridy=2; centro.add(new JLabel("Método pago:"), gbc);
        gbc.gridx=1; centro.add(cboMetodo, gbc);

        add(centro, BorderLayout.CENTER);

        /* ---------- Botonera ---------- */
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        pie.setOpaque(false);

        JButton btnRegistrar = crearBoton("Registrar", ICO_PAGAR);
        JButton btnCancelar  = crearBotonPlano("Cancelar",0xE57373);
        JButton btnShot      = crearBotonPlano("Captura", 0x80CBC4);

        btnRegistrar.addActionListener(e ->
                JOptionPane.showMessageDialog(this,"(Demo) Pago registrado ✓"));

        btnCancelar.addActionListener(e -> dispose());

        btnShot.addActionListener(e -> ScreenshotUtil.capturarComponente(this));

        pie.add(btnRegistrar); pie.add(btnCancelar); pie.add(btnShot);
        add(pie, BorderLayout.SOUTH);
    }

    /* ---------- Util ---------- */
    private JButton crearBoton(String txt,String url){
        ImageIcon ico = new ImageIcon(
                new ImageIcon(url).getImage().getScaledInstance(20,20, Image.SCALE_SMOOTH));
        JButton b = new JButton(txt, ico);
        estilizarPlano(b, BG_BTN);
        return b;
    }
    private JButton crearBotonPlano(String txt,int rgb){
        JButton b = new JButton(txt);
        estilizarPlano(b,new Color(rgb));
        return b;
    }
    private void estilizarPlano(JButton b,Color base){
        b.setFocusPainted(false);
        b.setBackground(base);
        b.setForeground(Color.DARK_GRAY);
        b.setBorder(BorderFactory.createEmptyBorder(6,12,6,12));
        b.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override public void mouseEntered(java.awt.event.MouseEvent e){ b.setBackground(base.darker()); }
            @Override public void mouseExited (java.awt.event.MouseEvent e){ b.setBackground(base); }
        });
    }
}
