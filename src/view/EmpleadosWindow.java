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
import java.time.LocalDate;
import java.util.List;

/**
 * Ventana para la gestión de empleados
 * Permite crear, modificar, eliminar y listar empleados
 */
public class EmpleadosWindow extends JFrame {
    private final SistemaRH sistemaRH;
    private JTable table;
    private DefaultTableModel tableModel;
    
    // Componentes del formulario
    private JTextField txtId, txtNombre, txtApellido, txtEmail, txtTelefono, txtSalario;
    private JComboBox<String> cmbTipo;
    private JTextField txtBeneficios, txtVacaciones; // Para permanentes
    private JTextField txtFechaFin, txtProyecto; // Para temporales
    private JPanel panelPermanente, panelTemporal;
    
    public EmpleadosWindow(SistemaRH sistemaRH) {
        this.sistemaRH = sistemaRH;
        initComponents();
        loadEmpleados();
    }
    
    private void initComponents() {
        setTitle("Gestión de Empleados");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panel superior con título
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 152, 219));
        JLabel titleLabel = new JLabel("Gestión de Empleados");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel central dividido en formulario y tabla
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        
        // Panel de formulario
        JPanel formPanel = createFormPanel();
        splitPane.setLeftComponent(new JScrollPane(formPanel));
        
        // Panel de tabla
        JPanel tablePanel = createTablePanel();
        splitPane.setRightComponent(tablePanel);
        
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        // Panel inferior con botones
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Empleado"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        int row = 0;
        
        // ID
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("ID:"), gbc);
        gbc.gridx = 1;
        txtId = new JTextField(15);
        panel.add(txtId, gbc);
        
        // Nombre
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        txtNombre = new JTextField(15);
        panel.add(txtNombre, gbc);
        
        // Apellido
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Apellido:"), gbc);
        gbc.gridx = 1;
        txtApellido = new JTextField(15);
        panel.add(txtApellido, gbc);
        
        // Email
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        txtEmail = new JTextField(15);
        panel.add(txtEmail, gbc);
        
        // Teléfono
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Teléfono:"), gbc);
        gbc.gridx = 1;
        txtTelefono = new JTextField(15);
        panel.add(txtTelefono, gbc);
        
        // Salario
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Salario:"), gbc);
        gbc.gridx = 1;
        txtSalario = new JTextField(15);
        panel.add(txtSalario, gbc);
        
        // Tipo de Empleado
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        cmbTipo = new JComboBox<>(new String[]{"Permanente", "Temporal"});
        cmbTipo.addActionListener(e -> toggleTipoEmpleadoPanels());
        panel.add(cmbTipo, gbc);
        
        // Panel para empleado permanente
        row++;
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        panelPermanente = createPermanentePanel();
        panel.add(panelPermanente, gbc);
        
        // Panel para empleado temporal
        gbc.gridy = row;
        panelTemporal = createTemporalPanel();
        panelTemporal.setVisible(false);
        panel.add(panelTemporal, gbc);
        
        return panel;
    }
    
    private JPanel createPermanentePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos Empleado Permanente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Beneficios:"), gbc);
        gbc.gridx = 1;
        txtBeneficios = new JTextField(15);
        panel.add(txtBeneficios, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Vacaciones:"), gbc);
        gbc.gridx = 1;
        txtVacaciones = new JTextField(15);
        panel.add(txtVacaciones, gbc);
        
        return panel;
    }
    
    private JPanel createTemporalPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos Empleado Temporal"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Fecha Fin (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        txtFechaFin = new JTextField(15);
        panel.add(txtFechaFin, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Proyecto:"), gbc);
        gbc.gridx = 1;
        txtProyecto = new JTextField(15);
        panel.add(txtProyecto, gbc);
        
        return panel;
    }
    
    private void toggleTipoEmpleadoPanels() {
        boolean isPermanente = cmbTipo.getSelectedItem().equals("Permanente");
        panelPermanente.setVisible(isPermanente);
        panelTemporal.setVisible(!isPermanente);
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Empleados"));
        
        String[] columns = {"ID", "Nombre", "Apellido", "Email", "Tipo", "Salario"};
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
                loadSelectedEmpleado();
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
        btnCrear.addActionListener(e -> crearEmpleado());
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.addActionListener(e -> actualizarEmpleado());
        
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.addActionListener(e -> eliminarEmpleado());
        
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
    
    private void crearEmpleado() {
        try {
            String id = txtId.getText().trim();
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String email = txtEmail.getText().trim();
            String telefono = txtTelefono.getText().trim();
            double salario = Double.parseDouble(txtSalario.getText().trim());
            
            if (id.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
                throw new IllegalArgumentException("Los campos obligatorios no pueden estar vacíos");
            }
            
            Empleado empleado;
            
            if (cmbTipo.getSelectedItem().equals("Permanente")) {
                double beneficios = Double.parseDouble(txtBeneficios.getText().trim());
                int vacaciones = Integer.parseInt(txtVacaciones.getText().trim());
                
                empleado = new EmpleadoPermanente(
                    id, nombre, apellido, email, telefono, 
                    LocalDate.now(), salario, beneficios, vacaciones
                );
            } else {
                LocalDate fechaFin = LocalDate.parse(txtFechaFin.getText().trim());
                String proyecto = txtProyecto.getText().trim();
                
                empleado = new EmpleadoTemporal(
                    id, nombre, apellido, email, telefono,
                    LocalDate.now(), salario, fechaFin, proyecto
                );
            }
            
            sistemaRH.agregarEmpleado(empleado);
            JOptionPane.showMessageDialog(this, "Empleado creado exitosamente");
            loadEmpleados();
            limpiarFormulario();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en formato numérico", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (HeadlessException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void actualizarEmpleado() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado para actualizar");
            return;
        }
        
        try {
            String id = txtId.getText().trim();
            Empleado empleado = sistemaRH.buscarEmpleado(id);
            
            if (empleado != null) {
                empleado.setNombre(txtNombre.getText().trim());
                empleado.setApellido(txtApellido.getText().trim());
                empleado.setEmail(txtEmail.getText().trim());
                empleado.setTelefono(txtTelefono.getText().trim());
                empleado.setSalario(Double.parseDouble(txtSalario.getText().trim()));
                
                switch (empleado) {
                    case EmpleadoPermanente emp -> {
                        emp.setBeneficios(Double.parseDouble(txtBeneficios.getText().trim()));
                        emp.setVacacionesAcumuladas(Integer.parseInt(txtVacaciones.getText().trim()));
                    }
                    case EmpleadoTemporal emp -> {
                        emp.setFechaFinContrato(LocalDate.parse(txtFechaFin.getText().trim()));
                        emp.setProyectoAsignado(txtProyecto.getText().trim());
                    }
                    default -> {
                    }
                }
                
                JOptionPane.showMessageDialog(this, "Empleado actualizado exitosamente");
                loadEmpleados();
            }
            
        } catch (HeadlessException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarEmpleado() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un empleado para eliminar");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de eliminar este empleado?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String id = txtId.getText().trim();
                sistemaRH.eliminarEmpleado(id);
                JOptionPane.showMessageDialog(this, "Empleado eliminado exitosamente");
                loadEmpleados();
                limpiarFormulario();
            } catch (NotFoundException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadEmpleados() {
        tableModel.setRowCount(0);
        List<Empleado> empleados = new ArrayList<>(sistemaRH.listarEmpleados());
        
        for (Empleado emp : empleados) {
            String tipo = emp instanceof EmpleadoPermanente ? "Permanente" : "Temporal";
            Object[] row = {
                emp.getId(),
                emp.getNombre(),
                emp.getApellido(),
                emp.getEmail(),
                tipo,
                String.format("$%.2f", emp.calcularSalario())
            };
            tableModel.addRow(row);
        }
    }
    
    private void loadSelectedEmpleado() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String id = (String) tableModel.getValueAt(selectedRow, 0);
            Empleado emp = sistemaRH.buscarEmpleado(id);
            
            if (emp != null) {
                txtId.setText(emp.getId());
                txtNombre.setText(emp.getNombre());
                txtApellido.setText(emp.getApellido());
                txtEmail.setText(emp.getEmail());
                txtTelefono.setText(emp.getTelefono());
                txtSalario.setText(String.valueOf(emp.getSalario()));
                
                switch (emp) {
                    case EmpleadoPermanente empPerm -> {
                        cmbTipo.setSelectedItem("Permanente");
                        txtBeneficios.setText(String.valueOf(empPerm.getBeneficios()));
                        txtVacaciones.setText(String.valueOf(empPerm.getVacacionesAcumuladas()));
                    }
                    case EmpleadoTemporal empTemp -> {
                        cmbTipo.setSelectedItem("Temporal");
                        txtFechaFin.setText(empTemp.getFechaFinContrato().toString());
                        txtProyecto.setText(empTemp.getProyectoAsignado());
                    }
                    default -> {
                    }
                }
            }
        }
    }
    
    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtSalario.setText("");
        txtBeneficios.setText("");
        txtVacaciones.setText("");
        txtFechaFin.setText("");
        txtProyecto.setText("");
        table.clearSelection();
    }
}