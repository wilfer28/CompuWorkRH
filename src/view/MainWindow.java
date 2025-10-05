/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

/**
 *
 * @author Wilfer
 */


import service.SistemaRH;
import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal del sistema CompuWork
 * Proporciona acceso a todos los módulos del sistema
 */
public class MainWindow extends JFrame {
    private final SistemaRH sistemaRH;
    
    public MainWindow() {
        this.sistemaRH = new SistemaRH();
        initComponents();
    }
    
    private void initComponents() {
        // Configuración de la ventana
        setTitle("CompuWork - Sistema de Gestión de RRHH");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal con BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel superior con título
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panel central con botones de módulos
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Panel inferior con información
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(41, 128, 185));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        
        JLabel titleLabel = new JLabel("Sistema de Gestión de Recursos Humanos");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        panel.add(titleLabel);
        return panel;
    }
    
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Botón Gestión de Empleados
        JButton btnEmpleados = createModuleButton(
            "Gestión de Empleados",
            "Crear, modificar y eliminar empleados",
            new Color(52, 152, 219)
        );
        btnEmpleados.addActionListener(e -> openEmpleadosWindow());
        
        // Botón Gestión de Departamentos
        JButton btnDepartamentos = createModuleButton(
            "Gestión de Departamentos",
            "Administrar departamentos de la empresa",
            new Color(46, 204, 113)
        );
        btnDepartamentos.addActionListener(e -> openDepartamentosWindow());
        
        // Botón Asignación de Empleados
        JButton btnAsignacion = createModuleButton(
            "Asignar Empleados",
            "Asignar empleados a departamentos",
            new Color(155, 89, 182)
        );
        btnAsignacion.addActionListener(e -> openAsignacionWindow());
        
        // Botón Reportes Individuales
        JButton btnReportesInd = createModuleButton(
            "Reportes Individuales",
            "Generar reportes de desempeño individual",
            new Color(230, 126, 34)
        );
        btnReportesInd.addActionListener(e -> openReportesIndividualesWindow());
        
        // Botón Reportes Departamentales
        JButton btnReportesDept = createModuleButton(
            "Reportes Departamentales",
            "Generar reportes por departamento",
            new Color(231, 76, 60)
        );
        btnReportesDept.addActionListener(e -> openReportesDepartamentalesWindow());
        
        // Botón Salir
        JButton btnSalir = createModuleButton(
            "Salir",
            "Cerrar la aplicación",
            new Color(149, 165, 166)
        );
        btnSalir.addActionListener(e -> System.exit(0));
        
        panel.add(btnEmpleados);
        panel.add(btnDepartamentos);
        panel.add(btnAsignacion);
        panel.add(btnReportesInd);
        panel.add(btnReportesDept);
        panel.add(btnSalir);
        
        return panel;
    }
    
    private JButton createModuleButton(String title, String description, Color color) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel descLabel = new JLabel(description, SwingConstants.CENTER);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(Color.WHITE);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        textPanel.setOpaque(false);
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        textPanel.add(titleLabel);
        textPanel.add(descLabel);
        
        button.add(textPanel, BorderLayout.CENTER);
        
        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JLabel footerLabel = new JLabel("CompuWork © 2025 - Universidad Digital de Antioquia");
        footerLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        footerLabel.setForeground(Color.GRAY);
        
        panel.add(footerLabel);
        return panel;
    }
    
    // Métodos para abrir las ventanas de cada módulo
    private void openEmpleadosWindow() {
        EmpleadosWindow window = new EmpleadosWindow(sistemaRH);
        window.setVisible(true);
    }
    
    private void openDepartamentosWindow() {
        DepartamentosWindow window = new DepartamentosWindow(sistemaRH);
        window.setVisible(true);
    }
    
    private void openAsignacionWindow() {
        AsignacionWindow window = new AsignacionWindow(sistemaRH);
        window.setVisible(true);
    }
    
    private void openReportesIndividualesWindow() {
        ReportesIndividualesWindow window = new ReportesIndividualesWindow(sistemaRH);
        window.setVisible(true);
    }
    
    private void openReportesDepartamentalesWindow() {
        ReportesDepartamentalesWindow window = new ReportesDepartamentalesWindow(sistemaRH);
        window.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}