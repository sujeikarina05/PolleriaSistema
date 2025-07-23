package com.mycompany.polloloco;

import com.mycompany.polloloco.vista.LoginFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {

    public static void main(String[] args) {
        // Establecer el Look and Feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException |
                 IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.err.println("No se pudo establecer el LookAndFeel: " + ex.getMessage());
        }

        // Ejecutar en el hilo de eventos de Swing (EDT)
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                new LoginFrame().setVisible(true);
            } catch (Exception ex) {
                System.err.println("Error al iniciar LoginFrame: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
}
