package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.modelo.Usuario;
import com.mycompany.polloloco.util.Sesion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Panel principal del cajero en el sistema Pollería Pollo Loco.
 */
public class CajeroFrame extends JFrame {

    private JLabel lblBienvenida;
    private JButton btnRegistrarPago;
    private JButton btnEmitirComprobante;
    private JButton btnExportarExcel;
    private JButton btnCerrarSesion;

    public CajeroFrame() {
        super("Panel de Cajero - Pollería Pollo Loco");
        setSize(520, 360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initComponents();
        actualizarBienvenida();
    }

    /** Crea y coloca todos los componentes. */
    private void initComponents() {
        // ---------- Barra superior ----------
        lblBienvenida = new JLabel("", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBienvenida.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblBienvenida, BorderLayout.NORTH);

        // ---------- Botones principales ----------
        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 12, 12));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(30, 80, 30, 80));

        btnRegistrarPago      = new JButton("Registrar pago");
        btnEmitirComprobante  = new JButton("Emitir comprobante");
        btnExportarExcel      = new JButton("Exportar ventas a Excel");
        btnCerrarSesion       = new JButton("Cerrar sesión");

        // Atajos de teclado
        btnRegistrarPago.setMnemonic(KeyEvent.VK_R);      // Alt+R
        btnEmitirComprobante.setMnemonic(KeyEvent.VK_E);  // Alt+E
        btnExportarExcel.setMnemonic(KeyEvent.VK_X);      // Alt+X
        btnCerrarSesion.setMnemonic(KeyEvent.VK_C);       // Alt+C

        // Tooltips
        btnRegistrarPago.setToolTipText("Ctrl+P  –  Registrar el cobro de un pedido");
        btnEmitirComprobante.setToolTipText("Ctrl+M  –  Generar boleta o factura");
        btnExportarExcel.setToolTipText("Ctrl+E  –  Exportar reporte diario a Excel");
        btnCerrarSesion.setToolTipText("Ctrl+Q  –  Finalizar sesión actual");

        // Acciones
        btnRegistrarPago.addActionListener(this::registrarPago);
        btnEmitirComprobante.addActionListener(this::emitirComprobante);
        btnExportarExcel.addActionListener(this::exportarExcel);
        btnCerrarSesion.addActionListener(this::cerrarSesion);

        // Añadir botones
        panelBotones.add(btnRegistrarPago);
        panelBotones.add(btnEmitirComprobante);
        panelBotones.add(btnExportarExcel);
        panelBotones.add(btnCerrarSesion);

        add(panelBotones, BorderLayout.CENTER);

        // Foco inicial
        SwingUtilities.invokeLater(btnRegistrarPago::requestFocusInWindow);

        // Shortcuts globales
        registrarAccesosDirectos();
    }

    /** Actualiza el texto de bienvenida usando la sesión actual. */
    private void actualizarBienvenida() {
        Usuario u = Sesion.getUsuarioActual();
        String nombre = (u != null && u.getNombre() != null) ? u.getNombre() : "Cajero";
        lblBienvenida.setText("Bienvenido, " + nombre);
    }

    // ---------- ACCIONES ----------

    private void registrarPago(ActionEvent e) {
        JOptionPane.showMessageDialog(this,
                "Funcionalidad de registro de pago pendiente.",
                "En desarrollo", JOptionPane.INFORMATION_MESSAGE);
        // TODO: new PagoFrame().setVisible(true);
    }

    private void emitirComprobante(ActionEvent e) {
        JOptionPane.showMessageDialog(this,
                "Funcionalidad de comprobante pendiente.",
                "En desarrollo", JOptionPane.INFORMATION_MESSAGE);
        // TODO: new ComprobanteFrame().setVisible(true);
    }

    private void exportarExcel(ActionEvent e) {
        JOptionPane.showMessageDialog(this,
                "Exportación de ventas a Excel pendiente.",
                "En desarrollo", JOptionPane.INFORMATION_MESSAGE);
        // TODO: usar ReporteExcelExporter con ventas del día
    }

    private void cerrarSesion(ActionEvent e) {
        int op = JOptionPane.showConfirmDialog(this,
                "¿Seguro que deseas cerrar sesión?",
                "Confirmar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (op == JOptionPane.YES_OPTION) {
            Sesion.setUsuarioActual(null);
            dispose();
            new LoginFrame().setVisible(true);
        }
    }

    /** Registra accesos directos de teclado globales. */
    private void registrarAccesosDirectos() {
        JRootPane root = getRootPane();
        InputMap  im   = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am   = root.getActionMap();

        im.put(KeyStroke.getKeyStroke("ctrl P"), "registrarPago");
        im.put(KeyStroke.getKeyStroke("ctrl M"), "emitirComprobante");
        im.put(KeyStroke.getKeyStroke("ctrl E"), "exportarExcel");
        im.put(KeyStroke.getKeyStroke("ctrl Q"), "cerrarSesion");

        am.put("registrarPago",      new AbstractAction() { public void actionPerformed(ActionEvent e) { registrarPago(e); }});
        am.put("emitirComprobante",  new AbstractAction() { public void actionPerformed(ActionEvent e) { emitirComprobante(e); }});
        am.put("exportarExcel",      new AbstractAction() { public void actionPerformed(ActionEvent e) { exportarExcel(e); }});
        am.put("cerrarSesion",       new AbstractAction() { public void actionPerformed(ActionEvent e) { cerrarSesion(e); }});
    }
}
