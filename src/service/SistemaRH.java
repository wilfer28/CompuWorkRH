/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;
import exceptions.*;
import model.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio principal para la gestión del sistema de recursos humanos
 * @author Wilfer
 */
public class SistemaRH {
    private final Map<String, Empleado> empleados;
    private final Map<String, Departamento> departamentos;
    private final Map<String, List<ReporteDesempeno>> reportesPorEmpleado;

    public SistemaRH() {
        this.empleados = new HashMap<>();
        this.departamentos = new HashMap<>();
        this.reportesPorEmpleado = new HashMap<>();
    }

    // ================ GESTIÓN DE EMPLEADOS ================

    /**
     * Crea un nuevo empleado en el sistema
     */
    public void crearEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado no puede ser nulo");
        }
        empleados.put(empleado.getId(), empleado);
        System.out.println("Empleado creado: " + empleado.getNombre() + " " + empleado.getApellido());
    }

    /**
     * Agrega un empleado al sistema
     */
    public void agregarEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado no puede ser nulo");
        }
        empleados.put(empleado.getId(), empleado);
        System.out.println("Empleado agregado: " + empleado.getNombre() + " " + empleado.getApellido());
    }

    /**
     * Método alternativo para agregar empleado
     */
    public void agregarEmpleados(Empleado empleado) {
        agregarEmpleado(empleado);
    }

    /**
     * Actualiza un empleado existente
     */
    public void actualizarEmpleado(String id, Empleado empleadoActualizado) throws NotFoundException {
        if (!empleados.containsKey(id)) {
            throw new NotFoundException("Empleado con ID " + id + " no encontrado");
        }
        empleados.put(id, empleadoActualizado);
        System.out.println("Empleado actualizado: " + id);
    }

    /**
     * Obtiene un empleado por su ID
     */
    public Empleado obtenerEmpleado(String id) throws NotFoundException {
        Empleado empleado = empleados.get(id);
        if (empleado == null) {
            throw new NotFoundException("Empleado con ID " + id + " no encontrado");
        }
        return empleado;
    }

    /**
     * Busca un empleado por ID (sin lanzar excepción)
     */
    public Empleado buscarEmpleado(String id) {
        return empleados.get(id);
    }

    /**
     * Elimina un empleado del sistema
     */
    public void eliminarEmpleado(String id) throws NotFoundException {
        Empleado empleado = empleados.remove(id);
        if (empleado == null) {
            throw new NotFoundException("Empleado con ID " + id + " no encontrado");
        }
        
        // Remover de todos los departamentos
        departamentos.values().forEach(dept -> {
            try {
                dept.removerEmpleado(empleado);
            } catch (AssignmentException ignored) {
                // El empleado no estaba en este departamento
            }
        });
        
        // Remover reportes del empleado
        reportesPorEmpleado.remove(id);
        
        System.out.println("Empleado eliminado: " + empleado.getNombre() + " " + empleado.getApellido());
    }

    /**
     * Lista todos los empleados
     */
    public Collection<Empleado> listarEmpleados() {
        return Collections.unmodifiableCollection(empleados.values());
    }

    // ================ GESTIÓN DE DEPARTAMENTOS ================

    /**
     * Crea un nuevo departamento
     */
    public void crearDepartamento(Departamento departamento) {
        if (departamento == null) {
            throw new IllegalArgumentException("El departamento no puede ser nulo");
        }
        departamentos.put(departamento.getId(), departamento);
        System.out.println("Departamento creado: " + departamento.getNombre());
    }

    /**
     * Agrega un departamento al sistema
     */
    public void agregarDepartamento(Departamento dept) {
        if (dept == null) {
            throw new IllegalArgumentException("El departamento no puede ser nulo");
        }
        departamentos.put(dept.getId(), dept);
        System.out.println("Departamento agregado: " + dept.getNombre());
    }

    /**
     * Obtiene un departamento por su ID
     */
    public Departamento obtenerDepartamento(String id) throws NotFoundException {
        Departamento departamento = departamentos.get(id);
        if (departamento == null) {
            throw new NotFoundException("Departamento con ID " + id + " no encontrado");
        }
        return departamento;
    }

    /**
     * Busca un departamento por ID (sin lanzar excepción)
     */
    public Departamento buscarDepartamento(String deptId) {
        return departamentos.get(deptId);
    }

    /**
     * Elimina un departamento del sistema
     */
    public void eliminarDepartamento(String id) throws NotFoundException {
        Departamento departamento = departamentos.remove(id);
        if (departamento == null) {
            throw new NotFoundException("Departamento con ID " + id + " no encontrado");
        }
        System.out.println("Departamento eliminado: " + departamento.getNombre());
        System.out.println("Nota: Los empleados del departamento eliminado quedan sin asignar");
    }

    /**
     * Lista todos los departamentos
     */
    public Collection<Departamento> listarDepartamentos() {
        return Collections.unmodifiableCollection(departamentos.values());
    }

    // ================ ASIGNACIONES ================

    /**
     * Asigna un empleado a un departamento
     */
    public void asignarEmpleadoADepartamento(String idEmpleado, String idDepartamento) 
            throws NotFoundException, AssignmentException {
        Empleado empleado = obtenerEmpleado(idEmpleado);
        Departamento departamento = obtenerDepartamento(idDepartamento);
        
        departamento.agregarEmpleado(empleado);
        System.out.println("Empleado " + empleado.getNombre() + " asignado a " + departamento.getNombre());
    }

    /**
     * Remueve un empleado de un departamento
     */
    public void removerEmpleadoDeDepartamento(String idEmpleado, String idDepartamento) 
            throws NotFoundException, AssignmentException {
        Empleado empleado = obtenerEmpleado(idEmpleado);
        Departamento departamento = obtenerDepartamento(idDepartamento);
        
        departamento.removerEmpleado(empleado);
        System.out.println("Empleado " + empleado.getNombre() + " removido de " + departamento.getNombre());
    }

    // ================ REPORTES ================

    /**
     * Genera un reporte individual de desempeño
     */
    public ReporteDesempeno generarReporteIndividual(ReporteDesempeno reporte) throws NotFoundException {
        // Verificar que el empleado existe
        obtenerEmpleado(reporte.getEmpleadoId());
        
        // Agregar el reporte a la lista del empleado
        reportesPorEmpleado.computeIfAbsent(reporte.getEmpleadoId(), k -> new ArrayList<>())
                          .add(reporte);
        
        System.out.println("Reporte generado: " + reporte.getId());
        return reporte;
    }

    /**
     * Agrega un reporte al sistema
     */
    public void agregarReporte(ReporteDesempeno reporte) {
        if (reporte == null) {
            throw new IllegalArgumentException("El reporte no puede ser nulo");
        }
        reportesPorEmpleado.computeIfAbsent(reporte.getEmpleadoId(), k -> new ArrayList<>())
                          .add(reporte);
        System.out.println("Reporte agregado: " + reporte.getId());
    }

    /**
     * Obtiene todos los reportes de un empleado
     */
    public List<ReporteDesempeno> obtenerReportesDeEmpleado(String idEmpleado) {
        return reportesPorEmpleado.getOrDefault(idEmpleado, Collections.emptyList());
    }

    /**
     * Lista todos los reportes del sistema
     */
    public List<ReporteDesempeno> listarReportes() {
        List<ReporteDesempeno> todosLosReportes = new ArrayList<>();
        reportesPorEmpleado.values().forEach(todosLosReportes::addAll);
        return todosLosReportes;
    }

    /**
     * Elimina un reporte del sistema
     */
    public void eliminarReporte(String reporteId) {
        boolean encontrado = false;
        for (List<ReporteDesempeno> reportes : reportesPorEmpleado.values()) {
            encontrado = reportes.removeIf(r -> r.getId().equals(reporteId));
            if (encontrado) break;
        }
        if (encontrado) {
            System.out.println("Reporte eliminado: " + reporteId);
        } else {
            System.out.println("Reporte no encontrado: " + reporteId);
        }
    }

    /**
     * Genera reporte consolidado de un departamento
     */
    public List<ReporteDesempeno> generarReporteDepartamental(String idDepartamento, String periodo) 
            throws NotFoundException {
        Departamento departamento = obtenerDepartamento(idDepartamento);
        
        List<ReporteDesempeno> reportesDepartamento = new ArrayList<>();
        
        for (Empleado empleado : departamento.obtenerEmpleados()) {
            List<ReporteDesempeno> reportesEmpleado = reportesPorEmpleado.getOrDefault(
                empleado.getId(), Collections.emptyList());
            
            // Filtrar por período
            List<ReporteDesempeno> reportesPeriodo = reportesEmpleado.stream()
                .filter(reporte -> reporte.getPeriodo().equalsIgnoreCase(periodo))
                .collect(Collectors.toList());
                
            reportesDepartamento.addAll(reportesPeriodo);
        }
        
        System.out.println("Reportes encontrados para " + departamento.getNombre() + 
                          " en período " + periodo + ": " + reportesDepartamento.size());
        return reportesDepartamento;
    }

    /**
     * Exporta un reporte a archivo
     */
    public void exportarReporte(ReporteDesempeno reporte, String rutaArchivo) 
            throws NotFoundException, ReportGenerationException {
        Empleado empleado = obtenerEmpleado(reporte.getEmpleadoId());
        reporte.exportar(rutaArchivo, empleado);
    }

    // ================ UTILIDADES ================

    /**
     * Obtiene estadísticas generales del sistema
     */
    public void mostrarEstadisticas() {
        System.out.println("\n=== ESTADÍSTICAS DEL SISTEMA ===");
        System.out.println("Total de empleados: " + empleados.size());
        System.out.println("Total de departamentos: " + departamentos.size());
        
        long totalReportes = reportesPorEmpleado.values().stream()
                                               .mapToLong(List::size)
                                               .sum();
        System.out.println("Total de reportes: " + totalReportes);
        
        long empleadosPermanentes = empleados.values().stream()
                                            .filter(emp -> emp instanceof EmpleadoPermanente)
                                            .count();
        long empleadosTemporales = empleados.values().stream()
                                           .filter(emp -> emp instanceof EmpleadoTemporal)
                                           .count();
                                           
        System.out.println("Empleados permanentes: " + empleadosPermanentes);
        System.out.println("Empleados temporales: " + empleadosTemporales);
        System.out.println("===============================\n");
    }
}