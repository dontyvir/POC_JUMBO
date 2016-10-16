package cl.bbr.vte.cotizaciones.dto;

/**
 * Clase que contiene información del log de la cotización
 * 
 * @author BBR
 *
 */
public class LogsCotizacionesDTO {
	private long cot_id;
	private String usuario;
	private String descripcion;
	private String fec_ing;
	/**
	 * @return Returns the cot_id.
	 */
	public long getCot_id() {
		return cot_id;
	}
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @return Returns the fec_ing.
	 */
	public String getFec_ing() {
		return fec_ing;
	}
	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param cot_id The cot_id to set.
	 */
	public void setCot_id(long cot_id) {
		this.cot_id = cot_id;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @param fec_ing The fec_ing to set.
	 */
	public void setFec_ing(String fec_ing) {
		this.fec_ing = fec_ing;
	}
	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	
	
}
