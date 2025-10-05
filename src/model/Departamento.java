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
    private String nombre;
    private String descripcion;
    private double presupuesto;
    private String ubicacion;
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

    // Métodos para agregar y remover empleados
    public void agregarEmpleado(Empleado empleado) throws AssignmentException {
        if (empleado == null) {
            throw new AssignmentException("El empleado no puede ser nulo");
        }
        if (empleados.contains(empleado)) {
            throw new AssignmentException("El empleado ya está asignado a este departamento");
        }
        empleados.add(empleado);
    }

    public void removerEmpleado(Empleado empleado) throws AssignmentException {
        if (empleado == null) {
            throw new AssignmentException("El empleado no puede ser nulo");
        }
        if (!empleados.remove(empleado)) {
            throw new AssignmentException("El empleado no pertenece a este departamento");
        }
    }

    // ✅ Este es el que debes usar
    public List<Empleado> getEmpleados() {
        return empleados;
    }

    // También tienes este que devuelve lista inmutable (opcional usarlo)
    public List<Empleado> obtenerEmpleados() {
        return Collections.unmodifiableList(empleados);
    }

    // Buscar por ID
    public Optional<Empleado> buscarEmpleadoPorId(String idEmpleado) {
        return empleados.stream()
                .filter(emp -> emp.getId().equals(idEmpleado))
                .findFirst();
    }

    // Getters y setters básicos
    public String getId()
    { return id; }
    public String getNombre()
    { return nombre; }
    public String getDescripcion()
    { return descripcion; }
    public double getPresupuesto()
    { return presupuesto; }
    public String getUbicacion()
    { return ubicacion; }
    public int getCantidadEmpleados()
    { return empleados.size(); }

    @Override
    public String toString() {
        return String.format("Departamento %s (%s) - Presupuesto: $%.2f - Ubicación: %s - Empleados: %d",
                nombre, id, presupuesto, ubicacion, empleados.size());
    }

    // ✅ Setters implementados (si los necesitas en la GUI)
    public void setNombre(String nombre)
    { this.nombre = nombre; }
    public void setDescripcion(String descripcion)
    { this.descripcion = descripcion; }
    public void setUbicacion(String ubicacion)
    { this.ubicacion = ubicacion; }
    public void setPresupuesto(double presupuesto)
    { this.presupuesto = presupuesto; }
}
