package cl.bbr.vte.empresas.dto;

import java.io.Serializable;

/**
 * DTO para datos de los tipos de calle. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class TiposCallesDTO implements Serializable {

	private long id;
	private String nombre;

	/**
	 * Constructor
	 */
	public TiposCallesDTO() {
	}
	
	/**
	 * Constructor 
	 * 
	 * @param id Identificador único
	 * @param nombre nombre
	 */
	public TiposCallesDTO(long id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
}