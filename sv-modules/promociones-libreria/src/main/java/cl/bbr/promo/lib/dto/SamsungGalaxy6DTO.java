package cl.bbr.promo.lib.dto;

import java.io.Serializable;

public class SamsungGalaxy6DTO implements Serializable {
	
	
	private static final long serialVersionUID = 8124188254292708793L;
	
	private int id;
	private String par_nombre;
	private String par_descripcion;
	private String stock;
	/**
	 * @return el id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id el id a establecer
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return el par_nombre
	 */
	public String getPar_nombre() {
		return par_nombre;
	}
	/**
	 * @param par_nombre el par_nombre a establecer
	 */
	public void setPar_nombre(String par_nombre) {
		this.par_nombre = par_nombre;
	}
	/**
	 * @return el par_descripcion
	 */
	public String getPar_descripcion() {
		return par_descripcion;
	}
	/**
	 * @param par_descripcion el par_descripcion a establecer
	 */
	public void setPar_descripcion(String par_descripcion) {
		this.par_descripcion = par_descripcion;
	}
	/**
	 * @return el stock
	 */
	public String getStock() {
		return stock;
	}
	/**
	 * @param stock el stock a establecer
	 */
	public void setStock(String stock) {
		this.stock = stock;
	}
	
	
	
}
