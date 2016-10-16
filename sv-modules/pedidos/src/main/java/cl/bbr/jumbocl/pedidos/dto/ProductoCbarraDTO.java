package cl.bbr.jumbocl.pedidos.dto;

import java.io.Serializable;

public class ProductoCbarraDTO implements Serializable{
	private long id_prod;
	private String cod_sap;
	private String uni_med;
	private String descripcion;
	private String cod_barra;
	/**
	 * @return Returns the cod_barra.
	 */
	public String getCod_barra() {
		return cod_barra;
	}
	/**
	 * @return Returns the cod_sap.
	 */
	public String getCod_sap() {
		return cod_sap;
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @return Returns the id_prod.
	 */
	public long getId_prod() {
		return id_prod;
	}
	/**
	 * @return Returns the uni_med.
	 */
	public String getUni_med() {
		return uni_med;
	}
	/**
	 * @param cod_barra The cod_barra to set.
	 */
	public void setCod_barra(String cod_barra) {
		this.cod_barra = cod_barra;
	}
	/**
	 * @param cod_sap The cod_sap to set.
	 */
	public void setCod_sap(String cod_sap) {
		this.cod_sap = cod_sap;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @param id_prod The id_prod to set.
	 */
	public void setId_prod(long id_prod) {
		this.id_prod = id_prod;
	}
	/**
	 * @param uni_med The uni_med to set.
	 */
	public void setUni_med(String uni_med) {
		this.uni_med = uni_med;
	}
	
	
}
