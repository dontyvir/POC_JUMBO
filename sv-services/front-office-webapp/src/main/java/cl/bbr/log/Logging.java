package cl.bbr.log;

import org.apache.log4j.Logger;

/**
 * Clase que permite la generción de log en capas de front (BizDelegate, Comandos). 
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
	private Logger logger;
	/**
	 * Identificador del cliente
	 */
	private String usr_id = "GUEST";
	/**
	 * Nombre del cliente
	 */
	private String usr_nombre = "GUEST" ;
	
    /**
     * Session del Cliente
     */
    private String usr_session = "GUEST" ;
    
    /**
     * User Agent del Cliente
     */
    private String usr_agent = "NAVEGADOR" ;
    
    /**
     * IP del Cliente
     */
    private String usr_ip = "0.0.0.0" ;
    
	/**
	 * Constructor
	 * 
	 * @param obj	Clase
	 */
	public Logging( Object obj ) {
		this.clase = obj.getClass().getName();
		this.logger = Logger.getLogger( this.clase );
	}
	
	public Logging( Class clase ) {
		this.logger = Logger.getLogger( clase);
	}
	
	/**
	 * Setter para el identificador único y el nombre del cliente
	 * 
	 * @param id		Identificador único del cliente
	 * @param nombre	Nombre del cliente
	 */
	public void setUsuario( String id, String nombre ) {
		this.usr_id = id;
		this.usr_nombre = nombre;
	}
    
    public void setSession( String session ) {
        this.usr_session = session;
    }
    
    public void setUsrAgent( String usr_agent ) {
        this.usr_agent = usr_agent;
    }

    public void setUsrIP( String usr_ip ) {
        this.usr_ip = usr_ip;
    }
    
	/**
	 * Generar log tipo debug con el texto ingresado.
	 * 
	 * @param texto	Texto
	 */
	public void debug(String texto ) {
		this.logger.debug( "Usuario:" + this.usr_nombre + "(" + this.usr_id + ") - (" + this.usr_session + ") - (" + this.usr_ip + ") - (" + this.usr_agent + ") - " + texto);
	}
	
	/**
	 * Generar log tipo info con el texto ingresado.
	 * 
	 * @param texto	Texto
	 */
	public void info(String texto ) {
		this.logger.info( "Usuario:" + this.usr_nombre + "(" + this.usr_id + ") - (" + this.usr_session + ") - (" + this.usr_ip + ") - (" + this.usr_agent + ") - " +  texto);
	}
	/**
	 * Generar log tipo warning con el texto ingresado.
	 * 
	 * @param texto	Texto
	 */
	public void warn(String texto ) {
		this.logger.warn("Usuario:" + this.usr_nombre + "(" + this.usr_id + ") - (" + this.usr_session + ") - (" + this.usr_ip + ") - (" + this.usr_agent + ") - " +  texto);
	}
	/**
	 * Generar log tipo fatal con el texto ingresado.
	 * 
	 * @param texto	Texto
	 */
	public void fatal( String texto ) {
		this.logger.fatal("Usuario:" + this.usr_nombre + "(" + this.usr_id + ") - (" + this.usr_session + ") - (" + this.usr_ip + ") - (" + this.usr_agent + ") - " +  texto);
	}
	/**
	 * Generar log tipo error con el texto ingresado.
	 * 
	 * @param texto	Texto
	 */
	public void error( String texto ) {
		this.logger.error("Usuario:" + this.usr_nombre + "(" + this.usr_id + ") - (" + this.usr_session + ") - (" + this.usr_ip + ") - (" + this.usr_agent + ") - " +  texto);
	}	
	
	/**
	 * Generar log tipo error.
	 * 
	 * @param ex	Excepción a grabar
	 */
	public void error( Exception ex ) {
		this.logger.error( "Usuario:" + this.usr_nombre + "(" + this.usr_id + ") - (" + this.usr_session + ") - (" + this.usr_ip + ") - (" + this.usr_agent + ") - " + ex.getMessage());
	}

	/**
	 * Generar log tipo error.
	 * 
	 * @param mensaje	Mensaje a grabar
	 * @param ex		Excepción a grabar
	 */
	public void error( String mensaje, Exception ex ) {
		//20121029avc
	    if(ex.getMessage() == null)
	        this.logger.error( "Usuario:" + this.usr_nombre + "(" + this.usr_id + ") - (" + this.usr_session + ") - (" + this.usr_ip + ") - (" + this.usr_agent + ") - " + mensaje + " --> " + ex.getClass().getName() );
	    else
	    //20121029avc
	        this.logger.error( "Usuario:" + this.usr_nombre + "(" + this.usr_id + ") - (" + this.usr_session + ") - (" + this.usr_ip + ") - (" + this.usr_agent + ") - " + mensaje + " --> " + ex.getMessage() );
	}	

	/**
	 * Generar log de inicio de un comando.
	 */
	public void inicio_comando() {
		this.info( "Inicio comando: " + getClass().getDeclaringClass().getName() );
	}	
	
	/**
	 * Generar log de inicio de un comando.
	 * 
	 * @param obj	Clase a grabar
	 */
	public void inicio_comando( Object obj ) {
		this.info( "Inicio comando: " + obj.getClass().getName() );
	}
	
	/**
	 * Generar log de fin de un comando.
	 * 
	 */
	public void fin_comando() {
		this.info( "Fin comando: " + getClass().getDeclaringClass().getName() );
	}

	/**
	 * Generar log de fin de un comando.
	 * 
	 * @param obj	Clase a grabar
	 */
	public void fin_comando( Object obj ) {
		this.info( "Fin comando: " + obj.getClass().getName() );
	}	
	
}
