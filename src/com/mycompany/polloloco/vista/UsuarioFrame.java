package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.controlador.UsuarioController;
import com.mycompany.polloloco.modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/** Ventana de administración básica de usuarios (UI “Pollería”). */
public class UsuarioFrame extends JFrame {

    /* ─────  ICONOS REMOTOS  ───── */
    private static final String ICO_NEW    = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQMOg2TMzxNa0M6DC2q6pQuESQfgu7tOgWFgg&s";
    private static final String ICO_EDIT   = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSZQ5NJ07sTnqP-Pe8TANi-lvKZ6Eec5gITxw&s";
    private static final String ICO_DELETE = "https://cdn-icons-png.flaticon.com/512/3687/3687412.png";

    /* ─────  MVC  ───── */
    private final UsuarioController controller = new UsuarioController();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Usuario", "Rol"}, 0);

    private final JTable tabla = new JTable(modelo);

    public UsuarioFrame() {
        super("Gestión de Usuarios");
        setSize(700, 460);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        construirUI();
        cargarDatos();
    }

    /* ---------- UI ---------- */
    private void construirUI() {
        /* Paleta */
        Color colFondo   = new Color(0xFFF8E1);          // amarillo pálido
        Color colHeader  = new Color(0xFFC107);          // ámbar
        Color colZebra   = new Color(0xFFF3CE);
        Color colHover   = new Color(0xFFE082);

        setLayout(new BorderLayout());
        getContentPane().setBackground(colFondo);

        /* Cabecera */
        JLabel cab = new JLabel("Listado de usuarios", SwingConstants.CENTER);
        cab.setFont(new Font("Segoe UI", Font.BOLD, 18));
        cab.setForeground(Color.DARK_GRAY);
        cab.setOpaque(true);
        cab.setBackground(colHeader);
        cab.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(cab, BorderLayout.NORTH);

        /* Tabla con zebra */
        tabla.setRowHeight(24);
        tabla.setSelectionBackground(new Color(0xFFE082));
        tabla.setFillsViewportHeight(true);
        tabla.setDefaultEditor(Object.class, null);          // solo lectura
        DefaultTableCellRenderer zebra = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setBackground(sel ? colHover : (row % 2 == 0 ? Color.WHITE : colZebra));
                return this;
            }
        };
        for (int i = 0; i < tabla.getColumnCount(); i++)
            tabla.getColumnModel().getColumn(i).setCellRenderer(zebra);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        /* Botonera */
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        botones.setOpaque(false);

        JButton btnNuevo   = crearBoton("Nuevo",   ICO_NEW);
        JButton btnEditar  = crearBoton("Editar",  ICO_EDIT);
        JButton btnEliminar= crearBoton("Eliminar",ICO_DELETE);

        botones.add(btnNuevo);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        add(botones, BorderLayout.SOUTH);

        /* Acciones demo */
        btnNuevo.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Funcionalidad Nuevo (pendiente)"));
        btnEditar.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Funcionalidad Editar (pendiente)"));
        btnEliminar.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Funcionalidad Eliminar (pendiente)"));
    }

    /* ---------- Datos ---------- */
    private void cargarDatos() {
        modelo.setRowCount(0);
        List<Usuario> lista = controller.listarUsuarios();
        for (Usuario u : lista) {
            modelo.addRow(new Object[]{
                    u.getId(), u.getNombre(), u.getUsuario(), u.getRol()
            });
        }
    }

    /* ---------- Util ---------- */
    private JButton crearBoton(String texto, String urlIcon) {
        ImageIcon ico = new ImageIcon(new ImageIcon(urlIcon)
                .getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH));
        JButton b = new JButton(texto, ico);
        b.setFocusPainted(false);
        b.setBackground(new Color(0xFFE082));
        b.setForeground(Color.DARK_GRAY);
        b.setBorder(BorderFactory.createEmptyBorder(6,12,6,12));

        /* Hover */
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(new Color(0xFFD54F));
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(new Color(0xFFE082));
            }
        });
        return b;
    }
}
