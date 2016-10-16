package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;

/**
 * Clase que muestra informacion de Estados.
 * 
 * @author BBR
 *
 */
public class EstadoDTO implements Serializable{

	/**
	 * Id de estado
	 */
	private char	id_estado;
	
	/**
	 * Nombre 
	 */
	private String	nombre;
	
	public EstadoDTO(){
		
	}
	
	/**
	 * @param id_estado
	 * @param nombre
	 */
	public EstadoDTO(char id_estado , String nombre) {
		this.id_estado = id_estado;
		this.nombre = nombre;
	}
	
	/**
	 * @return id_estado
	 */
	public char getId_estado() {
		return id_estado;
	}
	
	/**
	 * @param id_estado , id_estado a modificar.
	 */
	public void setId_estado(char id_estado) {
		this.id_estado = id_estado;
	}
	
	/**
	 * @return nombre
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * @param nombre , nombre a modificar.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
}
