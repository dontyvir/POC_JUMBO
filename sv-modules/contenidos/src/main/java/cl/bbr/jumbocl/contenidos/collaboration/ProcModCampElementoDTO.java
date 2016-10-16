package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

/**
 * Clase que contiene la información de la relación (a eliminar) entre la campaña y el elemento.
 * @author BBR
 *
 */
public class ProcModCampElementoDTO implements Serializable{
	
    /**
     * Id de campana.
     */
    private long id_campana; // obligatorio
    
    /**
     * Id del elemento. 
     */
    private long id_elemento; // obligatorio
    
    /**
     * Login del usuario. 
     */
    private String usr_login;
    
    /**
     * Mensaje a imprimir en el log. 
     */
    private String mensaje;
    
    /**
     * Accion a realizar
     */
    private String accion;

    /**
     * Numero de clicks
     */
    private long clicks;
    
	/**
	 * Constructor inicial.
	 */
	public ProcModCampElementoDTO() {
		super();
	}

	/**
	 * @return Returns the accion.
	 */
	public String getAccion() {
		return accion;
	}

	/**
	 * @param accion The accion to set.
	 */
	public void setAccion(String accion) {
		this.accion = accion;
	}

	/**
	 * @return Returns the id_campana.
	 */
	public long getId_campana() {
		return id_campana;
	}

	/**
	 * @param id_campana The id_campana to set.
	 */
	public void setId_campana(long id_campana) {
		this.id_campana = id_campana;
	}

	/**
	 * @return Returns the id_elemento.
	 */
	public long getId_elemento() {
		return id_elemento;
	}

	/**
	 * @param id_elemento The id_elemento to set.
	 */
	public void setId_elemento(long id_elemento) {
		this.id_elemento = id_elemento;
	}

	/**
	 * @return Returns the mensaje.
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje The mensaje to set.
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return Returns the usr_login.
	 */
	public String getUsr_login() {
		return usr_login;
	}

	/**
	 * @param usr_login The usr_login to set.
	 */
	public void setUsr_login(String usr_login) {
		this.usr_login = usr_login;
	}

	/**
	 * @return Returns the clicks.
	 */
	public long getClicks() {
		return clicks;
	}

	/**
	 * @param clicks The clicks to set.
	 */
	public void setClicks(long clicks) {
		this.clicks = clicks;
	}
	
	
}
