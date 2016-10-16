package cl.bbr.jumbocl.shared.log;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Genera el archivo log, que muestra los resultados de las diferentes operaciones.
 * 
 * @author BBR
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
	//private Logger logger;
	private Logger logger;
	private static Map Maplogger = new HashMap();
	//private static Properties prop = new Properties();
	//private static  java.io.InputStream log4jProps=null;
	/**
	 * Constructor
	 * 
	 * @param obj	Clase
	 */	
	public Logging( Object obj ) {
		this.clase = obj.getClass().getName();	
		//System.out.print("Object:::"+this.clase);

		try {
			if(Maplogger.get(this.clase)==null){
				java.io.InputStream log4jProps = this.getClass().getClassLoader().getResourceAsStream("/log4j_app.properties");
				Properties prop = new Properties();
				prop.load(log4jProps);				
				PropertyConfigurator.configure(prop);
				logger = Logger.getLogger(this.clase);
				Maplogger.put(this.clase,logger);
				//System.out.print("log4jProps::"+log4jProps.toString());
			}else{
				logger = (Logger) Maplogger.get(this.clase);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Logging( String cls ) {
		this.clase = cls;
		//System.out.print("Object:::"+this.clase);

		try {
			if(Maplogger.get(this.clase)==null){
				java.io.InputStream log4jProps = this.getClass().getClassLoader().getResourceAsStream("/log4j_app.properties");
				Properties prop = new Properties();
				prop.load(log4jProps);				
				PropertyConfigurator.configure(prop);
				logger = Logger.getLogger(this.clase);
				Maplogger.put(this.clase,logger);
				//System.out.print("log4jProps::"+log4jProps.toString());
			}else{
				logger = (Logger) Maplogger.get(this.clase);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*public static Logger getLogger(Object obj) {
		 
		 if (logger==null) {		 
			 new Logging(obj);
		 }
		 
		 return logger;
	}
	
	public static Logger getLogger(String cls) {
		 
		 if (logger==null) {		 
			 new Logging(cls);
		 }
		 
		 return logger;
	}*/
	
	
	/**
	 * Generar log tipo debug con el texto ingresado.
	 * 
	 * @param texto	Texto
	 */
	public void debug( String texto ) {
		if(logger.isDebugEnabled())
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
		logger.error(ex);
	}
	
	/**
	 * Generar log tipo error con el texto ingresado.
	 * 
	 * @param ex	Excepción a grabar
	 */
	public void error(Throwable throwable) {
		logger.error(throwable);
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
	 * Generar log de inicio de un comando.
	 */
	public void inicio_comando() {

		this.info( "---------------------------------------" );
		this.info( "Inicio comando: " + getClass().getDeclaringClass().getName() );
		this.info( "---------------------------------------" );
		
	}	
	
	/**
	 * Generar log de inicio de un comando.
	 * 
	 * @param obj	Clase a grabar
	 */
	public void inicio_comando( Object obj ) {

		this.info( "---------------------------------------" );
		this.info( "Inicio comando: " + obj.getClass().getName() );
		this.info( "---------------------------------------" );
	}
	
	/**
	 * Generar log de fin de un comando.
	 * 
	 */
	public void fin_comando() {
		
		this.info( "---------------------------------------" );
		this.info( "Fin comando: " + getClass().getDeclaringClass().getName() );
		this.info( "---------------------------------------" );
	}

	/**
	 * Generar log de fin de un comando.
	 * 
	 * @param obj	Clase a grabar
	 */
	public void fin_comando( Object obj ) {
		
		this.info( "---------------------------------------" );
		this.info( "Fin comando: " + obj.getClass().getName() );
		this.info( "---------------------------------------" );
	}
	
	/**
	 * Log de los datos que se recuperan desde la base de datos
	 * 
	 * @param metodo	Nombre del método para poder identificar luego la consulta
	 * @param rs		ResultSet con data del resultado de la consulta
	 */
	public void logData( String metodo, ResultSet rs ) {
		
		// Revisar datos que se recuperan desde la base de datos
		try {
			ResultSetMetaData rsmd = rs.getMetaData();			
			for( int i = 1; i <= rsmd.getColumnCount(); i++ ) {
				logger.debug( metodo + " - " + rsmd.getTableName(i) + "." + rsmd.getColumnName(i) + "-->" + rs.getString( rsmd.getColumnName(i) ) + "<" );
			}
		} catch (SQLException e) {
			logger.error("Problemas en logData", e);
			e.printStackTrace();
		}
	}	
}