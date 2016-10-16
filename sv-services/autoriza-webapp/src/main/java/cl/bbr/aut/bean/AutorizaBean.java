/*
 * Creado el 23-09-2008
 *
 * TODO Para cambiar la plantilla de este archivo generado, vaya a
 * Ventana - Preferencias - Java - Estilo de código - Plantillas de código
 */
package cl.bbr.aut.bean;

import cl.bbr.common.framework.Autenticador;

/**
 * @author BBR
 *
 *  Webservices bean para la autenticacion con LDAP
 */
public class AutorizaBean {

	//protected Logging  logger = new Logging(this);
	
    public boolean doAuthLdap(String login, String pass){
    	
    	Autenticador auth = new Autenticador();
    	boolean res=false;
    	
    	System.out.println("Autenticacion[login="+login+"]");
    	//logger.debug("Autenticacion[login="+login+"]");
    	
    	// verifica conexion ldap
    	try {
    		//res =auth.doAuth(login, pass);
    		res =auth.doLDAPAuth(login, pass);
    	} catch(Exception e){
    		e.printStackTrace();
    		System.out.println("Error:"+e.getMessage());
    		//logger.debug("Error:"+e.getMessage());
    	}
		System.out.println("Autenticacion[login="+login+"]:"+res);
    	//logger.debug("Autenticacion[login="+login+"]:"+res);
    	//obtiene datos del perfil
    	return res;
    }
}
