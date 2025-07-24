package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.POLLOLOCO;
import com.mycompany.polloloco.util.Sesion;
import com.mycompany.polloloco.vista.panel.MesaPanel;
import com.mycompany.polloloco.vista.panel.ProductoPanel;
import com.mycompany.polloloco.vista.panel.ReportePanel;
import com.mycompany.polloloco.vista.panel.UsuarioPanel;
import com.mycompany.polloloco.vista.panel.TurnoPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Panel principal del Administrador con acceso a todos los módulos.
 */
public class AdminFrame extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel panelCentral = new JPanel(cardLayout);

    public AdminFrame() {
        super(POLLOLOCO.TITULO_APP + " - Administración");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initUI();
        Sesion.renovarActividad();
        setVisible(true);
    }

    private void initUI() {
        // Barra de herramientas superior
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.add(createButton("Usuarios", e -> showPanel("USU")));
        toolbar.add(createButton("Productos", e -> showPanel("PRO")));
        toolbar.add(createButton("Mesas", e -> showPanel("MES")));
        toolbar.add(createButton("Turnos", e -> showPanel("TUR")));
        toolbar.add(createButton("Reportes", e -> showPanel("REP")));
        toolbar.addSeparator();
        toolbar.add(createButton("Salir", this::logout));
        add(toolbar, BorderLayout.NORTH);

        // Panel central con CardLayout
        panelCentral.add(new UsuarioPanel(), "USU");
        panelCentral.add(new ProductoPanel(), "PRO");
        panelCentral.add(new MesaPanel(), "MES");
        panelCentral.add(new TurnoPanel(), "TUR");
        panelCentral.add(new ReportePanel(), "REP");
        add(panelCentral, BorderLayout.CENTER);

        // Panel inicial
        showPanel("USU");
    }

    /**
     * Crea un botón de la barra de herramientas.
     */
    private JButton createButton(String title, AbstractAction action) {
        JButton btn = new JButton(new AbstractAction(title) {
            { putValue(SMALL_ICON, null); }
            @Override public void actionPerformed(ActionEvent e) { action.actionPerformed(e); }
        });
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return btn;
    }

    /**
     * Muestra el panel identificado por nombre en el CardLayout.
     */
    private void showPanel(String name) {
        cardLayout.show(panelCentral, name);
        Sesion.renovarActividad();
    }

    /**
     * Cierra la sesión y vuelve al LoginFrame.
     */
    private void logout(ActionEvent e) {
        int opt = JOptionPane.showConfirmDialog(this,
                "¿Cerrar sesión?", "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (opt == JOptionPane.YES_OPTION) {
            Sesion.cerrar();
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
