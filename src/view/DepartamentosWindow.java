/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;
import java.util.ArrayList;
import model.Departamento;
import service.SistemaRH;
import exceptions.NotFoundException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
/**
 *
 * @author Wilfer
 */
public class DepartamentosWindow extends JFrame {
    private final SistemaRH sistemaRH;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtId, txtNombre, txtDescripcion, txtPresupuesto, txtUbicacion;
    
    public DepartamentosWindow(SistemaRH sistemaRH) {
        this.sistemaRH = sistemaRH;
        initComponents();
        loadDepartamentos();
    }
    
    private void initComponents() {
        setTitle("Gestión de Departamentos");
        setSize(900, 600);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(46, 204, 113));
        JLabel titleLabel = new JLabel("Gestión de Departamentos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(350);
        
        JPanel formPanel = createFormPanel();
        splitPane.setLeftComponent(new JScrollPane(formPanel));
        
        JPanel tablePanel = createTablePanel();
        splitPane.setRightComponent(tablePanel);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Departamento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(15);
        panel.add(txtId, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(15);
        panel.add(txtNombre, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        txtDescripcion = new JTextField(15);
        panel.add(txtDescripcion, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Presupuesto:"), gbc);
        gbc.gridx = 1;
        txtPresupuesto = new JTextField(15);
        panel.add(txtPresupuesto, gbc);
        
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Ubicación:"), gbc);
        gbc.gridx = 1;
        txtUbicacion = new JTextField(15);
        panel.add(txtUbicacion, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Departamentos"));
        
        String[] columns = {"ID", "Nombre", "Descripción", "Presupuesto", "Empleados"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedDepartamento();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnCrear = new JButton("Crear");
        btnCrear.setBackground(new Color(46, 204, 113));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.addActionListener(e -> crearDepartamento());
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.addActionListener(e -> actualizarDepartamento());
        
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.addActionListener(e -> eliminarDepartamento());
        
        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        
        panel.add(btnCrear);
        panel.add(btnActualizar);
        panel.add(btnEliminar);
        panel.add(btnLimpiar);
        panel.add(btnCerrar);
        
        return panel;
    }
    
    private void crearDepartamento() {
        try {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            double presupuesto = Double.parseDouble(txtPresupuesto.getText().trim());
            String ubicacion = txtUbicacion.getText().trim();
            
            if (id.isEmpty() || nombre.isEmpty()) {
                throw new IllegalArgumentException("ID y Nombre son obligatorios");
            }
            
            Departamento dept = new Departamento(id, nombre, descripcion, presupuesto, ubicacion);
            sistemaRH.agregarDepartamento(dept);
            
            JOptionPane.showMessageDialog(this, "Departamento creado exitosamente");
            loadDepartamentos();
            limpiarFormulario();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en formato numérico", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (HeadlessException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarDepartamento() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un departamento para actualizar");
            return;
        }
        
        try {
            String id = txtId.getText().trim();
            Departamento dept = sistemaRH.buscarDepartamento(id);
            
            if (dept != null) {
                dept.setNombre(txtNombre.getText().trim());
                dept.setDescripcion(txtDescripcion.getText().trim());
                dept.setPresupuesto(Double.parseDouble(txtPresupuesto.getText().trim()));
                dept.setUbicacion(txtUbicacion.getText().trim());
                
                JOptionPane.showMessageDialog(this, "Departamento actualizado exitosamente");
                loadDepartamentos();
            }
            
        } catch (HeadlessException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarDepartamento() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un departamento para eliminar");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de eliminar este departamento?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String id = txtId.getText().trim();
                sistemaRH.eliminarDepartamento(id);
                JOptionPane.showMessageDialog(this, "Departamento eliminado exitosamente");
                loadDepartamentos();
                limpiarFormulario();
            } catch (NotFoundException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadDepartamentos() {
        tableModel.setRowCount(0);
        List<Departamento> departamentos = new ArrayList<>(sistemaRH.listarDepartamentos());
        
        for (Departamento dept : departamentos) {
            Object[] row = {
                dept.getId(),
                dept.getNombre(),
                dept.getDescripcion(),
                String.format("$%.2f", dept.getPresupuesto()),
                dept.getEmpleados().size()
            };
            tableModel.addRow(row);
        }
    }
    
    private void loadSelectedDepartamento() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            Departamento dept = sistemaRH.buscarDepartamento(id);
            
            if (dept != null) {
                txtId.setText(dept.getId());
                txtNombre.setText(dept.getNombre());
                txtDescripcion.setText(dept.getDescripcion());
                txtPresupuesto.setText(String.valueOf(dept.getPresupuesto()));
                txtUbicacion.setText(dept.getUbicacion());
            }
        }
    }
    
    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtPresupuesto.setText("");
        txtUbicacion.setText("");
        table.clearSelection();
    }
}
