package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;

public class TipoElementoDTO implements Serializable{
	
	private long id_tipo;
	private String nombre;
	private String estado;
	
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
	 * @return Returns the id_tipo.
	 */
	public long getId_tipo() {
		return id_tipo;
	}
	/**
	 * @param id_tipo The id_tipo to set.
	 */
	public void setId_tipo(long id_tipo) {
		this.id_tipo = id_tipo;
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
