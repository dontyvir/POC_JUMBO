package cl.cencosud.informes.colaborador.main;

import org.apache.log4j.Logger;

import cl.cencosud.informes.colaborador.engine.Maestro;

/**
 * Clase que se encarga de contener el método main del aplicativo de informe
 * descuento colaborador.
 *
 */
public class InformeDescuentoColaborador {

	/**
	 *  logger de la clase
	 */
	protected static Logger logger = Logger.getLogger(InformeDescuentoColaborador.class.getName());

	
	/**
	 * Método main del aplicativo.
	 * Se encarga de realizar una instancia del maestro de la aplicación.
	 * Este mestro es quien se encarga de realizar la ejecución del proceso.
	 * 
	 * @param args argumentos entregados desde la llamada del jar del aplicativo.
	 */
	public static void main(String[] args) {
		logger.debug("[InformeDescuentoColaborador][main] Inicia metodo");
		Maestro master = new Maestro();
		
		try {
			
			logger.debug("[InformeDescuentoColaborador][main] inicio el process");
			master.process();
			logger.debug("[InformeDescuentoColaborador][main] proceso terminado");
			System.exit(0);
			
		} catch (Exception e) {
			
			logger.error("[InformeDescuentoColaborador][main] Error al ejecutar el proceso",e);
			System.exit(-1);
		
		}
	}

}
