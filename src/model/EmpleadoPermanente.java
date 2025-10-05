/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.time.LocalDate;
/**
 *
 * @author Wilfer
 */
public class EmpleadoPermanente extends Empleado {
    private final double beneficios; // porcentaje adicional del salario base
    private int vacacionesAcumuladas; // días de vacaciones

    public EmpleadoPermanente(String id, String nombre, String apellido, String email, 
                             String telefono, LocalDate fechaIngreso, double salarioBase,
                             double beneficios, int vacacionesAcumuladas) {
        super(id, nombre, apellido, email, telefono, fechaIngreso, salarioBase);
        this.beneficios = beneficios;
        this.vacacionesAcumuladas = vacacionesAcumuladas;
    }

    /**
     * Calcula el valor monetario de los beneficios
     * @return 
     */
    public double calcularBeneficios() {
        return getSalarioBase() * beneficios;
    }

    @Override
    public double calcularSalario() {
        return getSalarioBase() + calcularBeneficios();
    }

    /**
     * Gestiona las vacaciones del empleado
     * @param diasUsados
     */
    public void gestionarVacaciones(int diasUsados) {
        if (diasUsados <= vacacionesAcumuladas) {
            vacacionesAcumuladas -= diasUsados;
            System.out.println("Vacaciones procesadas. Días restantes: " + vacacionesAcumuladas);
        } else {
            System.out.println("No hay suficientes días de vacaciones acumulados.");
        }
    }

    // Getters y setters
    public double getBeneficios() { return beneficios; }
    public int getVacacionesAcumuladas() { return vacacionesAcumuladas; }
    public void setVacacionesAcumuladas(int vacacionesAcumuladas) { 
        this.vacacionesAcumuladas = vacacionesAcumuladas; 
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" - Permanente (Beneficios: %.2f%%, Vacaciones: %d días)", 
                beneficios * 100, vacacionesAcumuladas);
    }

    public void setBeneficios(double parseDouble) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}