package cl.bbr.jumbocl.contenidos.dto;

import java.io.Serializable;

/**
 * Clase que muestra informacion del criterio de busqueda del producto.
 * 
 * @author BBR
 *
 */
public class ProductosCbarraDTO implements Serializable  {
	
	private long   pro_id;
	private String pro_tipo_prod;
	private String pro_desc_corta;
	private String pro_desc_larga;
	private String pro_estado;
	private String pro_unimed_desc;
	private String pro_cod_sap;
	private String pro_unimed;
	private String pro_cod_barra;
	private String pro_generico;
	/**
	 * @return Returns the pro_cod_barra.
	 */
	public String getPro_cod_barra() {
		return pro_cod_barra;
	}
	/**
	 * @return Returns the pro_cod_sap.
	 */
	public String getPro_cod_sap() {
		return pro_cod_sap;
	}
	/**
	 * @return Returns the pro_desc_corta.
	 */
	public String getPro_desc_corta() {
		return pro_desc_corta;
	}
	/**
	 * @return Returns the pro_desc_larga.
	 */
	public String getPro_desc_larga() {
		return pro_desc_larga;
	}
	/**
	 * @return Returns the pro_estado.
	 */
	public String getPro_estado() {
		return pro_estado;
	}
	/**
	 * @return Returns the pro_id.
	 */
	public long getPro_id() {
		return pro_id;
	}
	/**
	 * @return Returns the pro_tipo_prod.
	 */
	public String getPro_tipo_prod() {
		return pro_tipo_prod;
	}
	/**
	 * @return Returns the pro_unimed.
	 */
	public String getPro_unimed() {
		return pro_unimed;
	}
	/**
	 * @return Returns the pro_unimed_desc.
	 */
	public String getPro_unimed_desc() {
		return pro_unimed_desc;
	}
	/**
	 * @param pro_cod_barra The pro_cod_barra to set.
	 */
	public void setPro_cod_barra(String pro_cod_barra) {
		this.pro_cod_barra = pro_cod_barra;
	}
	/**
	 * @param pro_cod_sap The pro_cod_sap to set.
	 */
	public void setPro_cod_sap(String pro_cod_sap) {
		this.pro_cod_sap = pro_cod_sap;
	}
	/**
	 * @param pro_desc_corta The pro_desc_corta to set.
	 */
	public void setPro_desc_corta(String pro_desc_corta) {
		this.pro_desc_corta = pro_desc_corta;
	}
	/**
	 * @param pro_desc_larga The pro_desc_larga to set.
	 */
	public void setPro_desc_larga(String pro_desc_larga) {
		this.pro_desc_larga = pro_desc_larga;
	}
	/**
	 * @param pro_estado The pro_estado to set.
	 */
	public void setPro_estado(String pro_estado) {
		this.pro_estado = pro_estado;
	}
	/**
	 * @param pro_id The pro_id to set.
	 */
	public void setPro_id(long pro_id) {
		this.pro_id = pro_id;
	}
	/**
	 * @param pro_tipo_prod The pro_tipo_prod to set.
	 */
	public void setPro_tipo_prod(String pro_tipo_prod) {
		this.pro_tipo_prod = pro_tipo_prod;
	}
	/**
	 * @param pro_unimed The pro_unimed to set.
	 */
	public void setPro_unimed(String pro_unimed) {
		this.pro_unimed = pro_unimed;
	}
	/**
	 * @param pro_unimed_desc The pro_unimed_desc to set.
	 */
	public void setPro_unimed_desc(String pro_unimed_desc) {
		this.pro_unimed_desc = pro_unimed_desc;
	}
	/**
	 * @return Returns the pro_generico.
	 */
	public String getPro_generico() {
		return pro_generico;
	}
	/**
	 * @param pro_generico The pro_generico to set.
	 */
	public void setPro_generico(String pro_generico) {
		this.pro_generico = pro_generico;
	}
	
	
	
	
}
