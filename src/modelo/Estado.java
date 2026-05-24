package modelo;

import static modelo.TipoPago.anciano;
import static modelo.TipoPago.menor;
import static modelo.TipoPago.parado;
import static modelo.TipoPago.trabajador;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;

public class Estado {

	// atributos sobre desarrollo
	private double capital = 0;
	private double cantidadProducidaPorTrabajador = 100;
	private final double edadJubilacion = 65;
	private final double edadMadurez = 18;
	private final double necesidadVitalBase = 100;
	public int defuncionesPeriodoActual = 0;
	private ArrayList<Integer> historialDiferenciales = new ArrayList<>();

	// poblacion
	private Sector<Menor> menores;
	private Sector<Adulto> trabajadores;
	private Sector<Adulto> parados;
	private Sector<Ser> ancianos;

	// produccion
	private double totalDemandado = 0;

	public Estado() {
		super();
		menores = new SectorNoPrioritario<Menor>(menor);
		trabajadores = new SectorPrioritario<Adulto>(trabajador);
		ancianos = new SectorNoPrioritario<Ser>(anciano);
		parados = new SectorPrioritarioParados(parado);
	}

	public void abrirPeriodo(double porcentajeIncrementoDemanda) {

		//// 1 calcular la cantidad que debe producir el estado segun el incremento
		//// (puede
		///// // ser una cantidad menor)
		 double objetivoProduccion = calcularCantidadAProducir(0);

		// actualizar demanda total según el porcentaje indicado
		totalDemandado *= 1 + porcentajeIncrementoDemanda;

		// 2 Contratar o despedir trabajadores adultos
		// dependiendo de la producción necesaria
		 gestionarEmpleos(objetivoProduccion);

		// // 3 decidir los nacimientos en funcion de cuantas difunciones, y otras
		// cosas,
		 gestionarNacimiento(objetivoProduccion);
	}

	private double calcularCantidadAProducir(double incremento) {
		double produccionActual;
		produccionActual = trabajadores.size() * cantidadProducidaPorTrabajador;
		return produccionActual * (1 + incremento);
	}

	public void gestionarEmpleos(double objetivoProduccion) {
		int TrabajadoresNecesarios;
		TrabajadoresNecesarios = (int) (objetivoProduccion / cantidadProducidaPorTrabajador);
		int diferencia;
		diferencia = TrabajadoresNecesarios - trabajadores.size();
		// Me sobran trabajadores
		if (diferencia > 0) {
			for (int i = 0; i < diferencia && parados.size() > 0; i++) {
				
				Adulto contratado = parados.getFirst();
				contratado.setPeriodosEnEstado(0); 
				trabajadores.offerLast(contratado);
			}
		}
		// Me faltan trabajadores
		else {
			if (diferencia < 0) {
				diferencia = Math.abs(diferencia);
				for (int i = 0; i < diferencia; i++) {
					Adulto despedido = trabajadores.getFirst();
					despedido.setPeriodosEnEstado(0); 
					parados.offerLast(despedido);

				}
			}

		}

	}

	////////////////////////////////////////////////////

	public void cerrarPeriodo() {
		// 1 Calcular el capital
		double totalProducido = trabajadores.size() * cantidadProducidaPorTrabajador;
		this.capital += totalProducido;
		// 2 pagar a los seres
		pagar(trabajadores, parados, menores, ancianos);// he cambiado el orden de tr
		// Tendria q preguntarme si puedo pagarlo
		ArrayList<Ser> poblacion = new ArrayList<>();
		poblacion.addAll(menores.getMiembros());
		poblacion.addAll(trabajadores.getMiembros());
		poblacion.addAll(parados.getMiembros());
		poblacion.addAll(ancianos.getMiembros());
		envejecer(poblacion);
		jubila(parados.getMiembros(), trabajadores.getMiembros());
		enterrar(menores.getMiembros(), parados.getMiembros(), trabajadores.getMiembros(), ancianos.getMiembros());
	}

	private void pagar(Sector<? extends Ser>... sector) {
		double deficit = 0;
		for (Sector<? extends Ser> poblacion : sector) {
			double presupuestoMaximo = poblacion.getTotalPago();
			deficit = capital - presupuestoMaximo;
			double pagoReal = poblacion.pago(deficit);
			capital -= pagoReal;
			deficit += presupuestoMaximo - pagoReal;
		}

		capital += deficit;
	}

	private void enterrar(AbstractCollection<? extends Ser>... listas) {
		this.defuncionesPeriodoActual = 0;
		for (AbstractCollection<? extends Ser> poblacion : listas) {
			Iterator<? extends Ser> iterator = poblacion.iterator();
			while (iterator.hasNext()) {
				Ser ser = iterator.next();
				if (!ser.isVivo()) {
					this.defuncionesPeriodoActual++;
					iterator.remove();
				}
			}
		}
	}

	private void jubila(AbstractCollection<Adulto>... listas) {
		for (AbstractCollection<Adulto> lista : listas) {
			Iterator<Adulto> iterator = lista.iterator();
			while (iterator.hasNext()) {
				Adulto adulto = iterator.next();
				if (adulto.getEdadActual() >= edadJubilacion) {
					this.capital += adulto.getAhorros();
					iterator.remove();
					ancianos.getMiembros().add(new Ser(adulto));
				}
			}
		}
	}

	private void envejecer(ArrayList<? extends Ser> lista) {
		for (Ser ser : lista) {
			ser.envejecer();
		}
	}

	public void gestionarNacimiento(double produccionActual) {
		double produccionPeriodoAnterior = trabajadores.size() * cantidadProducidaPorTrabajador;
		double diferencialDeProduccion = produccionActual - produccionPeriodoAnterior;
		int diferencialDeSeres = (int) (diferencialDeProduccion / cantidadProducidaPorTrabajador);
		historialDiferenciales.add(diferencialDeSeres);
		if (historialDiferenciales.size() > 5) {
			historialDiferenciales.remove(0);
		}
		int suma = 0;
		for (Integer diferencial : historialDiferenciales) {
			suma += diferencial;
		}
		int mediaDiferenciales = suma / historialDiferenciales.size();

		int nacimientos = this.defuncionesPeriodoActual + mediaDiferenciales;
		for (int i = 0; i < nacimientos; i++) {
			this.menores.offerLast(new Menor(80, 100));
		}
	}

	public AbstractCollection<Menor> getMenores() {
		return menores.getMiembros();
	}

	public AbstractCollection<Adulto> getTrabajadores() {
		return trabajadores.getMiembros();
	}

	public Sector<Adulto> getSectorTrabajadores() {
		return trabajadores;

	}

	public Sector<Adulto> getSectorParados() {
		return parados;
	}

	public void setTrabajadores(Sector<Adulto> trabajadores) {
		this.trabajadores = trabajadores;
	}

	public void setParados(Sector<Adulto> parados) {
		this.parados = parados;
	}

	public AbstractCollection<Adulto> getParados() {
		return parados.getMiembros();
	}

	public AbstractCollection<Ser> getAncianos() {
		return ancianos.getMiembros();
	}

	public double getCapital() {
		return capital;
	}

	public void setCapital(double capital) {
		this.capital = capital;
	}
}