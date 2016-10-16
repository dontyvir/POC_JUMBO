/*
 * Creado el 18-mar-2011
 *
 */
package cl.bbr.jumbocl.bolsas.dto;

/**
 * @author ctapiat
 *
 */
public class BolsaDTO {
	
	String cod_bolsa;
	String desc_bolsa;
	String cod_barra_bolsa;
	int stock;
	int id_producto;
	String cod_sucursal;
	String cod_sap;
	String act;

	
	/**
	 * @return Devuelve stock.
	 */
	public int getStock() {
		return stock;
	}
	/**
	 * @param stock El stock a establecer.
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}
	/**
	 * @return Devuelve cod_barra_bolsa.
	 */
	public String getCod_barra_bolsa() {
		return cod_barra_bolsa;
	}
	/**
	 * @param cod_barra_bolsa El cod_barra_bolsa a establecer.
	 */
	public void setCod_barra_bolsa(String cod_barra_bolsa) {
		this.cod_barra_bolsa = cod_barra_bolsa;
	}
	/**
	 * @return Devuelve cod_bolsa.
	 */
	public String getCod_bolsa() {
		return cod_bolsa;
	}
	/**
	 * @param cod_bolsa El cod_bolsa a establecer.
	 */
	public void setCod_bolsa(String cod_bolsa) {
		this.cod_bolsa = cod_bolsa;
	}
	/**
	 * @return Devuelve desc_bolsa.
	 */
	public String getDesc_bolsa() {
		return desc_bolsa;
	}
	/**
	 * @param desc_bolsa El desc_bolsa a establecer.
	 */
	public void setDesc_bolsa(String desc_bolsa) {
		this.desc_bolsa = desc_bolsa;
	}
	/**
	 * @return Devuelve cod_sucursal.
	 */
	public String getCod_sucursal() {
		return cod_sucursal;
	}
	/**
	 * @param cod_sucursal El cod_sucursal a establecer.
	 */
	public void setCod_sucursal(String cod_sucursal) {
		this.cod_sucursal = cod_sucursal;
	}
	/**
	 * @return Devuelve id_producto.
	 */
	public int getId_producto() {
		return id_producto;
	}
	/**
	 * @param id_producto El id_producto a establecer.
	 */
	public void setId_producto(int id_producto) {
		this.id_producto = id_producto;
	}
	/**
	 * @return Devuelve cod_sap.
	 */
	public String getCod_sap() {
		return cod_sap;
	}
	/**
	 * @param cod_sap El cod_sap a establecer.
	 */
	public void setCod_sap(String cod_sap) {
		this.cod_sap = cod_sap;
	}
	/**
	 * @return Devuelve act.
	 */
	public String getAct() {
		return act;
	}
	/**
	 * @param act El act a establecer.
	 */
	public void setAct(String act) {
		this.act = act;
	}
}
