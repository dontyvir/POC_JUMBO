package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;

/**
 * Clase que muestra informacion de Marcas.
 * 
 * @author BBR
 *
 */
public class MarcasDTO implements Serializable{
	
	/**
	 * Id dela marca 
	 */
	private long id;
	
	/**
	 * Nombre
	 */
	private String nombre;
	
	/**
	 * Estado
	 */
	private String estado;
	
	/**
	 * Cantidad de productos 
	 */
	private int cant_prods;
	
	/**
	 * Constructor inicial.
	 */
	public MarcasDTO() {
		super();
	}
	
	/**
	 * @param id
	 * @param nombre
	 * @param estado
	 */
	public MarcasDTO(long id , String nombre , String estado) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.estado = estado;
	}
	
	/**
	 * @return Retorna estado.
	 */
	public String getEstado() {
		return estado;
	}
	
	/**
	 * @param estado , estado a modificar.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * @return Retorna id.
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @param id , id a modificar.
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * @return Retorna nombre.
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

	/**
	 * @return Retorna cant_prods.
	 */
	public int getCant_prods() {
		return cant_prods;
	}

	/**
	 * @param cant_prods , cant_prods a modificar.
	 */
	public void setCant_prods(int cant_prods) {
		this.cant_prods = cant_prods;
	}
	

}
