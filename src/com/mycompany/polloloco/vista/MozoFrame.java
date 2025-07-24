package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.modelo.Usuario;
import com.mycompany.polloloco.util.Sesion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/** Panel principal para mozos: creación de pedidos y gestión de mesas. */
public class MozoFrame extends JFrame {

    /* ---------- Paleta e iconos ---------- */
    private static final Color BG_MAIN  = new Color(0xFFF8E1);   // crema suave
    private static final Color BG_BTN   = new Color(0xFFD54F);   // amarillo‑pollo
    private static final Color BG_HOVER = BG_BTN.darker();

    private static final String ICO_PEDIDO =
            "https://cdn-icons-png.flaticon.com/512/3075/3075977.png";
    private static final String ICO_MESAS =
            "https://cdn-icons-png.flaticon.com/512/3075/3075974.png";
    private static final String ICO_SALIR =
            "https://thumbs.dreamstime.com/b/bot%C3%B3n-redondo-azul-ci%C3%A1nico-vidrioso-de-la-salida-del-sistema-97912713.jpg";

    /* ---------- Componentes ---------- */
    private final JLabel  lblBienvenida = new JLabel("", SwingConstants.CENTER);
    private final JButton btnNuevoPedido =
            crearBoton("Registrar nuevo pedido", ICO_PEDIDO, KeyEvent.VK_N, "Ctrl+N – Abrir registro");
    private final JButton btnVerMesas     =
            crearBoton("Ver mesas disponibles", ICO_MESAS, KeyEvent.VK_M, "Ctrl+M – Estado mesas");
    private final JButton btnCerrarSesion =
            crearBoton("Cerrar sesión", ICO_SALIR, KeyEvent.VK_Q, "Ctrl+Q – Salir");

    public MozoFrame() {
        super("Panel de Mozo – Pollería Pollo Loco");
        setSize(540, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().setBackground(BG_MAIN);
        setLayout(new BorderLayout());

        construirUI();
        actualizarBienvenida();
        registrarAccesosDirectos();
    }

    /* ---------- UI ---------- */
    private void construirUI() {
        /* Cabecera */
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(14, 0, 18, 0));
        add(lblBienvenida, BorderLayout.NORTH);

        /* Botonera central */
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(14, 0, 14, 0);
        gbc.weightx = 1;

        btnNuevoPedido.addActionListener(this::abrirPedido);
        btnVerMesas.addActionListener(this::verMesas);
        btnCerrarSesion.addActionListener(this::cerrarSesion);

        gbc.gridy = 0; centro.add(btnNuevoPedido,  gbc);
        gbc.gridy = 1; centro.add(btnVerMesas,     gbc);
        gbc.gridy = 2; centro.add(btnCerrarSesion, gbc);

        add(centro, BorderLayout.CENTER);

        /* Foco inicial */
        SwingUtilities.invokeLater(btnNuevoPedido::requestFocusInWindow);
    }

    private JButton crearBoton(String texto,String url,int mnemonic,String tooltip){
        ImageIcon ico = new ImageIcon(
                new ImageIcon(url).getImage().getScaledInstance(24,24, Image.SCALE_SMOOTH));
        JButton b = new JButton(texto, ico);
        b.setMnemonic(mnemonic);
        b.setToolTipText(tooltip);
        b.setFocusPainted(false);
        b.setBackground(BG_BTN);
        b.setForeground(Color.DARK_GRAY);
        b.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));
        b.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override public void mouseEntered(java.awt.event.MouseEvent e){ b.setBackground(BG_HOVER);}
            @Override public void mouseExited (java.awt.event.MouseEvent e){ b.setBackground(BG_BTN);}
        });
        return b;
    }

    /* ---------- Bienvenida ---------- */
    private void actualizarBienvenida() {
        Usuario u = Sesion.getUsuarioActual();
        lblBienvenida.setText("¡Bienvenido, " + (u != null ? u.getNombre() : "Mozo") + "!");
    }

    /* ---------- Acciones ---------- */
    private void abrirPedido(ActionEvent e){ new PedidoFrame().setVisible(true); }

    private void verMesas(ActionEvent e){
        JOptionPane.showMessageDialog(this,
                "Módulo Mesas aún no implementado.",
                "Info", JOptionPane.INFORMATION_MESSAGE);
        // new MesaFrame().setVisible(true);
    }

    private void cerrarSesion(ActionEvent e){
        if (JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas cerrar sesión?","Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
            Sesion.cerrarSesion();
            dispose();
            new LoginFrame().setVisible(true);
        }
    }

    /* ---------- Shortcuts globales ---------- */
    private void registrarAccesosDirectos(){
        JRootPane root = getRootPane();
        InputMap im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = root.getActionMap();

        im.put(KeyStroke.getKeyStroke("ctrl N"), "NUEVO");
        im.put(KeyStroke.getKeyStroke("ctrl M"), "MESAS");
        im.put(KeyStroke.getKeyStroke("ctrl Q"), "SALIR");

        am.put("NUEVO", new AbstractAction(){ public void actionPerformed(ActionEvent e){ abrirPedido(e);} });
        am.put("MESAS",new AbstractAction(){ public void actionPerformed(ActionEvent e){ verMesas(e);} });
        am.put("SALIR", new AbstractAction(){ public void actionPerformed(ActionEvent e){ cerrarSesion(e);} });
    }
}
