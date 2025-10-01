/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.time.LocalDate;
import java.util.Objects;
/**
 *
 * @author Wilfer
 */
 public abstract class Empleado {
    private final String id;
    private String nombre;
    private String apellido;
    private String email;
    private String telefono;
    private final LocalDate fechaIngreso;
    private double salarioBase;

    public Empleado(String id, String nombre, String apellido, String email, 
                   String telefono, LocalDate fechaIngreso, double salarioBase) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.fechaIngreso = fechaIngreso;
        this.salarioBase = salarioBase;
    }

    // Método abstracto que debe ser implementado por las subclases
    public abstract double calcularSalario();

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public double getSalarioBase() { return salarioBase; }

    // Setters
    public void setSalarioBase(double salarioBase) { 
        this.salarioBase = salarioBase; 
    }

    /**
     * Obtiene información básica del empleado
     * @return 
     */
    public String obtenerInformacion() {
        return String.format("%s %s (ID: %s) - Email: %s - Tel: %s - Ingreso: %s",
                nombre, apellido, id, email, telefono, fechaIngreso);
    }

    /**
     * Actualiza los datos del empleado
     * @param nombre
     * @param apellido
     * @param email
     * @param telefono
     * @param salarioBase
     */
    public void actualizar(String nombre, String apellido, String email, 
                          String telefono, double salarioBase) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.salarioBase = salarioBase;
    }

    @Override
    public String toString() {
        return obtenerInformacion() + String.format(" - Salario Base: $%.2f", salarioBase);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Empleado)) return false;
        Empleado empleado = (Empleado) o;
        return Objects.equals(id, empleado.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}