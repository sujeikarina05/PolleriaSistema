package com.mycompany.polloloco.vista;

import javax.swing.*;
import java.awt.*;
import com.mycompany.polloloco.util.ScreenshotUtil;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

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

        JButton btnCapturar = new JButton("Capturar");
        btnCapturar.addActionListener(e -> ScreenshotUtil.capturarComponente(this));
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pie.add(btnCapturar);
        add(pie, BorderLayout.SOUTH);
    }
}
