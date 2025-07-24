package com.mycompany.polloloco.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilidad para capturar componentes Swing y exportar la imagen en PNG.
 * <p>
 *  ▸ Pregunta la ruta al usuario (JFileChooser)
 *  ▸ Copia la captura al portapapeles
 *  ▸ Crea carpetas automáticamente (capturas/AAAA-MM)
 *  ▸ Añade sello de fecha al nombre si el usuario no lo hace
 */
public final class ScreenshotUtil {

    private static final Logger LOG = Logger.getLogger(ScreenshotUtil.class.getName());
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtil() {}

    /**
     * Captura el componente especificado y guarda la imagen como PNG.
     * También la copia al portapapeles para pegar rápido en chats o docs.
     */
    public static void capturarComponente(Component comp) {
        JFileChooser ch = new JFileChooser();
        ch.setDialogTitle("Guardar captura PNG");
        ch.setSelectedFile(new File(nombreSugerido()));

        if (ch.showSaveDialog(comp) != JFileChooser.APPROVE_OPTION) return;

        File file = ensurePng(ch.getSelectedFile());
        file.getParentFile().mkdirs();

        try {
            BufferedImage img = new BufferedImage(comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            comp.paintAll(g2);
            g2.dispose();

            ImageIO.write(img, "png", file);
            copiarAlPortapapeles(img);

            JOptionPane.showMessageDialog(comp, "Captura guardada en:\n" + file.getAbsolutePath(),
                                          "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "No se pudo guardar la captura", ex);
            JOptionPane.showMessageDialog(comp, "Error al guardar captura: " + ex.getMessage(),
                                          "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /* ------------------------ Helpers ------------------------ */
    private static File ensurePng(File f) {
        String path = f.getAbsolutePath();
        if (!path.toLowerCase().endsWith(".png")) {
            f = new File(path + ".png");
        }
        return f;
    }

    private static String nombreSugerido() {
        LocalDateTime ahora = LocalDateTime.now();
        String carpeta = "capturas/" + ahora.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return carpeta + "/captura_" + ahora.format(FMT) + ".png";
    }

    private static void copiarAlPortapapeles(Image img) {
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable t = new TransferableImage(img);
        cb.setContents(t, null);
    }

    /* Transferable wrapper */
    private record TransferableImage(Image image) implements Transferable {
        @Override public Object getTransferData(java.awt.datatransfer.DataFlavor flavor) {
            if (flavor.equals(java.awt.datatransfer.DataFlavor.imageFlavor)) return image;
            return null;
        }
        @Override public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() {
            return new java.awt.datatransfer.DataFlavor[]{java.awt.datatransfer.DataFlavor.imageFlavor};
        }
        @Override public boolean isDataFlavorSupported(java.awt.datatransfer.DataFlavor flavor) {
            return flavor.equals(java.awt.datatransfer.DataFlavor.imageFlavor);
        }
    }
}
