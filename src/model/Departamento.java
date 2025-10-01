/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import exceptions.AssignmentException;
import java.util.*;
/**
 *
 * @author Wilfer
 */
/**
 * Representa un departamento de la empresa
 */
public class Departamento {
    private final String id;
    private final String nombre;
    private final String descripcion;
    private final double presupuesto;
    private final String ubicacion;
    private final List<Empleado> empleados;

    public Departamento(String id, String nombre, String descripcion, 
                       double presupuesto, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.presupuesto = presupuesto;
        this.ubicacion = ubicacion;
        this.empleados = new ArrayList<>();
    }

    /**
     * Agrega un empleado al departamento
     * @param empleado
     * @throws exceptions.AssignmentException
     */
    public void agregarEmpleado(Empleado empleado) throws AssignmentException {
        if (empleado == null) {
            throw new AssignmentException("El empleado no puede ser nulo");
        }
        if (empleados.contains(empleado)) {
            throw new AssignmentException("El empleado ya está asignado a este departamento");
        }
        empleados.add(empleado);
    }

    /**
     * Remueve un empleado del departamento
     * @param empleado
     * @throws exceptions.AssignmentException
     */
    public void removerEmpleado(Empleado empleado) throws AssignmentException {
        if (empleado == null) {
            throw new AssignmentException("El empleado no puede ser nulo");
        }
        if (!empleados.remove(empleado)) {
            throw new AssignmentException("El empleado no pertenece a este departamento");
        }
    }

    /**
     * Obtiene una lista inmutable de empleados
     * @return 
     */
    public List<Empleado> obtenerEmpleados() {
        return Collections.unmodifiableList(empleados);
    }

    /**
     * Busca un empleado por ID dentro del departamento
     * @param idEmpleado
     * @return 
     */
    public Optional<Empleado> buscarEmpleadoPorId(String idEmpleado) {
        return empleados.stream()
                .filter(emp -> emp.getId().equals(idEmpleado))
                .findFirst();
    }

    // Getters
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPresupuesto() { return presupuesto; }
    public String getUbicacion() { return ubicacion; }
    public int getCantidadEmpleados() { return empleados.size(); }

    @Override
    public String toString() {
        return String.format("Departamento %s (%s) - Presupuesto: $%.2f - Ubicación: %s - Empleados: %d",
                nombre, id, presupuesto, ubicacion, empleados.size());
    }
}