package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

/**
 * Clase que actualiza los datos de una elemento. 
 * @author BBR
 *
 */
public class ProcModElementoDTO implements Serializable{
    /**
     * Nombre de la elemento. 
     */
    private String nombre; 
      
    /**
     * Descripción. 
     */
    private String descripcion; 
      
    /**
     * Estado. 
     */
    private String estado; 
      
    /**
     * Url_destino. 
     */
    private String url_destino; 

    /**
     * Tipo. 
     */
    private long tipo;

    /**
     * Usuario de modificación. 
     */
    private long usu_modif;
      
    /**
     * Id de la elemento 
     */
    private long id_elemento; // obligatorio
	
    /**
	 * Constructor inicial. 
	 */
	public ProcModElementoDTO() {
	}

	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
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
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return Returns the tipo.
	 */
	public long getTipo() {
		return tipo;
	}

	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(long tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return Returns the url_destino.
	 */
	public String getUrl_destino() {
		return url_destino;
	}

	/**
	 * @param url_destino The url_destino to set.
	 */
	public void setUrl_destino(String url_destino) {
		this.url_destino = url_destino;
	}

	/**
	 * @return Returns the usu_modif.
	 */
	public long getUsu_modif() {
		return usu_modif;
	}

	/**
	 * @param usu_modif The usu_modif to set.
	 */
	public void setUsu_modif(long usu_modif) {
		this.usu_modif = usu_modif;
	}

}
