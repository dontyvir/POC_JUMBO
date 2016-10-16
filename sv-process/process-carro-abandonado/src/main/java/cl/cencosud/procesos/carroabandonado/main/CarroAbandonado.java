package cl.cencosud.procesos.carroabandonado.main;

import org.apache.log4j.Logger;

import cl.cencosud.procesos.carroabandonado.engine.Maestro;

public class CarroAbandonado {

	/**
	 *  logger de la clase
	 */
	protected static Logger logger = Logger.getLogger(CarroAbandonado.class.getName());
	

	
	/**
	 * Método main del aplicativo.
	 * Se encarga de realizar una instancia del maestro de la aplicación.
	 * Este maestro es quien se encarga de realizar la ejecución del proceso.
	 * 
	 * @param args argumentos entregados desde la llamada del jar del aplicativo.
	 */
	public static void main(String[] args) {
		logger.debug("[CarroAbandonado][main] Inicia metodo");

		Maestro master = new Maestro();

		try {
			logger.debug("[CarroAbandonado][main] inicio el process");
			master.process();
			logger.debug("[CarroAbandonado][main] proceso terminado");
			System.exit(0);
		} catch (Exception e) {
			logger.error("[CarroAbandonado][main] Error al ejecutar el proceso",e);
			System.exit(-1);
		}
	}
}
