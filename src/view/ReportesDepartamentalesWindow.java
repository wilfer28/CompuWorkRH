/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author Wilfer
 */
import model.*;
import java.util.ArrayList;
import service.SistemaRH;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportesDepartamentalesWindow extends JFrame {
    private final SistemaRH sistemaRH;
    private JComboBox<String> cmbDepartamento, cmbPeriodo;
    private JTable tableReporte;
    private DefaultTableModel tableModel;
    private JTextArea txtResumen;
    
    public ReportesDepartamentalesWindow(SistemaRH sistemaRH) {
        this.sistemaRH = sistemaRH;
        initComponents();
        loadDepartamentos();
    }
    
    private void initComponents() {
        setTitle("Reportes Departamentales de Desempeño");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(231, 76, 60));
        JLabel titleLabel = new JLabel("Reportes Departamentales de Desempeño");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Filter panel
        JPanel filterPanel = createFilterPanel();
        mainPanel.add(filterPanel, BorderLayout.NORTH);
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(350);
        
        JPanel tablePanel = createTablePanel();
        splitPane.setTopComponent(tablePanel);
        
        JPanel resumenPanel = createResumenPanel();
        splitPane.setBottomComponent(resumenPanel);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Filtros de Búsqueda"));
        
        panel.add(new JLabel("Departamento:"));
        cmbDepartamento = new JComboBox<>();
        cmbDepartamento.setPreferredSize(new Dimension(200, 25));
        panel.add(cmbDepartamento);
        
        panel.add(new JLabel("Periodo:"));
        cmbPeriodo = new JComboBox<>(new String[]{
            "Todos", "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        });
        panel.add(cmbPeriodo);
        
        JButton btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setBackground(new Color(46, 204, 113));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.addActionListener(e -> generarReporteDepartamental());
        panel.add(btnGenerar);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Detalle de Reportes por Empleado"));
        
        String[] columns = {"Empleado ID", "Nombre", "Tipo", "Periodo", "Puntaje", "Estado", "Fecha"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableReporte = new JTable(tableModel);
        tableReporte.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(tableReporte);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createResumenPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Resumen Estadístico"));
        
        txtResumen = new JTextArea();
        txtResumen.setEditable(false);
        txtResumen.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtResumen.setMargin(new Insets(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(txtResumen);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnExportar = new JButton("Exportar Reporte Completo");
        btnExportar.setBackground(new Color(52, 152, 219));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.addActionListener(e -> exportarReporteCompleto());
        
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarReporte());
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        
        panel.add(btnExportar);
        panel.add(btnLimpiar);
        panel.add(btnCerrar);
        
        return panel;
    }
    
    private void loadDepartamentos() {
        cmbDepartamento.removeAllItems();
        List<Departamento> departamentos = new ArrayList<>(sistemaRH.listarDepartamentos());
        
        for (Departamento dept : departamentos) {
            String item = dept.getId() + " - " + dept.getNombre();
            cmbDepartamento.addItem(item);
        }
    }
    
    private void generarReporteDepartamental() {
        if (cmbDepartamento.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un departamento");
            return;
        }
        
        try {
            String deptInfo = (String) cmbDepartamento.getSelectedItem();
            String deptId = deptInfo.split(" - ")[0];
            String periodoSeleccionado = (String) cmbPeriodo.getSelectedItem();
            
            Departamento dept = sistemaRH.buscarDepartamento(deptId);
            if (dept == null) {
                JOptionPane.showMessageDialog(this, "Departamento no encontrado");
                return;
            }
            
            tableModel.setRowCount(0);
            List<Empleado> empleados = new ArrayList<>(dept.getEmpleados());
            List<ReporteDesempeno> reportes = sistemaRH.listarReportes();
            
            double sumaPuntajes = 0;
            int cantidadReportes = 0;
            int satisfactorios = 0;
            int necesitanMejora = 0;
            
            for (Empleado emp : empleados) {
                for (ReporteDesempeno reporte : reportes) {
                    if (reporte.getEmpleadoId().equals(emp.getId())) {
                        // Filtrar por periodo si no es "Todos"
                        if (periodoSeleccionado.equals("Todos") || 
                            reporte.getPeriodo().equals(periodoSeleccionado)) {
                            
                            String tipo = emp instanceof EmpleadoPermanente ? "Permanente" : "Temporal";
                            String estado = reporte.getPuntaje() >= 70 ? "Satisfactorio" : "Necesita Mejora";
                            
                            if (reporte.getPuntaje() >= 70) {
                                satisfactorios++;
                            } else {
                                necesitanMejora++;
                            }
                            
                            Object[] row = {
                                emp.getId(),
                                emp.getNombre() + " " + emp.getApellido(),
                                tipo,
                                reporte.getPeriodo(),
                                String.format("%.2f", reporte.getPuntaje()),
                                estado,
                                reporte.getFechaEvaluacion()
                            };
                            tableModel.addRow(row);
                            
                            sumaPuntajes += reporte.getPuntaje();
                            cantidadReportes++;
                        }
                    }
                }
            }
            
            // Generar resumen estadístico
            generarResumen(dept, empleados.size(), cantidadReportes, sumaPuntajes, 
                          satisfactorios, necesitanMejora);
            
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generarResumen(Departamento dept, int totalEmpleados, int cantidadReportes,
                                double sumaPuntajes, int satisfactorios, int necesitanMejora) {
        StringBuilder resumen = new StringBuilder();
        resumen.append("═══════════════════════════════════════════════════════════════\n");
        resumen.append("               RESUMEN ESTADÍSTICO DEL DEPARTAMENTO\n");
        resumen.append("═══════════════════════════════════════════════════════════════\n\n");
        
        resumen.append(String.format("Departamento: %s\n", dept.getNombre()));
        resumen.append(String.format("Ubicación: %s\n", dept.getUbicacion()));
        resumen.append(String.format("Presupuesto: $%.2f\n\n", dept.getPresupuesto()));
        
        resumen.append("───────────────────────────────────────────────────────────────\n");
        resumen.append("                      MÉTRICAS GENERALES\n");
        resumen.append("───────────────────────────────────────────────────────────────\n\n");
        
        resumen.append(String.format("Total de Empleados: %d\n", totalEmpleados));
        resumen.append(String.format("Total de Reportes Generados: %d\n", cantidadReportes));
        
        if (cantidadReportes > 0) {
            double promedio = sumaPuntajes / cantidadReportes;
            resumen.append(String.format("Puntaje Promedio: %.2f\n\n", promedio));
            
            resumen.append("───────────────────────────────────────────────────────────────\n");
            resumen.append("                   DISTRIBUCIÓN DE DESEMPEÑO\n");
            resumen.append("───────────────────────────────────────────────────────────────\n\n");
            
            resumen.append(String.format("✓ Desempeño Satisfactorio (≥70): %d reportes (%.1f%%)\n", 
                                        satisfactorios, 
                                        (satisfactorios * 100.0 / cantidadReportes)));
            resumen.append(String.format("✗ Necesita Mejora (<70): %d reportes (%.1f%%)\n\n", 
                                        necesitanMejora, 
                                        (necesitanMejora * 100.0 / cantidadReportes)));
            
            // Clasificación del departamento
            String clasificacion;
            if (promedio >= 90) {
                clasificacion = "EXCELENTE ⭐⭐⭐";
            } else if (promedio >= 80) {
                clasificacion = "MUY BUENO ⭐⭐";
            } else if (promedio >= 70) {
                clasificacion = "BUENO ⭐";
            } else if (promedio >= 60) {
                clasificacion = "REGULAR";
            } else {
                clasificacion = "DEFICIENTE";
            }
            
            resumen.append("───────────────────────────────────────────────────────────────\n");
            resumen.append("                  CLASIFICACIÓN DEL DEPARTAMENTO\n");
            resumen.append("───────────────────────────────────────────────────────────────\n\n");
            resumen.append(String.format("        %s\n", clasificacion));
            
        } else {
            resumen.append("\n⚠ No hay reportes disponibles para este departamento en el periodo seleccionado.\n");
        }
        
        resumen.append("\n═══════════════════════════════════════════════════════════════\n");
        
        txtResumen.setText(resumen.toString());
    }
    
    private void exportarReporteCompleto() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No hay datos para exportar. Genere un reporte primero.");
            return;
        }
        
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "Reporte_Departamental_" + timestamp + ".txt";
            
            // Escribir encabezado
            try (FileWriter writer = new FileWriter(fileName)) {
                // Escribir encabezado
                writer.write("═══════════════════════════════════════════════════════════════\n");
                writer.write("          REPORTE DEPARTAMENTAL DE DESEMPEÑO\n");
                writer.write("                    CompuWork RRHH\n");
                writer.write("═══════════════════════════════════════════════════════════════\n\n");
                writer.write("Fecha de generación: " + LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n\n");
                
                // Escribir resumen
                writer.write(txtResumen.getText());
                writer.write("\n\n");
                
                // Escribir detalle de reportes
                writer.write("═══════════════════════════════════════════════════════════════\n");
                writer.write("              DETALLE DE REPORTES POR EMPLEADO\n");
                writer.write("═══════════════════════════════════════════════════════════════\n\n");
                
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    writer.write(String.format("Empleado: %s - %s\n",
                            tableModel.getValueAt(i, 0),
                            tableModel.getValueAt(i, 1)));
                    writer.write(String.format("  Tipo: %s\n", tableModel.getValueAt(i, 2)));
                    writer.write(String.format("  Periodo: %s\n", tableModel.getValueAt(i, 3)));
                    writer.write(String.format("  Puntaje: %s\n", tableModel.getValueAt(i, 4)));
                    writer.write(String.format("  Estado: %s\n", tableModel.getValueAt(i, 5)));
                    writer.write(String.format("  Fecha Evaluación: %s\n", tableModel.getValueAt(i, 6)));
                    writer.write("───────────────────────────────────────────────────────────────\n");
                }
                
                writer.write("\n═══════════════════════════════════════════════════════════════\n");
                writer.write("                     FIN DEL REPORTE\n");
                writer.write("═══════════════════════════════════════════════════════════════\n");
            }
            
            JOptionPane.showMessageDialog(this, 
                "Reporte exportado exitosamente a: " + new java.io.File(fileName).getAbsolutePath());
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al exportar reporte: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void limpiarReporte() {
        tableModel.setRowCount(0);
        txtResumen.setText("");
        cmbPeriodo.setSelectedIndex(0);
    }
}