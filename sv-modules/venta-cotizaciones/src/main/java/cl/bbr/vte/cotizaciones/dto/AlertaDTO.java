package cl.bbr.vte.cotizaciones.dto;

/**
 * DTO para las alertas. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class AlertaDTO {
	private long ale_id;
	private String ale_nom;
	private String ale_descr;
	private String ale_tipo;
	private int ale_orden;
	private String ale_activo;
	/**
	 * @return Returns the ale_activo.
	 */
	public String getAle_activo() {
		return ale_activo;
	}
	/**
	 * @return Returns the ale_descr.
	 */
	public String getAle_descr() {
		return ale_descr;
	}
	/**
	 * @return Returns the ale_id.
	 */
	public long getAle_id() {
		return ale_id;
	}
	/**
	 * @return Returns the ale_nom.
	 */
	public String getAle_nom() {
		return ale_nom;
	}
	/**
	 * @return Returns the ale_orden.
	 */
	public int getAle_orden() {
		return ale_orden;
	}
	/**
	 * @return Returns the ale_tipo.
	 */
	public String getAle_tipo() {
		return ale_tipo;
	}
	/**
	 * @param ale_activo The ale_activo to set.
	 */
	public void setAle_activo(String ale_activo) {
		this.ale_activo = ale_activo;
	}
	/**
	 * @param ale_descr The ale_descr to set.
	 */
	public void setAle_descr(String ale_descr) {
		this.ale_descr = ale_descr;
	}
	/**
	 * @param ale_id The ale_id to set.
	 */
	public void setAle_id(long ale_id) {
		this.ale_id = ale_id;
	}
	/**
	 * @param ale_nom The ale_nom to set.
	 */
	public void setAle_nom(String ale_nom) {
		this.ale_nom = ale_nom;
	}
	/**
	 * @param ale_orden The ale_orden to set.
	 */
	public void setAle_orden(int ale_orden) {
		this.ale_orden = ale_orden;
	}
	/**
	 * @param ale_tipo The ale_tipo to set.
	 */
	public void setAle_tipo(String ale_tipo) {
		this.ale_tipo = ale_tipo;
	}
	
	
}
