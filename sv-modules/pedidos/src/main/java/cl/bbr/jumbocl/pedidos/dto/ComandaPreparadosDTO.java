package cl.bbr.jumbocl.pedidos.dto;

public class ComandaPreparadosDTO {
	private String id_pedido;
	private String cod_sap;
	private String uni_med;
	private String descr;
	private String sector;
	private String cantidad;
	private String obs;
	private String tipo_ve;
		
	public ComandaPreparadosDTO() {
	}

	/**
	 * @param id_pedido
	 * @param cod_sap
	 * @param uni_med
	 * @param descr
	 * @param sector
	 * @param cantidad
	 * @param obs
	 */
	public ComandaPreparadosDTO(String id_pedido, String cod_sap, String uni_med, String descr, String sector, String cantidad, String obs) {
		this.id_pedido = id_pedido;
		this.cod_sap = cod_sap;
		this.uni_med = uni_med;
		this.descr = descr;
		this.sector = sector;
		this.cantidad = cantidad;
		this.obs = obs;
	}

	/**
	 * @return Returns the tipo_ve.
	 */
	public String getTipo_ve() {
		return tipo_ve;
	}

	/**
	 * @param tipo_ve The tipo_ve to set.
	 */
	public void setTipo_ve(String tipo_ve) {
		this.tipo_ve = tipo_ve;
	}

	/**
	 * @return Returns the descr.
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * @param descr The descr to set.
	 */
	public void setDescr(String descr) {
		this.descr = descr;
	}

	/**
	 * @return Returns the cantidad.
	 */
	public String getCantidad() {
		return cantidad;
	}

	/**
	 * @return Returns the cod_sap.
	 */
	public String getCod_sap() {
		return cod_sap;
	}

	/**
	 * @return Returns the id_pedido.
	 */
	public String getId_pedido() {
		return id_pedido;
	}

	/**
	 * @return Returns the obs.
	 */
	public String getObs() {
		return obs;
	}

	/**
	 * @return Returns the sector.
	 */
	public String getSector() {
		return sector;
	}

	/**
	 * @return Returns the uni_med.
	 */
	public String getUni_med() {
		return uni_med;
	}

	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @param cod_sap The cod_sap to set.
	 */
	public void setCod_sap(String cod_sap) {
		this.cod_sap = cod_sap;
	}

	/**
	 * @param id_pedido The id_pedido to set.
	 */
	public void setId_pedido(String id_pedido) {
		this.id_pedido = id_pedido;
	}

	/**
	 * @param obs The obs to set.
	 */
	public void setObs(String obs) {
		this.obs = obs;
	}

	/**
	 * @param sector The sector to set.
	 */
	public void setSector(String sector) {
		this.sector = sector;
	}

	/**
	 * @param uni_med The uni_med to set.
	 */
	public void setUni_med(String uni_med) {
		this.uni_med = uni_med;
	}
	

}
