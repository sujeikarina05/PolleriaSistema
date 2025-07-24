package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.modelo.Usuario;
import com.mycompany.polloloco.util.Sesion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.image.BufferedImage;

/** Panel principal del Cajero – Pollería Pollo Loco (versión con estilo + iconos). */
public class CajeroFrame extends JFrame {

    /* === Paleta corporativa === */
    private static final Color ROJO        = new Color(194, 24, 7);
    private static final Color ROJO_HOVER  = new Color(157, 19, 4);
    private static final Color AMARILLO    = new Color(255, 221, 87);
    private static final Color BLANCO      = Color.WHITE;

    /* === Iconos para acciones === */
    private static final String ICO_PAGO   = "https://cdn-icons-png.flaticon.com/512/1251/1251827.png";
    private static final String ICO_COMP   = "https://cdn-icons-png.flaticon.com/512/1041/1041890.png";
    private static final String ICO_EXCEL  = "https://cdn-icons-png.flaticon.com/512/732/732220.png";
    private static final String ICO_SALIR  = "https://thumbs.dreamstime.com/b/bot%C3%B3n-redondo-azul-ci%C3%A1nico-vidrioso-de-la-salida-del-sistema-97912713.jpg";

    /* === Ilustración de cabecera === */
    private static final String ICO_CAJERO = "https://w7.pngwing.com/pngs/862/33/png-transparent-coffee-shop-cashier-illustration.png";

    private JLabel  lblBienvenida;
    private JButton btnRegistrarPago;
    private JButton btnEmitirComprobante;
    private JButton btnExportarExcel;
    private JButton btnCerrarSesion;

    public CajeroFrame() {
        super("Panel de Cajero – Pollería Pollo Loco");
        setSize(560, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        construirUI();
        actualizarBienvenida();
    }

    /* --------------------- UI --------------------- */
    private void construirUI() {

        /* ---- CABECERA con gradiente y logo cajero ---- */
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, ROJO, getWidth(), 0, AMARILLO));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(getWidth(), 60));
        header.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel logo = new JLabel(cargarIcono(ICO_CAJERO, 40, 40));
        header.add(logo, BorderLayout.WEST);

        lblBienvenida = new JLabel("", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBienvenida.setForeground(BLANCO);
        header.add(lblBienvenida, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        /* ---- BOTONERA central ---- */
        JPanel centro = new JPanel(new GridLayout(4, 1, 0, 16));
        centro.setBorder(BorderFactory.createEmptyBorder(40, 90, 40, 90));
        centro.setBackground(BLANCO);

        btnRegistrarPago     = crearBoton("Registrar pago",          ICO_PAGO);
        btnEmitirComprobante = crearBoton("Emitir comprobante",      ICO_COMP);
        btnExportarExcel     = crearBoton("Exportar ventas a Excel", ICO_EXCEL);
        btnCerrarSesion      = crearBoton("Cerrar sesión",           ICO_SALIR);

        btnRegistrarPago    .addActionListener(this::registrarPago);
        btnEmitirComprobante.addActionListener(this::emitirComprobante);
        btnExportarExcel    .addActionListener(this::exportarExcel);
        btnCerrarSesion     .addActionListener(this::cerrarSesion);

        centro.add(btnRegistrarPago);
        centro.add(btnEmitirComprobante);
        centro.add(btnExportarExcel);
        centro.add(btnCerrarSesion);

        add(centro, BorderLayout.CENTER);
    }

    /* ------------------ HELPERS ------------------ */

    /** Crea botón con estilo plano, icono remoto y efecto hover. */
    private JButton crearBoton(String texto, String urlIcono) {
        JButton b = new JButton(texto, cargarIcono(urlIcono, 28, 28));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setIconTextGap(16);
        b.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        b.setForeground(BLANCO);
        b.setBackground(ROJO);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(ROJO_HOVER); }
            @Override public void mouseExited (MouseEvent e) { b.setBackground(ROJO);      }
        });
        return b;
    }

    /** Carga un icono desde URL, devolviendo un placeholder si falla. */
    private ImageIcon cargarIcono(String url, int w, int h) {
        try {
            Image img = new ImageIcon(new URL(url)).getImage()
                                                    .getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (MalformedURLException e) {
            // Placeholder transparente si falla
            return new ImageIcon(new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB));
        }
    }

    private void actualizarBienvenida() {
        Usuario u = Sesion.getUsuarioActual();
        lblBienvenida.setText("Bienvenido, " + ((u != null) ? u.getNombre() : "Cajero"));
    }

    /* ------------------ ACCIONES ------------------ */

    private void registrarPago(ActionEvent e)       { new PagoFrame().setVisible(true); }
    private void emitirComprobante(ActionEvent e)   { new ComprobanteFrame().setVisible(true); }
    private void exportarExcel(ActionEvent e)       {
        JOptionPane.showMessageDialog(this,
            "Exportación de ventas a Excel pendiente.",
            "En desarrollo", JOptionPane.INFORMATION_MESSAGE);
    }
    private void cerrarSesion(ActionEvent e)        {
        if (JOptionPane.showConfirmDialog(this,"¿Cerrar sesión?","Confirmar",
                JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
            Sesion.cerrarSesion();
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}