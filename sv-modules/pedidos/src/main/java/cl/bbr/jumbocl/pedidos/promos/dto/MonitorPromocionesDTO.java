package cl.bbr.jumbocl.pedidos.promos.dto;

public class MonitorPromocionesDTO {

	private long id_promocion;
	private String cod_promocion;
	private long id_local;
	private String nom_local;
	private String tipo_promo;
	private String descripcion;
	private String fecha_inicio;
	private String fecha_fin;
	
	/**
	 * Constructor
	 */
	public MonitorPromocionesDTO() {
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
	 * @return Returns the id_promocion.
	 */
	public long getId_promocion() {
		return id_promocion;
	}

	/**
	 * @param id_promocion The id_promocion to set.
	 */
	public void setId_promocion(long id_promocion) {
		this.id_promocion = id_promocion;
	}

	/**
	 * @return Returns the cod_promocion.
	 */
	public String getCod_promocion() {
		return cod_promocion;
	}

	/**
	 * @param cod_promocion The cod_promocion to set.
	 */
	public void setCod_promocion(String cod_promocion) {
		this.cod_promocion = cod_promocion;
	}

	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return Returns the fecha_fin.
	 */
	public String getFecha_fin() {
		return fecha_fin;
	}

	/**
	 * @param fecha_fin The fecha_fin to set.
	 */
	public void setFecha_fin(String fecha_fin) {
		this.fecha_fin = fecha_fin;
	}

	/**
	 * @return Returns the fecha_inicio.
	 */
	public String getFecha_inicio() {
		return fecha_inicio;
	}

	/**
	 * @param fecha_inicio The fecha_inicio to set.
	 */
	public void setFecha_inicio(String fecha_inicio) {
		this.fecha_inicio = fecha_inicio;
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

	/**
	 * @return Returns the tipo_promo.
	 */
	public String getTipo_promo() {
		return tipo_promo;
	}

	/**
	 * @param tipo_promo The tipo_promo to set.
	 */
	public void setTipo_promo(String tipo_promo) {
		this.tipo_promo = tipo_promo;
	}	
	
	


}
