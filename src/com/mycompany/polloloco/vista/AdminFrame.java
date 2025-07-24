package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.modelo.Usuario;
import com.mycompany.polloloco.util.Sesion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;          // ✅ faltaba
import java.net.MalformedURLException;
import java.net.URL;

/** Panel principal del administrador del sistema. */
public class AdminFrame extends JFrame {

    /* ---------- Paleta corporativa ---------- */
    private static final Color ROJO       = new Color(194, 24, 7);
    private static final Color ROJO_OSC   = new Color(157, 19, 4);
    private static final Color AMARILLO   = new Color(255, 213, 79);
    private static final Color BLANCO     = Color.WHITE;

    /* ---------- Iconos remotos ---------- */
    private static final String ICON_USERS   =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/1/12/User_icon_2.svg/1200px-User_icon_2.svg.png";
    private static final String ICON_PRODS   =
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4Kh1c3CvjtkQZp0IcKM-jolTgj22GXfMQ7A&s";
    private static final String ICON_REPORTS =
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSEkm6Y_BahvZ8Tf1NuCiZGLR3aPMVPnTcx9g&s";
    private static final String ICON_LOGOUT  =
        "https://thumbs.dreamstime.com/b/bot%C3%B3n-redondo-azul-ci%C3%A1nico-vidrioso-de-la-salida-del-sistema-97912713.jpg";

    /* ---------- Componentes ---------- */
    private JLabel  lblBienvenida;
    private JButton btnUsuarios;
    private JButton btnProductos;
    private JButton btnReportes;
    private JButton btnCerrarSesion;

    public AdminFrame() {
        super("Panel de Administrador – Pollería Pollo Loco");
        setSize(560, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        initComponents();
        actualizarBienvenida();
    }

    /* ---------- Interfaz ---------- */
    private void initComponents() {
        /* ——— Cabecera con gradiente ——— */
        JPanel cabecera = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setPaint(new GradientPaint(0, 0, ROJO, getWidth(), 0, AMARILLO));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        cabecera.setPreferredSize(new Dimension(getWidth(), 60));
        cabecera.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel logo = new JLabel(
                cargarIcono("https://upload.wikimedia.org/wikipedia/commons/7/79/Rotisserie-chicken-icon.png",
                             32, 32));
        cabecera.add(logo, BorderLayout.WEST);

        lblBienvenida = new JLabel("", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBienvenida.setForeground(BLANCO);
        cabecera.add(lblBienvenida, BorderLayout.CENTER);

        add(cabecera, BorderLayout.NORTH);

        /* ——— Botonera ——— */
        JPanel centro = new JPanel(new GridLayout(4, 1, 0, 18));
        centro.setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
        centro.setBackground(BLANCO);

        btnUsuarios     = crearBoton("Gestión de usuarios",  ICON_USERS);
        btnProductos    = crearBoton("Gestión de productos", ICON_PRODS);
        btnReportes     = crearBoton("Reportes y ventas",    ICON_REPORTS);
        btnCerrarSesion = crearBoton("Cerrar sesión",        ICON_LOGOUT);

        btnUsuarios    .addActionListener(this::gestionarUsuarios);
        btnProductos   .addActionListener(this::gestionarProductos);
        btnReportes    .addActionListener(this::verReportes);
        btnCerrarSesion.addActionListener(this::cerrarSesion);

        centro.add(btnUsuarios);
        centro.add(btnProductos);
        centro.add(btnReportes);
        centro.add(btnCerrarSesion);

        add(centro, BorderLayout.CENTER);
    }

    /** Helper para crear botones estilizados. */
    private JButton crearBoton(String texto, String url) {
        JButton b = new JButton(texto, cargarIcono(url, 28, 28));
        b.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setIconTextGap(16);
        b.setForeground(BLANCO);
        b.setBackground(ROJO);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        b.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { b.setBackground(ROJO_OSC); }
            @Override public void mouseExited (MouseEvent e) { b.setBackground(ROJO);     }
        });
        return b;
    }

    /** Descarga y redimensiona un icono; placeholder si falla. */
    private ImageIcon cargarIcono(String url, int w, int h) {
        try {
            Image img = new ImageIcon(new URL(url))
                            .getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch (MalformedURLException e) {
            return new ImageIcon(new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB));
        }
    }

    /* ---------- Texto dinámico ---------- */
    private void actualizarBienvenida() {
        Usuario u = Sesion.getUsuarioActual();
        lblBienvenida.setText("Bienvenido, " +
                ((u != null && u.getNombre() != null) ? u.getNombre() : "Administrador"));
    }

    /* ---------- Acciones ---------- */
    private void gestionarUsuarios (ActionEvent e) { new UsuarioFrame ().setVisible(true); }
    private void gestionarProductos(ActionEvent e) { new ProductoFrame().setVisible(true); }
    private void verReportes       (ActionEvent e) { new ReporteFrame ().setVisible(true); }

    private void cerrarSesion(ActionEvent e) {
        if (JOptionPane.showConfirmDialog(this, "¿Cerrar sesión?", "Confirmar",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Sesion.setUsuarioActual(null);
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
