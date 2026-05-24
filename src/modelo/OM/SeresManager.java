package modelo.OM;

import java.util.ArrayList;
import java.util.List;

import modelo.Adulto;
import modelo.Menor;
import modelo.Ser;
import modelo.TipoPago;

public class SeresManager {

	public List<Menor> getMenores(int cantidad) {
		ArrayList<Menor> menores = new ArrayList<>();
		double esperanzaVida = 90;
		for (int i = 0; i < cantidad; i++) {
			menores.add(new Menor(esperanzaVida, TipoPago.menor.getNecesidadVital()));
		}
		return menores;
	}

	public List<Adulto> getAdultos(int cantidad) {
		ArrayList<Adulto> adultos = new ArrayList<>();
		int edadActual = 30;  
		double esperanzaVida = 90;
		double necesidadVital = TipoPago.trabajador.getNecesidadVital();
		double ahorrosIniciales = 500;

		for (int i = 0; i < cantidad; i++) {
			adultos.add(new Adulto(edadActual, esperanzaVida, necesidadVital, ahorrosIniciales));
		}
		return adultos;
	}

	public List<Ser> getAncianos(int cantidad) {
		ArrayList<Ser> ancianos = new ArrayList<>();
		int edadActual = 70; 
		double esperanzaVida = 90;
		double necesidadVital = TipoPago.anciano.getNecesidadVital();

		for (int i = 0; i < cantidad; i++) {
			ancianos.add(new Ser(edadActual, esperanzaVida, necesidadVital));
		}
		return ancianos;
	}
}