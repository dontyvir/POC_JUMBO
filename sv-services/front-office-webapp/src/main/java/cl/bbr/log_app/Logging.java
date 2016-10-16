package cl.bbr.log_app;

import java.sql.ResultSet;

import org.apache.log4j.Logger;

import cl.bbr.log.Log4JUtils;

/**
 * Clase que permite la generción de log en capas interiores (Servicios, controles, DAO). 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class Logging {	

	/**
	 * Nombre de la clase para el log
	 */
	private String clase = "---";
	/**
	 * Log
	 */
	private static Logger logger = null;

	/**
	 * Constructor
	 * 
	 * @param obj	Clase
	 */
	public Logging(Object obj) {
		
		this.clase = obj.getClass().getName();
		
		Log4JUtils.initializeLog4J();
		
		logger = Logger.getLogger(this.clase);

	}
	
	/**
	 * Generar log tipo debug con el texto ingresado.
	 * 
	 * @param texto	Texto
	 */
	public void debug( String texto ) {
		logger.debug(texto);
	}
	
	/**
	 * Generar log tipo info con el texto ingresado.
	 * 
	 * @param texto	Texto
	 */
	public void info( String texto ) {
		logger.info(texto);
	}
	
	/**
	 * Generar log tipo warning con el texto ingresado.
	 * 
	 * @param texto	Texto
	 */
	public void warn( String texto ) {
		logger.warn(texto);
	}
	
	/**
	 * Generar log tipo fatal con el texto ingresado.
	 * 
	 * @param texto	Texto
	 */
	public void fatal( String texto ) {
		logger.fatal(texto);
	}
	
	/**
	 * Generar log tipo error con el texto ingresado.
	 * 
	 * @param texto	Texto
	 */
	public void error( String texto ) {
		logger.error(texto);
	}	
	
	/**
	 * Generar log tipo error con el texto ingresado.
	 * 
	 * @param ex	Excepción a grabar
	 */
	public void error( Exception ex ) {
		logger.error(ex.getMessage());
	}

	/**
	 * Generar log tipo error.
	 * 
	 * @param mensaje	Mensaje a grabar
	 * @param ex		Excepción a grabar
	 */
	public void error( String mensaje, Exception ex ) {
		logger.error( mensaje + " --> " + ex.getMessage() );
	}	
	
	/**
	 * Log de los datos que se recuperan desde la base de datos
	 * 
	 * @param metodo	Nombre del método para poder identificar luego la consulta
	 * @param rs		ResultSet con data del resultado de la consulta
	 */
	public void logData( String metodo, ResultSet rs ) {
		
		// Revisar datos que se recuperan desde la base de datos
		/*
		try {
			ResultSetMetaData rsmd = rs.getMetaData();			
			for( int i = 1; i <= rsmd.getColumnCount(); i++ ) {
				logger.debug( metodo + " - " + rsmd.getTableName(i) + "." + rsmd.getColumnName(i) + "-->" + rs.getString( rsmd.getColumnName(i) ) + "<" );
			}
		} catch (SQLException e) {
			logger.error("Problemas en logData", e);
			e.printStackTrace();
		}
		*/
		
	}	

}
