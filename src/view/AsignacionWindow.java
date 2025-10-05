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
import exceptions.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Collection;
import java.util.List;

public class AsignacionWindow extends JFrame {
    private final SistemaRH sistemaRH;
    private JComboBox<String> cmbEmpleado, cmbDepartamento;
    private JTable tableAsignaciones;
    private DefaultTableModel tableModel;
    
    public AsignacionWindow(SistemaRH sistemaRH) {
        this.sistemaRH = sistemaRH;
        initComponents();
        loadData();
    }
    
    private void initComponents() {
        setTitle("Asignación de Empleados a Departamentos");
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(155, 89, 182));
        JLabel titleLabel = new JLabel("Asignación de Empleados a Departamentos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.NORTH);
        
        // Table panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Nueva Asignación"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Empleado:"), gbc);
        
        gbc.gridx = 1;
        cmbEmpleado = new JComboBox<>();
        gbc.weightx = 1.0;
        panel.add(cmbEmpleado, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0;
        panel.add(new JLabel("   Departamento:"), gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0;
        cmbDepartamento = new JComboBox<>();
        panel.add(cmbDepartamento, gbc);
        
        gbc.gridx = 4; gbc.weightx = 0;
        JButton btnAsignar = new JButton("Asignar");
        btnAsignar.setBackground(new Color(46, 204, 113));
        btnAsignar.setForeground(Color.WHITE);
        btnAsignar.addActionListener(e -> asignarEmpleado());
        panel.add(btnAsignar, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Asignaciones Actuales"));
        
        String[] columns = {"Empleado ID", "Nombre Completo", "Tipo", "Departamento", "Ubicación"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableAsignaciones = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableAsignaciones);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnRemover = new JButton("Remover Asignación");
        btnRemover.setBackground(new Color(231, 76, 60));
        btnRemover.setForeground(Color.WHITE);
        btnRemover.addActionListener(e -> removerAsignacion());
        
        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.addActionListener(e -> loadAsignaciones());
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        
        panel.add(btnRemover);
        panel.add(btnActualizar);
        panel.add(btnCerrar);
        
        return panel;
    }
    
    private void loadData() {
        loadEmpleados();
        loadDepartamentos();
        loadAsignaciones();
    }
    
    private void loadEmpleados() {
        cmbEmpleado.removeAllItems();
        List<Empleado> empleados = new ArrayList<>(sistemaRH.listarEmpleados());
        
        for (Empleado emp : empleados) {
            String item = emp.getId() + " - " + emp.getNombre() + " " + emp.getApellido();
            cmbEmpleado.addItem(item);
        }
    }
    
    private void loadDepartamentos() {
        cmbDepartamento.removeAllItems();
        List<Departamento> departamentos = new ArrayList<>(sistemaRH.listarDepartamentos());
        
        for (Departamento dept : departamentos) {
            String item = dept.getId() + " - " + dept.getNombre();
            cmbDepartamento.addItem(item);
        }
    }
    
    private void loadAsignaciones() {
        tableModel.setRowCount(0);
        List<Departamento> departamentos = new ArrayList<>(sistemaRH.listarDepartamentos());
        
        for (Departamento dept : departamentos) {
            for (Empleado emp : dept.getEmpleados()) {
                String tipo = emp instanceof EmpleadoPermanente ? "Permanente" : "Temporal";
                Object[] row = {
                    emp.getId(),
                    emp.getNombre() + " " + emp.getApellido(),
                    tipo,
                    dept.getNombre(),
                    dept.getUbicacion()
                };
                tableModel.addRow(row);
            }
        }
    }
    
    private void asignarEmpleado() {
        if (cmbEmpleado.getSelectedItem() == null || cmbDepartamento.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un empleado y un departamento");
            return;
        }
        
        try {
            String empleadoInfo = (String) cmbEmpleado.getSelectedItem();
            String empleadoId = empleadoInfo.split(" - ")[0];
            
            String departamentoInfo = (String) cmbDepartamento.getSelectedItem();
            String departamentoId = departamentoInfo.split(" - ")[0];
            
            sistemaRH.asignarEmpleadoADepartamento(empleadoId, departamentoId);
            
            JOptionPane.showMessageDialog(this, "Empleado asignado exitosamente");
            loadAsignaciones();
            
        } catch (NotFoundException | AssignmentException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void removerAsignacion() {
        int selectedRow = tableAsignaciones.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una asignación para remover");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de remover esta asignación?",
            "Confirmar remoción",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String empleadoId = (String) tableModel.getValueAt(selectedRow, 0);
                
                // Buscar el departamento del empleado
                Collection<Departamento> departamentos = sistemaRH.listarDepartamentos();
                for (Departamento dept : departamentos) {
                    Empleado emp = dept.getEmpleados().stream()
                        .filter(e -> String.valueOf (e.getId()).equals(empleadoId))
                        .findFirst()
                        .orElse(null);
                    
                    if (emp != null) {
                        dept.removerEmpleado(emp);
                        JOptionPane.showMessageDialog(this, "Asignación removida exitosamente");
                        loadAsignaciones();
                        return;
                    }
                }
                
            } catch (AssignmentException | HeadlessException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
