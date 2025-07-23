package com.mycompany.polloloco.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Exportador genérico de datos a archivo Excel (.xlsx).
 */
public class ReporteExcelExporter {

    /**
     * Exporta una tabla simple a un archivo Excel.
     * @param nombreArchivo ruta del archivo a generar
     * @param encabezados lista de encabezados de columnas
     * @param datos lista de filas (cada fila es una lista de objetos)
     */
    public void exportar(String nombreArchivo, List<String> encabezados, List<List<Object>> datos) {
        try (Workbook libro = new XSSFWorkbook()) {
            Sheet hoja = libro.createSheet("Reporte");

            // Estilo de encabezado
            CellStyle estiloEncabezado = libro.createCellStyle();
            Font fuente = libro.createFont();
            fuente.setBold(true);
            estiloEncabezado.setFont(fuente);

            // Crear fila de encabezados
            Row filaEncabezado = hoja.createRow(0);
            for (int i = 0; i < encabezados.size(); i++) {
                Cell celda = filaEncabezado.createCell(i);
                celda.setCellValue(encabezados.get(i));
                celda.setCellStyle(estiloEncabezado);
            }

            // Llenar contenido
            for (int i = 0; i < datos.size(); i++) {
                Row fila = hoja.createRow(i + 1);
                List<Object> filaDatos = datos.get(i);
                for (int j = 0; j < filaDatos.size(); j++) {
                    Cell celda = fila.createCell(j);
                    Object valor = filaDatos.get(j);
                    if (valor instanceof Number) {
                        celda.setCellValue(((Number) valor).doubleValue());
                    } else {
                        celda.setCellValue(valor != null ? valor.toString() : "");
                    }
                }
            }

            // Autosize columnas
            for (int i = 0; i < encabezados.size(); i++) {
                hoja.autoSizeColumn(i);
            }

            // Guardar archivo
            try (FileOutputStream out = new FileOutputStream(nombreArchivo)) {
                libro.write(out);
                System.out.println("Archivo Excel generado: " + nombreArchivo);
            }

        } catch (IOException e) {
            System.err.println("Error al exportar a Excel: " + e.getMessage());
        }
    }
}
