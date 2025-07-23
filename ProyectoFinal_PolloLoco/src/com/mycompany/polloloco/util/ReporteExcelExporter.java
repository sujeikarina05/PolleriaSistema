package com.mycompany.polloloco.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/** Exportador simple que escribe los datos en formato CSV. */
public final class ReporteExcelExporter {

    /**
     * Exporta los datos a la ruta especificada en formato CSV.
     * @return true si se escribió sin errores
     */
    public static boolean exportar(String ruta, List<String> encabezados, List<List<Object>> datos) {
        File f = new File(ruta);
        if (!f.getName().toLowerCase().endsWith(".csv")) {
            f = new File(ruta + ".csv");
        }
        f.getParentFile().mkdirs();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            bw.write(String.join(",", encabezados));
            bw.newLine();
            for (List<Object> fila : datos) {
                bw.write(formatearFila(fila));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("❌ Error al exportar reporte: " + e.getMessage());
            return false;
        }
    }

    private static String formatearFila(List<Object> fila) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fila.size(); i++) {
            Object val = fila.get(i);
            if (val instanceof LocalDate d) {
                sb.append(d.toString());
            } else {
                sb.append(val != null ? val.toString() : "");
            }
            if (i < fila.size() - 1) sb.append(',');
        }
        return sb.toString();
    }
}
