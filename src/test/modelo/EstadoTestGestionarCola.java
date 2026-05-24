package test.modelo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.Adulto;
import modelo.Estado;

class EstadoTestGestionarCola {
	Estado estado;

	@BeforeEach
	void beforeEach() {
		estado = new Estado();
		for (int i = 0; i < 8; i++) {
			Adulto parado = new Adulto(20, 70, 100, 1000);
			parado.setPeriodosEnEstado(5); 
			estado.getSectorParados().offerLast(parado);
			
			Adulto trabajador = new Adulto(20, 80, 100, 1000);
			trabajador.setPeriodosEnEstado(3);
			estado.getSectorTrabajadores().offerLast(trabajador);
		}
	}

	@Test
	void testGestionarEmpleosContratarYResetearAntiguedad() {
		
		estado.gestionarEmpleos(1200); 
		
		assertEquals(4, estado.getParados().size());
		assertEquals(12, estado.getTrabajadores().size());
		
		
		boolean hayNuevos = false;
		for(Adulto t : estado.getTrabajadores()) {
			if(t.getPeriodosEnEstado() == 0) hayNuevos = true;
		}
		assertTrue(hayNuevos);
	}

	@Test
	void testGestionarEmpleosFaltanParados() {
	
		estado.gestionarEmpleos(2000); 
		
		assertEquals(0, estado.getParados().size());
		assertEquals(16, estado.getTrabajadores().size());
	}
	
	@Test
	void testGestionarEmpleosDespedir() {
				estado.gestionarEmpleos(600); 
		
		assertEquals(10, estado.getParados().size());
		assertEquals(6, estado.getTrabajadores().size());
	}
}