package cl.bbr.vte.cotizaciones.dto;

import java.io.Serializable;

/**
 * Clase que contiene la informacón de los estados 
 * 
 * @author BBR
 *
 */
public class EstadoDTO implements Serializable{

	private long	id_estado;
	private String	nombre;
	private String tipo_estado;
	/**
	 * @return Returns the tipo_estado.
	 */
	public String getTipo_estado() {
		return tipo_estado;
	}
	/**
	 * @param tipo_estado The tipo_estado to set.
	 */
	public void setTipo_estado(String tipo_estado) {
		this.tipo_estado = tipo_estado;
	}
	/**
	 * @return Returns the id_estado.
	 */
	public long getId_estado() {
		return id_estado;
	}
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param id_estado The id_estado to set.
	 */
	public void setId_estado(long id_estado) {
		this.id_estado = id_estado;
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	

}
