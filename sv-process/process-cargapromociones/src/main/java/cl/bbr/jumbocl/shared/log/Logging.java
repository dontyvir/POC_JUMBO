package cl.bbr.jumbocl.shared.log;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Logging {

	private String clase = "---";
	private Logger logger;

	public Logging( Object obj ) {
		this.clase = obj.getClass().getName();
        //PropertyConfigurator.configure("C:/workspace_rational/ListenerPOS/src/log4j_app.properties");
		//PropertyConfigurator.configure("C:/Java/eclipse/workspace/BOL/IRS Monitor - Listener/src/log4j_app.properties");
		PropertyConfigurator.configure("log4j_app.properties");
		this.logger = Logger.getLogger( this.clase );
	}
	
	public void debug( String texto ) {
		this.logger.debug(texto);
	}
	public void info( String texto ) {
		this.logger.info(texto);
	}
	public void warn( String texto ) {
		this.logger.warn(texto);
	}
	public void fatal( String texto ) {
		this.logger.fatal(texto);
	}
	public void error( String texto ) {
		this.logger.error(texto);
	}
	
	public void error( Exception ex ) {
		this.logger.error(ex.getMessage());
	}

	public void error( String mensaje, Exception ex ) {
		this.logger.error( mensaje + " --> " + ex.getMessage() );
	}

	public void inicio_comando() {
		logger.info( "---------------------------------------" );
		logger.info( "Inicio comando: " + getClass().getDeclaringClass().getName() );
		logger.info( "---------------------------------------" );
	}
	
	public void inicio_comando( Object obj ) {
		logger.info( "---------------------------------------" );
		logger.info( "Inicio comando: " + obj.getClass().getName() );
		logger.info( "---------------------------------------" );
	}
	
	public void fin_comando() {
		logger.info( "---------------------------------------" );
		logger.info( "Fin comando: " + getClass().getDeclaringClass().getName() );
		logger.info( "---------------------------------------" );
	}

	public void fin_comando( Object obj ) {
		logger.info( "---------------------------------------" );
		logger.info( "Fin comando: " + obj.getClass().getName() );
		logger.info( "---------------------------------------" );
	}
	
}

