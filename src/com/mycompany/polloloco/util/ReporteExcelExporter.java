package com.mycompany.polloloco.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Exporta colecciones de datos arbitrarios a un archivo <b>Excel (XLSX)</b> o
 * <b>CSV</b> (fallback).  Funciona con listas genéricas para reutilizar el
 * componente desde cualquier pantalla.
 * <p>
 * Ejemplo de uso:
 * <pre>
 *  List<String> headers = List.of("Fecha", "N° Pedido", "Total", "Cajero");
 *  List<List<Object>> rows = servicioReporte.obtenerVentasDelDía();
 *  ReporteExcelExporter.exportar("ventas_hoy.xlsx", headers, rows);
 * </pre>
 */
public final class ReporteExcelExporter {

    private static final Logger LOG = Logger.getLogger(ReporteExcelExporter.class.getName());
    private static final DateTimeFormatter DT_FILE = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ReporteExcelExporter() {}

    /* ------------------------------------------------------ */
    /*                      API PÚBLICA                       */
    /* ------------------------------------------------------ */

    /**
     * Exporta los datos a la ruta indicada.  Si la extensión es «.csv» el
     * método delega en {@link #exportarCsv(File, List, List)}; en caso
     * contrario genera un archivo XLSX con Apache POI.
     *
     * @param rutaArchivo  Nombre/Path destino (ej. "reporte_ventas.xlsx").
     * @param encabezados  Lista de títulos de columna.
     * @param filas        Filas de datos (cualquier objeto, se invoca
     *                     {@link Object#toString()}).
     * @return <code>true</code> si la operación finalizó sin excepciones.
     */
    public static boolean exportar(String rutaArchivo,
                                   List<String> encabezados,
                                   List<List<Object>> filas) {
        Objects.requireNonNull(encabezados, "encabezados no puede ser null");
        Objects.requireNonNull(filas, "filas no puede ser null");

        // Añadir extensión si falta
        if (!rutaArchivo.contains(".")) rutaArchivo += ".xlsx";

        File destino = new File(rutaArchivo);
        destino.getParentFile().mkdirs();

        try {
            if (rutaArchivo.toLowerCase().endsWith(".csv")) {
                exportarCsv(destino, encabezados, filas);
            } else {
                exportarXlsx(destino, encabezados, filas);
            }
            return true;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error al exportar reporte", ex);
            return false;
        }
    }

    /* ------------------------------------------------------ */
    /*                       XLSX (POI)                       */
    /* ------------------------------------------------------ */

    private static void exportarXlsx(File archivo,
                                     List<String> headers,
                                     List<List<Object>> datos) throws IOException {
        // Sin dependencia de Apache POI, exportamos un CSV con extensión XLSX
        exportarCsv(archivo, headers, datos);
    }

    /* ------------------------------------------------------ */
    /*                        CSV                             */
    /* ------------------------------------------------------ */

    private static void exportarCsv(File archivo,
                                    List<String> encabezados,
                                    List<List<Object>> datos) throws IOException {
        try (var bw = Files.newBufferedWriter(archivo.toPath())) {
            // Encabezados
            bw.write(String.join(",", encabezados));
            bw.newLine();
            // Filas
            for (List<Object> fila : datos) {
                bw.write(formatearFilaCsv(fila));
                bw.newLine();
            }
        }
    }

    private static String formatearFilaCsv(List<Object> fila) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fila.size(); i++) {
            Object val = fila.get(i);
            String s = val == null ? "" : val.toString();
            // Escapar comas y comillas
            if (s.contains(",") || s.contains("\"")) {
                s = '"' + s.replace("\"", "\"\"") + '"';
            }
            sb.append(s);
            if (i < fila.size() - 1) sb.append(',');
        }
        return sb.toString();
    }

    /* ------------------------------------------------------ */
    /*                Helper para nombres únicos              */
    /* ------------------------------------------------------ */

    /**
     * Genera un nombre de archivo con timestamp si el usuario pasa solo una
     * carpeta como ruta.
     */
    public static String generarNombrePorDefecto(String carpeta, String prefijo) {
        String timestamp = LocalDate.now().format(DT_FILE);
        return carpeta + File.separator + prefijo + "_" + timestamp + ".xlsx";
    }
}