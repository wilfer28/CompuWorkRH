/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
/**
 *
 * @author Wilfer
 */
/**
 * Empleado temporal con fecha de fin de contrato
 */
public class EmpleadoTemporal extends Empleado {
    private LocalDate fechaFinContrato;
    private final String proyectoAsignado;

    public EmpleadoTemporal(String id, String nombre, String apellido, String email, 
                           String telefono, LocalDate fechaIngreso, double salarioBase,
                           LocalDate fechaFinContrato, String proyectoAsignado) {
        super(id, nombre, apellido, email, telefono, fechaIngreso, salarioBase);
        this.fechaFinContrato = fechaFinContrato;
        this.proyectoAsignado = proyectoAsignado;
    }

    /**
     * Valida si el contrato está vigente
     * @return 
     */
    public boolean validarContrato() {
        return LocalDate.now().isBefore(fechaFinContrato) || 
               LocalDate.now().isEqual(fechaFinContrato);
    }

    /**
     * Renueva el contrato hasta una nueva fecha
     * @param nuevaFecha
     */
    public void renovarContrato(LocalDate nuevaFecha) {
        if (nuevaFecha.isAfter(LocalDate.now())) {
            this.fechaFinContrato = nuevaFecha;
            System.out.println("Contrato renovado hasta: " + nuevaFecha);
        } else {
            System.out.println("Error: La nueva fecha debe ser posterior a hoy");
        }
    }

    @Override
    public double calcularSalario() {
        // Bono por días restantes de contrato
        double bonoContrato = 0;
        if (validarContrato()) {
            long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), fechaFinContrato);
            if (diasRestantes > 30) {
                bonoContrato = 100; // Bono fijo si faltan más de 30 días
            }
        }
        return getSalarioBase() + bonoContrato;
    }

    // Getters
    public LocalDate getFechaFinContrato() { return fechaFinContrato; }
    public String getProyectoAsignado() { return proyectoAsignado; }

    @Override
    public String toString() {
        return super.toString() + String.format(" - Temporal (Fin: %s, Proyecto: %s)", 
                fechaFinContrato, proyectoAsignado);
    }

    public void setFechaFinContrato(LocalDate parse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setProyectoAsignado(String trim) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}