package com.mycompany.polloloco.vista;

import javax.swing.*;
import java.awt.*;

/** Ventana sencilla para registrar pagos de pedidos. */
public class PagoFrame extends JFrame {

    public PagoFrame() {
        super("Registrar Pago");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        construirUI();
    }

    private void construirUI() {
        JLabel lbl = new JLabel("Pantalla de registro de pagos en desarrollo", SwingConstants.CENTER);
        add(lbl, BorderLayout.CENTER);
    }
}
