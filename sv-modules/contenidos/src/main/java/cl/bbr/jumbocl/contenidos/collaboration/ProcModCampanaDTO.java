package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;

/**
 * Clase que actualiza los datos de una campana. 
 * @author BBR
 *
 */
public class ProcModCampanaDTO implements Serializable{
    /**
     * Nombre de la campana. 
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
     * Usuario de modificación. 
     */
    private long usu_modif;
      
      
    /**
     * Id de la campana 
     */
    private long id_campana; // obligatorio
	
    /**
	 * Constructor inicial. 
	 */
	public ProcModCampanaDTO() {
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
