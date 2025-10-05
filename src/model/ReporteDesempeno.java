/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import exceptions.ReportGenerationException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
/**
 *
 * @author Wilfer
 */

/**
 * Maneja los reportes de desempeño de empleados
 */
public class ReporteDesempeno {
    private final String id;
    private final String empleadoId;
    private final String periodo; // ej. "2025-Q1" o "Julio 2025"
    private double puntaje; // 0-100
    private final String comentarios;
    private final LocalDate fechaEvaluacion;

    public ReporteDesempeno(String id, String empleadoId, String periodo, 
                           double puntaje, String comentarios, LocalDate fechaEvaluacion) {
        this.id = id;
        this.empleadoId = empleadoId;
        this.periodo = periodo;
        this.puntaje = Math.max(0, Math.min(100, puntaje)); // Asegurar rango 0-100
        this.comentarios = comentarios;
        this.fechaEvaluacion = fechaEvaluacion;
    }

    /**
     * Calcula el puntaje basado en diferentes métricas
     * @param objetivos
     * @param competencias
     * @param asistencia
     * @return 
     */
    public double calcularPuntaje(double objetivos, double competencias, double asistencia) {
        // Ponderación: objetivos 50%, competencias 30%, asistencia 20%
        double total = (objetivos * 0.5) + (competencias * 0.3) + (asistencia * 0.2);
        this.puntaje = Math.max(0, Math.min(100, total));
        return this.puntaje;
    }

    /**
     * Genera el texto completo del reporte
     * @param empleado
     * @return 
     */
    public String generarReporte(Empleado empleado) {
        StringBuilder reporte = new StringBuilder();
        reporte.append("================================\n");
        reporte.append("    REPORTE DE DESEMPEÑO\n");
        reporte.append("================================\n\n");
        reporte.append("ID Reporte: ").append(id).append("\n");
        reporte.append("Empleado: ").append(empleado.obtenerInformacion()).append("\n");
        reporte.append("Período: ").append(periodo).append("\n");
        reporte.append("Fecha Evaluación: ").append(fechaEvaluacion).append("\n");
        reporte.append("Puntaje: ").append(String.format("%.2f/100", puntaje)).append("\n\n");
        reporte.append("Comentarios:\n").append(comentarios).append("\n\n");
        reporte.append("Clasificación: ").append(obtenerClasificacion()).append("\n");
        reporte.append("================================\n");
        
        return reporte.toString();
    }

    /**
     * Exporta el reporte a un archivo de texto
     * @param rutaArchivo
     * @param empleado
     * @throws exceptions.ReportGenerationException
     */
    public void exportar(String rutaArchivo, Empleado empleado) throws ReportGenerationException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            writer.write(generarReporte(empleado));
            System.out.println("Reporte exportado exitosamente a: " + rutaArchivo);
        } catch (IOException e) {
            throw new ReportGenerationException(
                "Error al exportar el reporte a " + rutaArchivo + ": " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene la clasificación basada en el puntaje
     */
    private String obtenerClasificacion() {
        if (puntaje >= 90) return "EXCELENTE";
        if (puntaje >= 80) return "BUENO";
        if (puntaje >= 70) return "SATISFACTORIO";
        if (puntaje >= 60) return "REGULAR";
        return "DEFICIENTE";
    }

    // Getters
    public String getId() { return id; }
    public String getEmpleadoId() { return empleadoId; }
    public String getPeriodo() { return periodo; }
    public double getPuntaje() { return puntaje; }
    public String getComentarios() { return comentarios; }
    public LocalDate getFechaEvaluacion() { return fechaEvaluacion; }

    @Override
    public String toString() {
        return String.format("Reporte %s - Empleado: %s - Período: %s - Puntaje: %.2f", 
                id, empleadoId, periodo, puntaje);
    }

    public String exportar() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}