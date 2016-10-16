package cl.cencosud.procesos;

import cl.cencosud.util.Logging;


/**
 * 
 * @author jolazogu
 *
 */
public class CargarStockOnline {
	
	private static Logging logger = new Logging( CargarStockOnline.class );
	
	public static void main( String[] args ) {
		
		logger.debug( "Inicio del proceso Cargar Stock Online " );
	
		try {
			
			logger.debug( "Inicio del proceso Cargar Stock Online" );
			
			ModificarIndicadorDeStock.ejecutar();
			
			logger.debug( "Fin del proceso de CargarStockOnline" );
			
		} catch ( Exception e ) {
			
			e.printStackTrace();
			logger.error( "El proceso no se ejecutó correctamente (ejecutar nuevamente)- problemas al procesar: " + e );
		
		}
	
	}
	
}
