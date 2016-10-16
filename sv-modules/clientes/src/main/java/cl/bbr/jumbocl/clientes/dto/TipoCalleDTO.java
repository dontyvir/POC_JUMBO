package cl.bbr.jumbocl.clientes.dto;

import java.io.Serializable;

public class TipoCalleDTO implements Serializable{
	private long id;
	private String nombre;
	private String estado;
	
	public TipoCalleDTO(){}
	
	public TipoCalleDTO(long id, String nombre, String estado){
		this.id = id;
		this.nombre = nombre;
		this.estado = estado;
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
	 * @return Returns the id.
	 */
	public long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(long id) {
		this.id = id;
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
