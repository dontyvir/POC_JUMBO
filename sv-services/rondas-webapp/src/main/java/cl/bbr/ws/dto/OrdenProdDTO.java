package cl.bbr.ws.dto;

public class OrdenProdDTO {
	private long id_detalle;
	private String cod_prod;
	private int orden;
	
	/**
	 * @return Devuelve cod_prod.
	 */
	public String getCod_prod() {
		return cod_prod;
	}
	/**
	 * @param cod_prod El cod_prod a establecer.
	 */
	public void setCod_prod(String cod_prod) {
		this.cod_prod = cod_prod;
	}
	/**
	 * @return Devuelve id_detalle.
	 */
	public long getId_detalle() {
		return id_detalle;
	}
	/**
	 * @param id_detalle El id_detalle a establecer.
	 */
	public void setId_detalle(long id_detalle) {
		this.id_detalle = id_detalle;
	}
	/**
	 * @return Devuelve orden.
	 */
	public int getOrden() {
		return orden;
	}
	/**
	 * @param orden El orden a establecer.
	 */
	public void setOrden(int orden) {
		this.orden = orden;
	}
}
