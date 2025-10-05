/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;
import model.Departamento;
import model.EmpleadoPermanente;
import model.EmpleadoTemporal;

/**
 *
 * @author Wilfer
 */

/**
 * Pruebas unitarias para la clase Departamento
 */
import exceptions.AssignmentException;
import java.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DepartamentoTest {
    
    private Departamento departamento;
    private EmpleadoPermanente empleado1;
    private EmpleadoTemporal empleado2;
    
    @Before
    public void setUp() {
        departamento = new Departamento(
            "D001",
            "Desarrollo de Software",
            "Departamento encargado del desarrollo de aplicaciones",
            50000000.0,
            "Piso 3, Edificio A"
        );
        
        empleado1 = new EmpleadoPermanente(
            "E001", "Juan", "Pérez", "juan@compuwork.com",
            "3001234567", LocalDate.now(), 3000000.0, 500000.0, 15
        );
        
        empleado2 = new EmpleadoTemporal(
            "E002", "María", "González", "maria@compuwork.com",
            "3109876543", LocalDate.now(), 2500000.0,
            LocalDate.now().plusMonths(6), "Proyecto Alpha"
        );
    }
    
    @After
    public void tearDown() {
        departamento = null;
        empleado1 = null;
        empleado2 = null;
    }
    
    @Test
    public void testCrearDepartamento() {
        assertNotNull("El departamento no debe ser null", departamento);
        assertEquals("D001", departamento.getId());
        assertEquals("Desarrollo de Software", departamento.getNombre());
        assertEquals(50000000.0, departamento.getPresupuesto(), 0.01);
    }
    
    @Test
    public void testAgregarEmpleado() throws AssignmentException {
        int cantidadInicial = departamento.getEmpleados().size();
        departamento.agregarEmpleado(empleado1);
        
        assertEquals("Debe haber un empleado más", 
                    cantidadInicial + 1, departamento.getEmpleados().size());
        assertTrue("El empleado debe estar en la lista", 
                  departamento.getEmpleados().contains(empleado1));
    }
    
    @Test
    public void testAgregarMultiplesEmpleados() throws AssignmentException {
        departamento.agregarEmpleado(empleado1);
        departamento.agregarEmpleado(empleado2);
        
        assertEquals("Debe haber 2 empleados", 2, departamento.getEmpleados().size());
    }
    
    @Test
    public void testRemoverEmpleado() throws AssignmentException {
        departamento.agregarEmpleado(empleado1);
        departamento.agregarEmpleado(empleado2);
        
        departamento.removerEmpleado(empleado1);
        
        assertEquals("Debe quedar 1 empleado", 1, departamento.getEmpleados().size());
        assertFalse("El empleado1 no debe estar en la lista", 
                   departamento.getEmpleados().contains(empleado1));
        assertTrue("El empleado2 debe seguir en la lista", 
                  departamento.getEmpleados().contains(empleado2));
    }
    
    @Test
    public void testObtenerEmpleados() throws AssignmentException {
        departamento.agregarEmpleado(empleado1);
        departamento.agregarEmpleado(empleado2);
        
        assertNotNull("La lista de empleados no debe ser null", 
                     departamento.getEmpleados());
        assertEquals(2, departamento.getEmpleados().size());
    }
    
    @Test
    public void testActualizarDatosDepartamento() {
        departamento.setNombre("Tecnología e Innovación");
        departamento.setPresupuesto(60000000.0);
        departamento.setUbicacion("Piso 5, Edificio B");
        
        assertEquals("Tecnología e Innovación", departamento.getNombre());
        assertEquals(60000000.0, departamento.getPresupuesto(), 0.01);
        assertEquals("Piso 5, Edificio B", departamento.getUbicacion());
    }
    
    @Test
    public void testDepartamentoVacio() {
        Departamento deptVacio = new Departamento(
            "D002", "Marketing", "Departamento de marketing", 
            30000000.0, "Piso 2"
        );
        
        assertEquals("Un departamento nuevo debe tener 0 empleados", 
                    0, deptVacio.getEmpleados().size());
    }
    
    @Test(expected = AssignmentException.class) // ✅ corregido
    public void testAgregarEmpleadoDuplicado() throws AssignmentException {
        departamento.agregarEmpleado(empleado1);
        departamento.agregarEmpleado(empleado1); // Debe lanzar excepción
    }
    
    @Test
    public void testPresupuestoPositivo() {
        assertTrue("El presupuesto debe ser positivo", 
                  departamento.getPresupuesto() > 0);
    }
}
