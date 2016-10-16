package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;

/**
 * DTO para datos de las regiones. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class RegionesDTO implements Serializable {

	private long id;
	private String nombre;
	private long orden;

	/**
	 * Constructor
	 */
	public RegionesDTO() {
	}
	
	/**
	 * Constructor 
	 * 
	 * @param id Identificador único
	 * @param nombre nombre
	 */
	public RegionesDTO(long id, String nombre) {
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
	
    /**
     * @return Devuelve orden.
     */
    public long getOrden() {
        return orden;
    }
    /**
     * @param orden El orden a establecer.
     */
    public void setOrden(long orden) {
        this.orden = orden;
    }
}