package cl.bbr.jumbocl.pedidos.promos.dto;

public class ProductoPromocionDTO {
	private long id_prodpromos;
	private long id_producto;
	private long id_local;
	
	private String nom_local;
	private String cod_prod1;
	private String uni_med;
	private String descr;
	private String tipo;
	private long cod_promo;
	
	/*
	private MonitorPromocionesDTO promo_normal;
	private MonitorPromocionesDTO promo_periodica;
	private MonitorPromocionesDTO promo_evento;
	*/
	
	/**
	 * @return Returns the cod_promo.
	 */
	public long getCod_promo() {
		return cod_promo;
	}

	/**
	 * @param cod_promo The cod_promo to set.
	 */
	public void setCod_promo(long cod_promo) {
		this.cod_promo = cod_promo;
	}

	/**
	 * @return Returns the cod_prod1.
	 */
	public String getCod_prod1() {
		return cod_prod1;
	}

	/**
	 * @param cod_prod1 The cod_prod1 to set.
	 */
	public void setCod_prod1(String cod_prod1) {
		this.cod_prod1 = cod_prod1;
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
	 * @return Returns the uni_med.
	 */
	public String getUni_med() {
		return uni_med;
	}

	/**
	 * @param uni_med The uni_med to set.
	 */
	public void setUni_med(String uni_med) {
		this.uni_med = uni_med;
	}

	/**
	 * @return Returns the nom_local.
	 */
	public String getNom_local() {
		return nom_local;
	}

	/**
	 * @param nom_local The nom_local to set.
	 */
	public void setNom_local(String nom_local) {
		this.nom_local = nom_local;
	}

	public ProductoPromocionDTO() {
		
	}

	/**
	 * @return Returns the id_local.
	 */
	public long getId_local() {
		return id_local;
	}

	/**
	 * @param id_local The id_local to set.
	 */
	public void setId_local(long id_local) {
		this.id_local = id_local;
	}

	/**
	 * @return Returns the id_prodpromos.
	 */
	public long getId_prodpromos() {
		return id_prodpromos;
	}

	/**
	 * @param id_prodpromos The id_prodpromos to set.
	 */
	public void setId_prodpromos(long id_prodpromos) {
		this.id_prodpromos = id_prodpromos;
	}

	/**
	 * @return Returns the id_producto.
	 */
	public long getId_producto() {
		return id_producto;
	}

	/**
	 * @param id_producto The id_producto to set.
	 */
	public void setId_producto(long id_producto) {
		this.id_producto = id_producto;
	}


	/**
	 * @return Returns the tipo.
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo The tipo to set.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	

}
