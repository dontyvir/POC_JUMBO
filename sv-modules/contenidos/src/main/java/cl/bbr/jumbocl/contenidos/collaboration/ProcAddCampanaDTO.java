package cl.bbr.jumbocl.contenidos.collaboration;

import java.io.Serializable;


/**
 * Clase que maneja información de una nueva campaña.
 * @author BBR
 *
 */
public class ProcAddCampanaDTO implements Serializable{
	private final static long serialVersionUID = 1;
	
    /**
     * Nombre de la campaña 
     */
    private String nombre;
    
    /**
     * Descripción de la campaña  
     */
    private String descripcion; 
    
    /**
     * Estado de la campaña
     */
    private String estado;
    
    /**
     * Fecha de creación de la campaña 
     */
    private String fec_crea;
    
	/**
	 * Constructor inicial. 
	 */
	public ProcAddCampanaDTO() {
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
	 * @return Returns the fec_crea.
	 */
	public String getFec_crea() {
		return fec_crea;
	}

	/**
	 * @param fec_crea The fec_crea to set.
	 */
	public void setFec_crea(String fec_crea) {
		this.fec_crea = fec_crea;
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
	
}
