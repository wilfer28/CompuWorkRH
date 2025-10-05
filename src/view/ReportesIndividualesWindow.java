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
import service.SistemaRH;
import exceptions.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class ReportesIndividualesWindow extends JFrame {
    private final SistemaRH sistemaRH;
    private JComboBox<String> cmbEmpleado, cmbPeriodo;
    private JTextField txtPuntaje, txtComentarios;
    private JTable tableReportes;
    private DefaultTableModel tableModel;
    
    public ReportesIndividualesWindow(SistemaRH sistemaRH) {
        this.sistemaRH = sistemaRH;
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Reportes Individuales de Desempeño");
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(230, 126, 34));
        JLabel titleLabel = new JLabel("Reportes Individuales de Desempeño");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(250);
        
        JPanel formPanel = createFormPanel();
        splitPane.setTopComponent(formPanel);
        
        JPanel tablePanel = createTablePanel();
        splitPane.setBottomComponent(tablePanel);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Generar Nuevo Reporte"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Empleado:"), gbc);
        gbc.gridx = 1;
        cmbEmpleado = new JComboBox<>();
        panel.add(cmbEmpleado, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Periodo:"), gbc);
        gbc.gridx = 1;
        cmbPeriodo = new JComboBox<>(new String[]{
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        });
        panel.add(cmbPeriodo, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Puntaje (0-100):"), gbc);
        gbc.gridx = 1;
        txtPuntaje = new JTextField(15);
        panel.add(txtPuntaje, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Comentarios:"), gbc);
        gbc.gridx = 1;
        gbc.gridheight = 2;
        txtComentarios = new JTextField(15);
        JScrollPane scrollComments = new JScrollPane(txtComentarios);
        scrollComments.setPreferredSize(new Dimension(200, 60));
        panel.add(scrollComments, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Reportes Existentes"));
        
        String[] columns = {"ID Reporte", "Empleado", "Periodo", "Puntaje", "Fecha", "Estado"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableReportes = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableReportes);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setBackground(new Color(46, 204, 113));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.addActionListener(e -> generarReporte());
        
        JButton btnExportar = new JButton("Exportar Seleccionado");
        btnExportar.setBackground(new Color(52, 152, 219));
        btnExportar.setForeground(Color.WHITE);
        btnExportar.addActionListener(e -> exportarReporte());
        
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.addActionListener(e -> eliminarReporte());
        
        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> loadReportes());
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        
        panel.add(btnGenerar);
        panel.add(btnExportar);
        panel.add(btnEliminar);
        panel.add(btnActualizar);
        panel.add(btnCerrar);
        
        return panel;
    }
    
    private void loadData() {
        loadEmpleados();
        loadReportes();
    }
    
    private void loadEmpleados() {
        cmbEmpleado.removeAllItems();
        List<Empleado> empleados = (List<Empleado>) sistemaRH.listarEmpleados();
        
        for (Empleado emp : empleados) {
            String item = emp.getId() + " - " + emp.getNombre() + " " + emp.getApellido();
            cmbEmpleado.addItem(item);
        }
    }
    
    private void loadReportes() {
        tableModel.setRowCount(0);
        List<ReporteDesempeno> reportes = sistemaRH.listarReportes();
        
        for (ReporteDesempeno reporte : reportes) {
            Empleado emp = sistemaRH.buscarEmpleado(reporte.getEmpleadoId());
            String nombreEmpleado = emp != null ? emp.getNombre() + " " + emp.getApellido() : "Desconocido";
            String estado = reporte.getPuntaje() >= 70 ? "Satisfactorio" : "Necesita Mejora";
            
            Object[] row = {
                reporte.getId(),
                nombreEmpleado,
                reporte.getPeriodo(),
                reporte.getPuntaje(),
                reporte.getFechaEvaluacion(),
                estado
            };
            tableModel.addRow(row);
        }
    }
    
    private void generarReporte() {
        if (cmbEmpleado.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un empleado");
            return;
        }
        
        try {
            String empleadoInfo = (String) cmbEmpleado.getSelectedItem();
            String empleadoId = empleadoInfo.split(" - ")[0];
            
            String periodo = (String) cmbPeriodo.getSelectedItem();
            double puntaje = Double.parseDouble(txtPuntaje.getText().trim());
            String comentarios = txtComentarios.getText().trim();
            
            if (puntaje < 0 || puntaje > 100) {
                throw new IllegalArgumentException("El puntaje debe estar entre 0 y 100");
            }
            
            String reporteId = "REP" + System.currentTimeMillis();
            ReporteDesempeno reporte = new ReporteDesempeno(
                reporteId, empleadoId, periodo, puntaje, comentarios, LocalDate.now()
            );
            
            sistemaRH.agregarReporte(reporte);
            
            JOptionPane.showMessageDialog(this, "Reporte generado exitosamente");
            loadReportes();
            limpiarFormulario();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El puntaje debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (HeadlessException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportarReporte() {
        int selectedRow = tableReportes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un reporte para exportar");
            return;
        }
        
        String reporteId = (String) tableModel.getValueAt(selectedRow, 0);
        ReporteDesempeno reporte = sistemaRH.listarReportes().stream()
                .filter(r -> r.getId().equals(reporteId))
                .findFirst()
                .orElse(null);
        if (reporte != null) {
            String fileName = reporte.exportar();
            JOptionPane.showMessageDialog(this, 
                    "Reporte exportado exitosamente a:\n" + new File(fileName).getAbsolutePath());
        }
    }
    
    private void eliminarReporte() {
        int selectedRow = tableReportes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un reporte para eliminar");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de eliminar este reporte?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            String reporteId = (String) tableModel.getValueAt(selectedRow, 0);
            sistemaRH.eliminarReporte(reporteId);
            JOptionPane.showMessageDialog(this, "Reporte eliminado exitosamente");
            loadReportes();
        }
    }
    
    private void limpiarFormulario() {
        txtPuntaje.setText("");
        txtComentarios.setText("");
        cmbPeriodo.setSelectedIndex(0);
    }
}