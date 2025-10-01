/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app;
import exceptions.*;
import model.*;
import service.SistemaRH;
import java.time.LocalDate;
import java.util.List;
/**
 *
 * @author Wilfer
 */

/**
 * Clase principal para demostrar el funcionamiento del sistema CompuWork
 */
public class App {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE GESTIÓN DE RECURSOS HUMANOS - COMPUWORK ===\n");
        
        // Crear instancia del sistema
        SistemaRH sistema = new SistemaRH();
        
        try {
            // ================ DEMO COMPLETA ================
            demoDepartamentos(sistema);
            demoEmpleados(sistema);
            demoAsignaciones(sistema);
            demoReportes(sistema);
            demoEstadisticas(sistema);
            
        } catch (Exception e) {
            System.err.println("Error en la demostración: " + e.getMessage());
        }
        
        System.out.println("\n=== FIN DE LA DEMOSTRACIÓN ===");
    }

    private static void demoDepartamentos(SistemaRH sistema) {
        System.out.println("1. CREANDO DEPARTAMENTOS...");
        
        Departamento deptRH = new Departamento("D001", "Recursos Humanos", 
            "Gestiona el personal de la empresa", 1500000.0, "Edificio Principal - Piso 3");
        
        Departamento deptIT = new Departamento("D002", "Tecnología", 
            "Desarrollo de software y sistemas", 2500000.0, "Edificio Tecnológico - Piso 2");
            
        Departamento deptVentas = new Departamento("D003", "Ventas", 
            "Comercialización de productos y servicios", 2000000.0, "Edificio Principal - Piso 1");

        sistema.crearDepartamento(deptRH);
        sistema.crearDepartamento(deptIT);
        sistema.crearDepartamento(deptVentas);
        
        System.out.println("✅ Departamentos creados exitosamente\n");
    }

    private static void demoEmpleados(SistemaRH sistema) {
        System.out.println("2. CREANDO EMPLEADOS...");
        
        // Empleados permanentes
        Empleado ana = new EmpleadoPermanente("E001", "Ana", "García", 
            "ana.garcia@compuwork.com", "3001234567", 
            LocalDate.of(2020, 3, 15), 3500000.0, 0.15, 20);
            
        Empleado carlos = new EmpleadoPermanente("E002", "Carlos", "Rodríguez", 
            "carlos.rodriguez@compuwork.com", "3007654321", 
            LocalDate.of(2019, 6, 10), 4200000.0, 0.20, 25);

        // Empleados temporales
        Empleado sofia = new EmpleadoTemporal("E003", "Sofia", "López", 
            "sofia.lopez@compuwork.com", "3009876543", 
            LocalDate.of(2024, 8, 1), 2800000.0, 
            LocalDate.of(2025, 12, 31), "Proyecto Digital 2025");
            
        Empleado miguel = new EmpleadoTemporal("E004", "Miguel", "Hernández", 
            "miguel.hernandez@compuwork.com", "3005555555", 
            LocalDate.of(2024, 10, 15), 2500000.0, 
            LocalDate.of(2025, 6, 30), "Migración de Sistemas");

        sistema.crearEmpleado(ana);
        sistema.crearEmpleado(carlos);
        sistema.crearEmpleado(sofia);
        sistema.crearEmpleado(miguel);
        
        System.out.println("✅ Empleados creados exitosamente\n");
    }

    private static void demoAsignaciones(SistemaRH sistema) {
        System.out.println("3. ASIGNANDO EMPLEADOS A DEPARTAMENTOS...");
        
        try {
            sistema.asignarEmpleadoADepartamento("E001", "D001"); // Ana -> RH
            sistema.asignarEmpleadoADepartamento("E002", "D002"); // Carlos -> IT
            sistema.asignarEmpleadoADepartamento("E003", "D002"); // Sofia -> IT
            sistema.asignarEmpleadoADepartamento("E004", "D003"); // Miguel -> Ventas
            
            System.out.println("✅ Asignaciones completadas exitosamente\n");
            
        } catch (NotFoundException | AssignmentException e) {
            System.err.println("Error en asignaciones: " + e.getMessage());
        }
    }

    private static void demoReportes(SistemaRH sistema) {
        System.out.println("4. GENERANDO REPORTES DE DESEMPEÑO...");
        
        try {
            // Reporte para Ana (Excelente desempeño)
            ReporteDesempeno reporteAna = new ReporteDesempeno("R001", "E001", "2025-Q3", 
                0, "Excelente liderazgo en procesos de selección. Implementó nuevas políticas de bienestar.", 
                LocalDate.now());
            reporteAna.calcularPuntaje(95, 88, 92); // Objetivos, Competencias, Asistencia
            
            // Reporte para Carlos (Buen desempeño)
            ReporteDesempeno reporteCarlos = new ReporteDesempeno("R002", "E002", "2025-Q3", 
                0, "Desarrollo exitoso del nuevo sistema de gestión. Lideró migración a la nube.", 
                LocalDate.now());
            reporteCarlos.calcularPuntaje(85, 90, 88);
            
            // Reporte para Sofia (Desempeño satisfactorio)
            ReporteDesempeno reporteSofia = new ReporteDesempeno("R003", "E003", "2025-Q3", 
                0, "Adaptación rápida al equipo. Contribuyó significativamente al proyecto digital.", 
                LocalDate.now());
            reporteSofia.calcularPuntaje(78, 82, 85);

            sistema.generarReporteIndividual(reporteAna);
            sistema.generarReporteIndividual(reporteCarlos);
            sistema.generarReporteIndividual(reporteSofia);
            
            // Exportar reportes a archivos
            sistema.exportarReporte(reporteAna, "reporte_ana_q3_2025.txt");
            sistema.exportarReporte(reporteCarlos, "reporte_carlos_q3_2025.txt");
            sistema.exportarReporte(reporteSofia, "reporte_sofia_q3_2025.txt");
            
            System.out.println("✅ Reportes generados y exportados exitosamente\n");
            
        } catch (NotFoundException | ReportGenerationException e) {
            System.err.println("Error generando reportes: " + e.getMessage());
        }
    }

    private static void demoEstadisticas(SistemaRH sistema) {
        System.out.println("5. MOSTRANDO INFORMACIÓN DEL SISTEMA...");
        
        // Mostrar estadísticas generales
        sistema.mostrarEstadisticas();
        
        try {
            // Mostrar departamentos con sus empleados
            System.out.println("=== DEPARTAMENTOS Y EMPLEADOS ===");
            for (Departamento dept : sistema.listarDepartamentos()) {
                System.out.println(dept);
                for (Empleado emp : dept.obtenerEmpleados()) {
                    System.out.println("  └─ " + emp);
                }
                System.out.println();
            }
            
            // Mostrar reporte departamental de IT
            System.out.println("=== REPORTE DEPARTAMENTAL - TECNOLOGÍA (Q3 2025) ===");
            List<ReporteDesempeno> reportesIT = sistema.generarReporteDepartamental("D002", "2025-Q3");
            if (!reportesIT.isEmpty()) {
                double promedioIT = reportesIT.stream().mapToDouble(ReporteDesempeno::getPuntaje).average().orElse(0);
                System.out.println("Promedio de desempeño del departamento: " + String.format("%.2f", promedioIT));
                
                for (ReporteDesempeno reporte : reportesIT) {
                    System.out.println("  • " + reporte);
                }
            }
            
        } catch (NotFoundException e) {
            System.err.println("Error mostrando estadísticas: " + e.getMessage());
        }
    }
}