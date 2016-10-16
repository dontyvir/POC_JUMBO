//package cl.jumbo.ventaondemand.log;
//
//import java.io.IOException;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Properties;
//
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//
///**
// * Genera el archivo log, que muestra los resultados de las diferentes operaciones.
// * 
// * @author BBR
// *
// */
//public class Logging {
//	
//	private String clase = "---";	
//	private Logger logger;
//	private static Map Maplogger = new HashMap();
//
//	public Logging( Object obj ) {
//		this.clase = obj.getClass().getName();
//		try {
//			if(Maplogger.get(this.clase)==null){
//				java.io.InputStream log4jProps = this.getClass().getClassLoader().getResourceAsStream("/log4j_app.properties");
//				Properties prop = new Properties();
//				prop.load(log4jProps);
//				PropertyConfigurator.configure(prop);
//				logger = Logger.getLogger(this.clase);
//				Maplogger.put(this.clase,logger);
//			}else{
//				logger = (Logger) Maplogger.get(this.clase);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}
//		
//	public Logging( String cls ) {
//		this.clase = cls;
//		try {
//			if(Maplogger.get(this.clase)==null){
//				java.io.InputStream log4jProps = this.getClass().getClassLoader().getResourceAsStream("/log4j_app.properties");
//				Properties prop = new Properties();
//				prop.load(log4jProps);				
//				PropertyConfigurator.configure(prop);
//				logger = Logger.getLogger(this.clase);
//				Maplogger.put(this.clase,logger);
//			}else{
//				logger = (Logger) Maplogger.get(this.clase);
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void debug( String texto ) {
//		if(logger.isDebugEnabled())
//			logger.debug(texto);
//	}
//	
//	public void info( String texto ) {
//		logger.info(texto);
//	}
//	
//	public void warn( String texto ) {
//		logger.warn(texto);
//	}
//	
//	public void fatal( String texto ) {
//		logger.fatal(texto);
//	}
//	
//	public void error( String texto ) {
//		logger.error(texto);
//	}	
//	
//	public void error( Exception ex ) {
//		logger.error(ex);
//	}
//	
//	public void error(Throwable throwable) {
//		logger.error(throwable);
//	}
//
//	public void error( String mensaje, Exception ex ) {
//		logger.error( mensaje + " --> " + ex.getMessage() );
//	}	
//
//	public void inicio_comando() {
//		this.info( "---------------------------------------" );
//		this.info( "Inicio comando: " + getClass().getDeclaringClass().getName() );
//		this.info( "---------------------------------------" );
//		
//	}	
//	public void inicio_comando( Object obj ) {
//		this.info( "---------------------------------------" );
//		this.info( "Inicio comando: " + obj.getClass().getName() );
//		this.info( "---------------------------------------" );
//	}
//	
//	public void fin_comando() {		
//		this.info( "---------------------------------------" );
//		this.info( "Fin comando: " + getClass().getDeclaringClass().getName() );
//		this.info( "---------------------------------------" );
//	}
//
//	public void fin_comando( Object obj ) {		
//		this.info( "---------------------------------------" );
//		this.info( "Fin comando: " + obj.getClass().getName() );
//		this.info( "---------------------------------------" );
//	}
//	
//	/*
//	 * Revisar datos que se recuperan desde la base de datos
//	 */
//	public void logData( String metodo, ResultSet rs ) {		
//		
//		try {
//			ResultSetMetaData rsmd = rs.getMetaData();			
//			for( int i = 1; i <= rsmd.getColumnCount(); i++ ) {
//				logger.debug( metodo + " - " + rsmd.getTableName(i) + "." + rsmd.getColumnName(i) + "[" + rs.getString( rsmd.getColumnName(i) ) + "]" );
//			}
//		} catch (SQLException e) {
//			logger.error("Problemas en logData", e);
//			e.printStackTrace();
//		}
//	}	
//}