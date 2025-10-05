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
 *
 * @author Wilfer
 */

/**
 * Servicio principal para la gestión del sistema de recursos humanos
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
     * @param empleado
     */
    public void crearEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado no puede ser nulo");
        }
        empleados.put(empleado.getId(), empleado);
        System.out.println("Empleado creado: " + empleado.getNombre() + " " + empleado.getApellido());
    }

    /**
     * Actualiza un empleado existente
     * @param id
     * @param empleadoActualizado
     * @throws exceptions.NotFoundException
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
     * @param id
     * @return 
     * @throws exceptions.NotFoundException
     */
    public Empleado obtenerEmpleado(String id) throws NotFoundException {
        Empleado empleado = empleados.get(id);
        if (empleado == null) {
            throw new NotFoundException("Empleado con ID " + id + " no encontrado");
        }
        return empleado;
    }

    /**
     * Elimina un empleado del sistema
     * @param id
     * @throws exceptions.NotFoundException
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
     * @return 
     */
    public Collection<Empleado> listarEmpleados() {
        return Collections.unmodifiableCollection(empleados.values());
    }

    // ================ GESTIÓN DE DEPARTAMENTOS ================

    /**
     * Crea un nuevo departamento
     * @param departamento
     */
    public void crearDepartamento(Departamento departamento) {
        if (departamento == null) {
            throw new IllegalArgumentException("El departamento no puede ser nulo");
        }
        departamentos.put(departamento.getId(), departamento);
        System.out.println("Departamento creado: " + departamento.getNombre());
    }

    /**
     * Obtiene un departamento por su ID
     * @param id
     * @return 
     * @throws exceptions.NotFoundException
     */
    public Departamento obtenerDepartamento(String id) throws NotFoundException {
        Departamento departamento = departamentos.get(id);
        if (departamento == null) {
            throw new NotFoundException("Departamento con ID " + id + " no encontrado");
        }
        return departamento;
    }

    /**
     * Elimina un departamento del sistema
     * @param id
     * @throws exceptions.NotFoundException
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
     * @return 
     */
    public Collection<Departamento> listarDepartamentos() {
        return Collections.unmodifiableCollection(departamentos.values());
    }

    // ================ ASIGNACIONES ================

    /**
     * Asigna un empleado a un departamento
     * @param idEmpleado
     * @param idDepartamento
     * @throws exceptions.NotFoundException
     * @throws exceptions.AssignmentException
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
     * @param idEmpleado
     * @param idDepartamento
     * @throws exceptions.NotFoundException
     * @throws exceptions.AssignmentException
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
     * @param reporte
     * @return 
     * @throws exceptions.NotFoundException
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
     * Obtiene todos los reportes de un empleado
     * @param idEmpleado
     * @return 
     */
    public List<ReporteDesempeno> obtenerReportesDeEmpleado(String idEmpleado) {
        return reportesPorEmpleado.getOrDefault(idEmpleado, Collections.emptyList());
    }

    /**
     * Genera reporte consolidado de un departamento
     * @param idDepartamento
     * @param periodo
     * @return 
     * @throws exceptions.NotFoundException 
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
     * @param reporte
     * @param rutaArchivo
     * @throws exceptions.NotFoundException
     * @throws exceptions.ReportGenerationException
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

    public Departamento buscarDepartamento(String deptId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public List<ReporteDesempeno> listarReportes() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void agregarEmpleados(Empleado empleado) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void agregarEmpleado(Empleado empleado) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Empleado buscarEmpleado(String id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void agregarReporte(ReporteDesempeno reporte) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void eliminarReporte(String reporteId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void agregarDepartamento(Departamento dept) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}