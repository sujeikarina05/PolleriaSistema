package com.mycompany.polloloco.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
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
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Reporte");

            // Estilos
            CellStyle headerStyle = wb.createCellStyle();
            Font bold = wb.createFont(); bold.setBold(true); headerStyle.setFont(bold);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            CreationHelper createHelper = wb.getCreationHelper();
            CellStyle dateStyle = wb.createCellStyle();
            dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));

            CellStyle moneyStyle = wb.createCellStyle();
            moneyStyle.setDataFormat(createHelper.createDataFormat().getFormat("$#,##0.00"));

            // Encabezados
            Row rowHead = sheet.createRow(0);
            for (int c = 0; c < headers.size(); c++) {
                Cell cell = rowHead.createCell(c);
                cell.setCellValue(headers.get(c));
                cell.setCellStyle(headerStyle);
            }

            // Datos
            int r = 1;
            for (List<Object> fila : datos) {
                Row row = sheet.createRow(r++);
                for (int c = 0; c < fila.size(); c++) {
                    Object val = fila.get(c);
                    Cell cell = row.createCell(c);
                    if (val instanceof Number n) {
                        cell.setCellValue(n.doubleValue());
                        cell.setCellStyle(moneyStyle);
                    } else if (val instanceof LocalDate d) {
                        cell.setCellValue(java.sql.Date.valueOf(d));
                        cell.setCellStyle(dateStyle);
                    } else {
                        cell.setCellValue(val != null ? val.toString() : "");
                    }
                }
            }

            // Autosize columnas
            for (int c = 0; c < headers.size(); c++) sheet.autoSizeColumn(c);

            // Guardar
            try (FileOutputStream fos = new FileOutputStream(archivo)) {
                wb.write(fos);
            }
        }
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