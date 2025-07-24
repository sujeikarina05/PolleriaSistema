package com.mycompany.polloloco.vista;

import com.mycompany.polloloco.util.ReporteExcelExporter;
import com.mycompany.polloloco.util.ScreenshotUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Ventana para mostrar reportes de pedidos y exportarlos. */
public class ReporteFrame extends JFrame {

    /* ─────  ICONOS  ───── */
    private static final String ICO_REFRESH = "https://icons.iconarchive.com/icons/custom-icon-design/flatastic-1/32/refresh-icon.png";
    private static final String ICO_EXCEL   = "https://img.icons8.com/?size=512&id=117561&format=png";   // <- NUEVO
    private static final String ICO_CAMERA  = "https://icons.iconarchive.com/icons/custom-icon-design/flatastic-2/32/camera-icon.png";

    /* ─────  Swing  ───── */
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Fecha", "Pedido", "Total", "Usuario"}, 0);
    private final JTable tabla = new JTable(modelo);

    private final JButton btnActualizar = crearBoton("Actualizar (F5)", ICO_REFRESH);
    private final JButton btnExportar   = crearBoton("Exportar a Excel",  ICO_EXCEL);
    private final JButton btnCapturar   = crearBoton("Capturar",          ICO_CAMERA);

    private final JComboBox<String> comboFiltro =
            new JComboBox<>(new String[]{"Hoy", "Esta semana", "Este mes"});

    public ReporteFrame() {
        super("Reportes ‑ Pollería Pollo Loco");
        setSize(760, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        construirUI();
        cargarDatos();           // primera carga
    }

    /* ---------- UI ---------- */
    private void construirUI() {
        Color colFondo  = new Color(0xFFF8E1);
        Color colHeader = new Color(0xFFC107);
        Color colZebra  = new Color(0xFFF3CE);
        Color colHover  = new Color(0xFFE082);

        getContentPane().setBackground(colFondo);
        setLayout(new BorderLayout());

        /* Cabecera */
        JPanel top = new JPanel(new GridBagLayout());
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(10,10,5,10));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5,5,5,5);

        g.gridx = 0; top.add(new JLabel("Filtrar por:"), g);
        g.gridx = 1; top.add(comboFiltro, g);
        g.gridx = 2; top.add(btnActualizar, g);
        add(top, BorderLayout.NORTH);

        /* Tabla */
        tabla.setRowHeight(24);
        tabla.setSelectionBackground(colHover);
        tabla.setAutoCreateRowSorter(true);

        DefaultTableCellRenderer zebra = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t,Object val,boolean sel,boolean foc,int row,int col){
                super.getTableCellRendererComponent(t,val,sel,foc,row,col);
                setBackground(sel?colHover:(row%2==0?Color.WHITE:colZebra));
                return this;
            }
        };
        for(int i=0;i<tabla.getColumnCount();i++)
            tabla.getColumnModel().getColumn(i).setCellRenderer(zebra);

        JScrollPane sp = new JScrollPane(tabla);
        sp.setBorder(BorderFactory.createLineBorder(colHeader,2));
        add(sp, BorderLayout.CENTER);

        /* Pie */
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,10));
        pie.setOpaque(false);
        pie.add(btnExportar);
        pie.add(btnCapturar);
        add(pie, BorderLayout.SOUTH);

        /* Listeners */
        btnActualizar.addActionListener(e -> cargarDatos());
        btnExportar  .addActionListener(e -> exportarExcel());
        btnCapturar  .addActionListener(e -> ScreenshotUtil.capturarComponente(this));

        /* F5 recarga */
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                     .put(KeyStroke.getKeyStroke("F5"), "REFRESH");
        getRootPane().getActionMap()
                     .put("REFRESH", new AbstractAction() {
                         public void actionPerformed(java.awt.event.ActionEvent e){ cargarDatos(); }
                     });
    }

    /* ---------- Datos (demo) ---------- */
    private void cargarDatos() {
        modelo.setRowCount(0);
        modelo.addRow(new Object[]{"2025‑07‑23", "#001", 48.00, "cajero1"});
        modelo.addRow(new Object[]{"2025‑07‑23", "#002", 32.50, "cajero2"});
        modelo.addRow(new Object[]{"2025‑07‑23", "#003", 78.90, "cajero1"});
        // TODO: conectar con PedidoDAO según filtro
    }

    /* ---------- Exportar ---------- */
    private void exportarExcel() {
        if (modelo.getRowCount()==0){
            JOptionPane.showMessageDialog(this,"⚠ No hay datos para exportar.");
            return;
        }
        List<String> enc = List.of("Fecha","Pedido","Total","Usuario");
        List<List<Object>> filas = obtenerFilasTabla();

        JFileChooser ch = new JFileChooser();
        ch.setDialogTitle("Guardar reporte");
        if (ch.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){
            String path = ch.getSelectedFile().getAbsolutePath();
            if (!path.toLowerCase().endsWith(".csv")) path+=".csv";
            ReporteExcelExporter.exportar(path, enc, filas);
            JOptionPane.showMessageDialog(this,
                    "Reporte exportado con "+filas.size()+" filas.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private List<List<Object>> obtenerFilasTabla(){
        List<List<Object>> out = new ArrayList<>();
        for(int r=0;r<modelo.getRowCount();r++){
            List<Object> fila = new ArrayList<>();
            for(int c=0;c<modelo.getColumnCount();c++)
                fila.add(modelo.getValueAt(r,c));
            out.add(fila);
        }
        return out;
    }

    /* ---------- Util ---------- */
    private JButton crearBoton(String txt,String url){
        ImageIcon ico = new ImageIcon(new ImageIcon(url)
                .getImage().getScaledInstance(22,22,Image.SCALE_SMOOTH));
        JButton b = new JButton(txt, ico);
        b.setFocusPainted(false);
        b.setBackground(new Color(0xFFE082));
        b.setForeground(Color.DARK_GRAY);
        b.setBorder(BorderFactory.createEmptyBorder(6,12,6,12));
        b.addMouseListener(new java.awt.event.MouseAdapter(){
            @Override public void mouseEntered(java.awt.event.MouseEvent e){ b.setBackground(new Color(0xFFD54F)); }
            @Override public void mouseExited (java.awt.event.MouseEvent e){ b.setBackground(new Color(0xFFE082)); }
        });
        return b;
    }
}