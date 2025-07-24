package com.mycompany.polloloco.vista;

import javax.swing.*;
import java.awt.*;
import com.mycompany.polloloco.util.ScreenshotUtil;

/** Ventana simple para registrar pedidos (demo). */
public class PedidoFrame extends JFrame {

    public PedidoFrame() {
        super("Registrar Pedido");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        construirUI();
    }

    private void construirUI() {
        JLabel lbl = new JLabel("Pantalla de registro de pedidos en desarrollo", SwingConstants.CENTER);
        add(lbl, BorderLayout.CENTER);
        JButton btnCapturar = new JButton("Capturar");
        btnCapturar.addActionListener(e -> ScreenshotUtil.capturarComponente(this));
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pie.add(btnCapturar);
        add(pie, BorderLayout.SOUTH);
    }
}
