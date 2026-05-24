package test.modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.Adulto;
import modelo.Estado;

public class EstadoTestGestionarNacimiento{
	Estado estado;

	@BeforeEach
	void beforeEach(){
		estado = new Estado();
		
		for(int i = 0; i < 10; i++){
			estado.getSectorTrabajadores().offerLast(new Adulto(20,80,100,1000));
		}
		
		estado.defuncionesPeriodoActual = 2; 
	}

	@Test
	void testGestionarNacimientoAumentaMenores(){
	
		estado.gestionarNacimiento(1400);
		assertEquals(6, estado.getMenores().size());
	}

	@Test
	void testGestionarNacimientoSoloReemplazaDefunciones(){
	
		estado.gestionarNacimiento(1000);
		assertEquals(2, estado.getMenores().size());
	}
}