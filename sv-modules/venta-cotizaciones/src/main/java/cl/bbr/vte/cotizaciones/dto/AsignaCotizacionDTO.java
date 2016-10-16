package cl.bbr.vte.cotizaciones.dto;

/**
 * DTO para asignar cotización. 
 * 
 * @author BBR e-commerce & retail
 *
 */
public class AsignaCotizacionDTO {
	private long id_cotizacion;
	private long id_usuario;
	private long id_motivo;
	private long id_cot_usr_act;
	private String usr_login;
	private String log;
	/**
	 * @return Returns the id_cot_usr_act.
	 */
	public long getId_cot_usr_act() {
		return id_cot_usr_act;
	}
	/**
	 * @return Returns the id_cotizacion.
	 */
	public long getId_cotizacion() {
		return id_cotizacion;
	}
	/**
	 * @return Returns the id_motivo.
	 */
	public long getId_motivo() {
		return id_motivo;
	}
	/**
	 * @return Returns the id_usuario.
	 */
	public long getId_usuario() {
		return id_usuario;
	}
	/**
	 * @return Returns the log.
	 */
	public String getLog() {
		return log;
	}
	/**
	 * @return Returns the usr_login.
	 */
	public String getUsr_login() {
		return usr_login;
	}
	/**
	 * @param id_cot_usr_act The id_cot_usr_act to set.
	 */
	public void setId_cot_usr_act(long id_cot_usr_act) {
		this.id_cot_usr_act = id_cot_usr_act;
	}
	/**
	 * @param id_cotizacion The id_cotizacion to set.
	 */
	public void setId_cotizacion(long id_cotizacion) {
		this.id_cotizacion = id_cotizacion;
	}
	/**
	 * @param id_motivo The id_motivo to set.
	 */
	public void setId_motivo(long id_motivo) {
		this.id_motivo = id_motivo;
	}
	/**
	 * @param id_usuario The id_usuario to set.
	 */
	public void setId_usuario(long id_usuario) {
		this.id_usuario = id_usuario;
	}
	/**
	 * @param log The log to set.
	 */
	public void setLog(String log) {
		this.log = log;
	}
	/**
	 * @param usr_login The usr_login to set.
	 */
	public void setUsr_login(String usr_login) {
		this.usr_login = usr_login;
	}

	
	
}
