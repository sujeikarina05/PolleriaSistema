package com.mycompany.polloloco.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/** Utilidad para capturar componentes de Swing como PNG. */
public final class ScreenshotUtil {

    private ScreenshotUtil() {}

    /**
     * Permite capturar el componente especificado y guardar la imagen
     * en un archivo PNG seleccionado por el usuario.
     */
    public static void capturarComponente(Component comp) {
        JFileChooser ch = new JFileChooser();
        ch.setDialogTitle("Guardar captura");
        if (ch.showSaveDialog(comp) == JFileChooser.APPROVE_OPTION) {
            File f = ch.getSelectedFile();
            if (!f.getName().toLowerCase().endsWith(".png")) {
                f = new File(f.getAbsolutePath() + ".png");
            }
            BufferedImage img = new BufferedImage(
                    comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = img.createGraphics();
            comp.paintAll(g2);
            g2.dispose();
            try {
                ImageIO.write(img, "png", f);
                JOptionPane.showMessageDialog(comp,
                        "Captura guardada en " + f.getAbsolutePath(),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(comp,
                        "No se pudo guardar la captura: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
