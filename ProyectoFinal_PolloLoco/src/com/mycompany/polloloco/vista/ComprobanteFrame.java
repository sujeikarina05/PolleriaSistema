package com.mycompany.polloloco.vista;

import javax.swing.*;
import java.awt.*;

/** Ventana para emitir comprobantes de pago. */
public class ComprobanteFrame extends JFrame {

    public ComprobanteFrame() {
        super("Emitir Comprobante");
        setSize(420, 260);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        construirUI();
    }

    private void construirUI() {
        JLabel lbl = new JLabel("Pantalla de emisión de comprobantes en desarrollo", SwingConstants.CENTER);
        add(lbl, BorderLayout.CENTER);
    }
}
