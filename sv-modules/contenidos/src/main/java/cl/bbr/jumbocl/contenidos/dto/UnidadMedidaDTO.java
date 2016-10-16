package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;

public class UnidadMedidaDTO implements Serializable{
	private long id;
	private String desc;
	private double cantidad;
	private String estado;

	/**
	 * @param id
	 * @param desc
	 * @param cantidad
	 * @param estado
	 */
	public UnidadMedidaDTO(long id, String desc, double cantidad, String estado) {
		super();
		this.id = id;
		this.desc = desc;
		this.cantidad = cantidad;
		this.estado = estado;
	}
	
	/**
	 * @return Returns the cantidad.
	 */
	public double getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	/**
	 * @return Returns the desc.
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc The desc to set.
	 */
	public void setDesc(String desc) {
		this.desc = desc;
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

}
