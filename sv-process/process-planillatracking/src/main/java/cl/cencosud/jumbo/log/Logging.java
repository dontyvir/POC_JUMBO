package cl.cencosud.jumbo.log;

//import java.io.File;
//import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Logging {

	private String clase = "---";
	private Logger logger;

	public Logging( Object obj ) {
		this.clase = obj.getClass().getName();
		//String Path = "";
		/*try{
			File fd = new File(".");
			System.out.println(fd.getCanonicalPath());
			Path = fd.getCanonicalPath();
		}catch(IOException e){
		    e.printStackTrace();
		}*/
        //PropertyConfigurator.configure("D:/IBM/WorkspaceDESA/TParis/ListenerPOS/src/log4j_app.properties");
		//PropertyConfigurator.configure("D:/IBM/WorkspaceDESA/CambioModeloOperacional/S&F/JavaSource/log4j.properties");
		PropertyConfigurator.configure("log4j.properties");
		this.logger = Logger.getLogger( this.clase );
	}
	
	public void debug( String texto ){
		this.logger.debug(texto);
	}
	public void info( String texto ){
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

