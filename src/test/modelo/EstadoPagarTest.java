package test.modelo;

import static modelo.TipoPago.menor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.PriorityQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelo.Adulto;
import modelo.Estado;
import modelo.Menor;
import modelo.Ser;
import modelo.TipoPago;
import modelo.OM.SeresManager;

class EstadoPagarTest {
	Estado estado;
	SeresManager seresManager = new SeresManager();
	int cantidadPersonas= 10;
	double cantidadTotalPagoAlSector;
	double factorDesarrollo = 5.55;
	AbstractCollection<Menor> menores;
	AbstractCollection<Adulto> trabajadores;
	AbstractCollection<Adulto> parados;
	AbstractCollection<Ser> ancianos;

	
	
	
	
	@BeforeEach
	void before() {
		estado = new Estado();
		menores = estado.getMenores();
		menores.addAll(seresManager.getMenores(cantidadPersonas));
		
		trabajadores = estado.getTrabajadores();
		trabajadores.addAll(seresManager.getAdultos(cantidadPersonas));
		
		parados = estado.getParados();
		parados.addAll(seresManager.getAdultos(cantidadPersonas));
		
		ancianos = estado.getAncianos();
		ancianos.addAll(seresManager.getAncianos(cantidadPersonas));
	}
	/*@Test
	void testCerrarPeriodo10MenoresCapitalSuficiente() {
		cantidadTotalPagoAlSector=menores.size()*menor.getNecesidadVital();
		estado.setCapital(cantidadTotalPagoAlSector);
		estado.cerrarPeriodo();
		assertEquals(0, estado.getCapital(),.1);
		for (Menor menor : menores) {
			assertEquals(factorDesarrollo, menor.getFactorDesarrollo(),.1);
		}
	}*/
	
	@Test
	void testCerrarPeriodoCapitalSuficiente() {
				double necesidadMenores = estado.getMenores().size() * TipoPago.menor.getNecesidadVital();
				double necesidadTrabajadores = estado.getTrabajadores().size() * TipoPago.trabajador.getNecesidadVital();
				double necesidadParados = estado.getParados().size() * TipoPago.parado.getNecesidadVital();
				double necesidadAncianos = estado.getAncianos().size() * TipoPago.anciano.getNecesidadVital();
		double gastoTotal = necesidadMenores + necesidadTrabajadores + necesidadParados + necesidadAncianos;
		
		double ingresosAutomaticos = estado.getTrabajadores().size() * 100;
		
		estado.setCapital(gastoTotal - ingresosAutomaticos);
		estado.cerrarPeriodo();
		assertEquals(0, estado.getCapital(), 0.1);
			for (Menor menor : menores) {
				assertEquals(factorDesarrollo, menor.getFactorDesarrollo(),.1);
				}
				for (Ser anciano : estado.getAncianos()) {
					assertEquals(90, anciano.getEsperanzaVida(), 0.1);
				}
				
				for (Adulto parado : estado.getParados()) {
					assertEquals(90, parado.getEsperanzaVida(), 0.1);
				}
	}
//
//		@Test
//		void testCerrarPeriodo10MenoresCapitalINNSuficiente() {
//			// que pasa si el capital es menor?
//			double reduccion = .5;
//			cantidadTotalPagoAlSector = menores.size() * menor.getNecesidadVital();
//			estado.setCapital(cantidadTotalPagoAlSector * reduccion);
//			estado.cerrarPeriodo();
//			// La prueba
//			double factorDesarrollo = this.factorDesarrollo * reduccion;
//			for (Menor menor : menores) {
//				assertEquals(factorDesarrollo, menor.getFactorDesarrollo(), 0.1);
//			}
//			assertEquals(0, estado.getCapital(),.1);
//		}	
	
	@Test
	void testCerrarPeriodoCapitalInsuficienteParaAncianos() {
		
		double pagoTrabajadores = estado.getTrabajadores().size() * TipoPago.trabajador.getPago();
		double pagoParados = estado.getParados().size() * TipoPago.parado.getPago();
		double pagoPrioritario = pagoTrabajadores + pagoParados;
		
		double ingresosAutomaticos = estado.getTrabajadores().size() * 100;
		estado.setCapital(pagoPrioritario - ingresosAutomaticos);
		
		estado.cerrarPeriodo();
		
		
		assertEquals(-300, estado.getCapital(), 0.1);
		
	
		for (Ser anciano : estado.getAncianos()) {
			assertTrue(anciano.getEsperanzaVida() < 90, "La vida del anciano debería haber bajado");
		}
	
	}
	@Test
	void testCerrarPeriodoCrisisParadosConAhorros() {
				Adulto paradoMuestra = (Adulto) estado.getParados().iterator().next();
		double ahorrosAntes = paradoMuestra.getAhorros(); 
		double vidaAntes = paradoMuestra.getEsperanzaVida(); 
		
		double pagoTrabajadores = estado.getTrabajadores().size() * TipoPago.trabajador.getPago();
		double pagoParados = estado.getParados().size() * TipoPago.parado.getPago();
		
	
		double presupuestoRecortado = pagoTrabajadores + (pagoParados / 2);
		double ingresosAutomaticos = estado.getTrabajadores().size() * 100;
		
		estado.setCapital(presupuestoRecortado - ingresosAutomaticos);
		estado.cerrarPeriodo();
		

		assertTrue(paradoMuestra.getAhorros() < ahorrosAntes, "El parado debería haber gastado ahorros");
		
	
		assertEquals(vidaAntes, paradoMuestra.getEsperanzaVida(), 0.1, "La vida del adulto no debe bajar");
	}
}
	
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		@Test
//		void testCerrarPeriodo10MenoresCapitalSINSupervivencia() {
//			double reduccion = .44;
//			cantidadTotalPagoAlSector = menores.size() * menor.getNecesidadVital();
//			double capital = cantidadTotalPagoAlSector * reduccion;
//			int infancia=18;
//			//envejecer hasta que dejan de ser menores
//			for (int i = 0; i < infancia; i++) {
//				estado.setCapital(capital);
//				estado.cerrarPeriodo();
//			}
//			double factorSuperVivencia=45;
//			for (Menor menor : menores) {
//				assertEquals(factorSuperVivencia,menor.getFactorDesarrollo(),.1);
//			}
//		}

