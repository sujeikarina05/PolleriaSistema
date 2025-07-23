package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.controlador.UsuarioController;
import com.mycompany.polloloco.modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/** Ventana de administración básica de usuarios. */
public class UsuarioFrame extends JFrame {

    private final UsuarioController controller = new UsuarioController();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"ID", "Nombre", "Usuario", "Rol"}, 0);
    private final JTable tabla = new JTable(modelo);

    public UsuarioFrame() {
        super("Gestión de Usuarios");
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

        // Acciones básicas (no implementadas en detalle)
        btnNuevo.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Funcionalidad en desarrollo"));
        btnEditar.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Funcionalidad en desarrollo"));
        btnEliminar.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Funcionalidad en desarrollo"));
    }

    private void cargarDatos() {
        modelo.setRowCount(0);
        List<Usuario> lista = controller.listarUsuarios();
        for (Usuario u : lista) {
            modelo.addRow(new Object[]{u.getId(), u.getNombre(), u.getUsuario(), u.getRol()});
        }
    }
}
