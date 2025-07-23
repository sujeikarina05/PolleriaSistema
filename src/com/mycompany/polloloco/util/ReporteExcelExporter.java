package com.mycompany.polloloco.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

/** Exportador genérico de datos a Excel (.xlsx). */
public final class ReporteExcelExporter {

    /** @return true si el archivo se generó sin errores. */
    public static boolean exportar(String ruta, List<String> encabezados, List<List<Object>> datos) {
        try (Workbook libro = new XSSFWorkbook()) {

            Sheet hoja = libro.createSheet("Reporte");
            crearEncabezados(hoja, encabezados, libro);
            cargarDatos(hoja, datos);

            // Ajustar ancho de columnas
            for (int i = 0; i < encabezados.size(); i++) {
                hoja.autoSizeColumn(i, true);
                // (opcional) ancho mínimo
                if (hoja.getColumnWidth(i) < 4000) hoja.setColumnWidth(i, 4000);
            }

            // Asegura que la ruta exista
            File f = new File(ruta);
            f.getParentFile().mkdirs();

            try (FileOutputStream out = new FileOutputStream(f)) {
                libro.write(out);
            }
            return true;

        } catch (IOException e) {
            System.err.println("❌ Error al exportar Excel: " + e.getMessage());
            return false;
        }
    }

    /* ---------- helpers internos ---------- */

    private static void crearEncabezados(Sheet hoja, List<String> cols, Workbook wb) {
        Row fila = hoja.createRow(0);
        CellStyle estilo = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        estilo.setFont(font);

        for (int i = 0; i < cols.size(); i++) {
            Cell c = fila.createCell(i);
            c.setCellValue(cols.get(i));
            c.setCellStyle(estilo);
        }
    }

    private static void cargarDatos(Sheet hoja, List<List<Object>> datos) {
        CreationHelper ch = hoja.getWorkbook().getCreationHelper();
        CellStyle fechaStyle = hoja.getWorkbook().createCellStyle();
        fechaStyle.setDataFormat(ch.createDataFormat().getFormat("yyyy-MM-dd"));

        DecimalFormat df = new DecimalFormat("#.##");

        for (int i = 0; i < datos.size(); i++) {
            Row fila = hoja.createRow(i + 1);
            List<Object> filaDatos = datos.get(i);

            for (int j = 0; j < filaDatos.size(); j++) {
                Cell celda = fila.createCell(j);
                Object val = filaDatos.get(j);

                if (val instanceof Number) {
                    celda.setCellValue(((Number) val).doubleValue());
                } else if (val instanceof LocalDate) {
                    celda.setCellValue((LocalDate) val);
                    celda.setCellStyle(fechaStyle);
                } else {
                    // Formateo simple si es decimal en String
                    if (val instanceof String str && str.matches("^-?\\d+(,?\\d+)?$")) {
                        celda.setCellValue(df.format(Double.parseDouble(str.replace(",", "."))));
                    } else {
                        celda.setCellValue(val != null ? val.toString() : "");
                    }
                }
            }
        }
    }
}
