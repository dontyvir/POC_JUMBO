package cl.bbr.vte.empresas.dto;

import java.io.Serializable;

/**
 * DTO para datos de las regiones 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class RegionesDTO implements Serializable{
	private long id;
	private String nombre;
	private long numero;
	
	/**
	 * Constructor
	 */
	public RegionesDTO() {
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

	public long getNumero() {
		return numero;
	}

	public void setNumero(long numero) {
		this.numero = numero;
	}
	


}
